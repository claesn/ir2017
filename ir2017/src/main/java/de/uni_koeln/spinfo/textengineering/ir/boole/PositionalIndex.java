package de.uni_koeln.spinfo.textengineering.ir.boole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import de.uni_koeln.spinfo.textengineering.ir.basic.Corpus;
import de.uni_koeln.spinfo.textengineering.ir.basic.Searcher;
import de.uni_koeln.spinfo.textengineering.ir.basic.Work;
import de.uni_koeln.spinfo.textengineering.ir.preprocess.Preprocessor;

public class PositionalIndex implements Searcher {

	Map<String, SortedMap<Integer, List<Integer>>> index;// Zugriffsstruktur mit Positionen
	private Preprocessor p = new Preprocessor();
	// Zugriff auf tokens & Titel (siehe Methode printSnippets()):
	private Corpus corpus;

	public PositionalIndex(Corpus corpus) {
		Long start = System.currentTimeMillis();
		index = index(corpus);
		this.corpus = corpus;// Korpus für Ergebnisaufbereitung
		System.out.println("Indexierung in: " + (System.currentTimeMillis() - start) + " ms.");
	}

	private Map<String, SortedMap<Integer, List<Integer>>> index(Corpus corpus) {

		Map<String, SortedMap<Integer, List<Integer>>> posIndex = new HashMap<>();
		// Der Index wird aufgebaut, indem ...
		List<Work> works = corpus.getWorks();
		// ... für jedes Werk ...
		for (int i = 0; i < works.size(); i++) {
			// ... der Text tokenisiert wird ...
			Work work = works.get(i);
			// ... und zwar mit unserem eigenen Preprocessor ...
			List<String> terms = p.tokenize(work.getText());
			// ... um dann für jeden Term ...
			for (int j = 0; j < terms.size(); j++) {
				String t = terms.get(j);
				// ... die postingsMap aus dem Index zu holen.
				SortedMap<Integer, List<Integer>> postingsMap = posIndex.get(t);
				/*
				 * Beim ersten Vorkommen des Terms ist diese noch leer (null), also legen wir uns einfach eine neue an:
				 */
				if (postingsMap == null) {
					postingsMap = new TreeMap<Integer, List<Integer>>();
					posIndex.put(t, postingsMap);
				}
				// das gleiche machen wir mit der Liste der Positionen:
				List<Integer> positionList = postingsMap.get(i);
				if (positionList == null) {
					positionList = new ArrayList<>();
				}
				positionList.add(j);
				/*
				 * Der Term wird indexiert, indem die Id des aktuellen Werks (= der aktuelle Zählerwert) zusammen mit
				 * der aktualisierten Positions-Liste der postings-Map hinzugefügt wird:
				 */
				postingsMap.put(i, positionList);
			}
		}
		return posIndex;
	}

	/*
	 * Die 'einfache' Index-Suche: Gibt Werke zurück, die (Teil-)queries enthalten. Einziger Unterschied: Zugriff auf
	 * Postings über keySet().
	 */
	@Override
	public Set<Integer> search(String query) {
		long start = System.currentTimeMillis();
		Set<Integer> result = new HashSet<Integer>();
		List<String> queries = p.tokenize(query);
		List<Set<Integer>> postingsLists = new ArrayList<Set<Integer>>();
		for (String q : queries) {
			// Einzige Veränderung ggü der Suche im invertierten Index: Wir nehmen das keySet der Postings-Maps
			Set<Integer> zwischenergebnis = new HashSet<Integer>(index.get(q).keySet());
			// wir brauchen hier jeweils ein neues Set wg. call by reference (Veränderungen am Set wirken sich auch auf
			// die Map aus, aus dem wir das Set beziehen)
			postingsLists.add(zwischenergebnis);
		}
		result = postingsLists.get(0);
		for (Set<Integer> pl : postingsLists) {
			result.retainAll(pl);// AND-Verknüpfung
			// result.addAll(pl);// OR-Verknüpfung
		}
		System.out.println("Suchdauer: " + (System.currentTimeMillis() - start) + " ms.");
		return result;
	}

