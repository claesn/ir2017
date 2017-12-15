package de.uni_koeln.spinfo.textengineering.ir.boole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import de.uni_koeln.spinfo.textengineering.ir.basic.Corpus;
import de.uni_koeln.spinfo.textengineering.ir.basic.Searcher;
import de.uni_koeln.spinfo.textengineering.ir.basic.Work;
import de.uni_koeln.spinfo.textengineering.ir.preprocess.Preprocessor;

public class InvertedIndex implements Searcher {

	protected Map<String, SortedSet<Integer>> index;// Unsere Zugriffsstruktur

	public InvertedIndex(Corpus corpus) {
		Long start = System.currentTimeMillis();
		index = index(corpus);
		System.out.println("Indexierung in: " + (System.currentTimeMillis() - start) + " ms.");
	}

	private Map<String, SortedSet<Integer>> index(Corpus corpus) {
		Map<String, SortedSet<Integer>> invIndex = new HashMap<String, SortedSet<Integer>>();
		// Der Index wird aufgebaut, indem ...
		List<Work> works = corpus.getWorks();
		// ... für jedes Werk ...
		for (int i = 0; i < works.size(); i++) {
			// ... Text tokenisiert wird ...
			Work work = works.get(i);
			List<String> terms = new Preprocessor().tokenize(work.getText());
			// ... um dann für jeden Term ...
			for (String t : terms) {
				// ... die postingsList aus dem Index zu holen.
				SortedSet<Integer> postingsList = invIndex.get(t);
				/*
				 * Beim ersten Vorkommen des Terms ist diese noch leer (null), also legen wir uns einfach eine neue an:
				 */
				if (postingsList == null) {
					postingsList = new TreeSet<Integer>();
					invIndex.put(t, postingsList);
				}
				/*
				 * Der Term wird indexiert, indem die Id des aktuellen Werks (= der aktuelle Zählerwert) der
				 * postings-list hinzugefügt wird:
				 */
				postingsList.add(i);
			}
		}
		System.out.println("Index erstellt, Größe: " + invIndex.size());
		return invIndex;
	}

	@Override
	public Set<Integer> search(String query) {
		// Suchen im Index
		Set<Integer> result = new HashSet<Integer>();
		List<String> queries = new Preprocessor().tokenize(query);

		// erstmal für jede Teilquery das Zwischenergebnis sammeln:
		List<Set<Integer>> postingsLists = new ArrayList<Set<Integer>>();
		for (String q : queries) {
			Set<Integer> zwischenergebnis = index.get(q);
			postingsLists.add(zwischenergebnis);
		}
		// Ergebnis ist die Schnittmenge (Intersection) der ersten Liste...
		result = postingsLists.get(0);
		// ... mit allen weiteren:
		for (Set<Integer> pl : postingsLists) {
			result.retainAll(pl);// AND-Verknüpfung
			// result.addAll(pl);// OR-Verknüpfung
		}
		return result;
	}

}
