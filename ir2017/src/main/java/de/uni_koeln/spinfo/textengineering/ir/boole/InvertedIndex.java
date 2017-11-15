package de.uni_koeln.spinfo.textengineering.ir.boole;

import java.util.Arrays;
import java.util.Collections;
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

public class InvertedIndex implements Searcher {

	Map<String, SortedSet<Integer>> index;// Unsere Zugriffsstruktur

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
			List<String> terms = Arrays.asList(work.getText().split("\\P{L}+"));
			Collections.sort(terms);// (das hier muss nicht unbedingt sein, machen wir nur, um konform zu den Folien zu bleiben...)
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
		return invIndex;
	}

	@Override
	public Set<Integer> search(String query) {
		//Suchen im Index
		long start = System.currentTimeMillis();
		Set<Integer> result = new HashSet<Integer>();
		List<String> queries = Arrays.asList(query.split("\\s+"));

		for (String q : queries) {
			Set<Integer> zwischenergebnis = index.get(q);
			result = zwischenergebnis;
		}
		// TODO Teilergebnisse kombinieren (z.B. analog zu TD-Matrix) ...

		System.out.println("Suchdauer: " + (System.currentTimeMillis() - start) + " ms.");
		return result;
	}

}