	/*
	 * Suche mit Beschränkung durch 'Nähe'. Grundidee: Positional Index als erweiterte Indexstruktur - zuerst wie bisher
	 * die Werke ermitteln, in denen beide Terme vorkommen, dann die PositionalIntersection "zuschalten". Vorteil:
	 * einfach "einklinken", ohne den Rest zu verändern.
	 */
	SortedMap<Integer, List<Integer>> proximitySearch(String query, int abstand) {

		List<String> queries = p.tokenize(query);
		List<SortedMap<Integer, List<Integer>>> allPostingsMaps = new ArrayList<>();
		for (String q : queries) {
			SortedMap<Integer, List<Integer>> postingsMap = index.get(q);
			allPostingsMaps.add(postingsMap);
		}
		// Ergebnis ist die Schnittmenge (Intersection) der ersten Map...
		SortedMap<Integer, List<Integer>> result = allPostingsMaps.get(0);
		// ... mit allen weiteren:
		for (SortedMap<Integer, List<Integer>> postingsMap : allPostingsMaps) {
			result = intersect(result, postingsMap, abstand);
		}
		return result;
	}

	/*
	 * Proximity Intersection unter Verwendung der Java-API.
	 */
	private SortedMap<Integer, List<Integer>> intersect(Map<Integer, List<Integer>> pl1,
			Map<Integer, List<Integer>> pl2, int abstand) {

		SortedMap<Integer, List<Integer>> answer = new TreeMap<>();

		// 1. nur Dokumente behalten, die in beiden postingsMaps enthalten sind:
		pl1.keySet().retainAll(pl2.keySet());

		// 2. nun noch alle Positionen testen:
		for (Integer workId : pl1.keySet()) {
			List<Integer> poslist1 = pl1.get(workId);
			List<Integer> poslist2 = pl2.get(workId);
			List<Integer> increment = new ArrayList<>();

			// Hilfsstruktur aufbauen: Jeweils erlaubten Abstand abziehen/hinzurechnen
			for (Integer pos : poslist1) {
				for (int i = -abstand; i <= abstand; i++) {
					if (!increment.contains(pos + i)) {
						increment.add(pos + i);
					}
				}
			}
			increment.retainAll(poslist2);
			if (increment.size() > 0) {
				answer.put(workId, increment);
			}
		}
		return answer;
	}

	/*
	 * Ergebnisdarstellung: Ausgabe von Fundstellen und Werktitel
	 */
	public void printSnippets(String query, SortedMap<Integer, List<Integer>> result, int maxDistance) {
		/*
		 * Da das Ergebnis nur die Position der letzten Teilquery enthält, sollte hier sowohl die Länge der Gesamtquery
		 * als auch der maximale Abstand berücksichtigt werden, innerhalb dessen die Terme auftreten dürfen, damit alle
		 * gesuchten Terme in der Ausgabe sichtbar sind.
		 */
		int queryLength = p.tokenize(query).size();
		int range = maxDistance + queryLength;

		for (Integer docId : result.keySet()) {
			// Werk als Tokenlist für Rekonstruktion der Fundstelle:
			Work work = corpus.getWorks().get(docId);
			List<String> tokens = p.tokenize(work.getText());
			// Die einzelnen Fundstellen:
			List<Integer> positions = result.get(docId);
			// Werktitel = erste Zeile des Werks
			String title = work.getTitle();
			System.out.println(
					String.format("'%s' %s-mal gefunden in Werk #%s (%s):", query, positions.size(), docId, title));
			for (Integer pos : positions) {
				// Textanfang und -ende abfangen (Math.max bzw. Math.min)
				int start = Math.max(0, pos - range);
				int end = Math.min(tokens.size(), pos + range);
				// Ausgabe der Position:
				System.out.print("Id " + docId + ", pos " + pos + ": ' ... ");
				for (int i = start; i <= end; i++) {
					System.out.print(tokens.get(i) + " ");
				}
				System.out.println(" ... '");
			}
		}
	}

	/**
	 * @return Alle Werke im Korpus.
	 */
	public List<Work> getWorks() {
		return corpus.getWorks();
	}

	/**
	 * @return Alle Terme im Korpus.
	 */
	public List<String> getTerms() {
		return new ArrayList<String>(index.keySet());
	}

	/**
	 * Die Dokumentenfrequenz eines Terms t in diesem Index.
	 * 
	 * @param t
	 * @return die Dokumentenfrequenz
	 */
	public double getDf(String t) {
		return index.get(t).size();
	}

	/**
	 * Die Termfrequenz eines Terms t in Bezug auf Werk w. Entspricht der Länge der Liste von Fundstellen (positions)
	 * des Terms in Werk w.
	 * 
	 * @param t
	 * @param w
	 * @return die Termfrequenz
	 */
	public double getTf(String t, Work w) {
		int indexOf = getWorks().indexOf(w);// Indexposition des Werks
		List<Integer> positions = index.get(t).get(indexOf);
		return positions == null ? 0 : positions.size();
	}
}
