package de.uni_koeln.spinfo.textengineering.ir.tolerant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uni_koeln.spinfo.textengineering.ir.basic.Corpus;
import de.uni_koeln.spinfo.textengineering.ir.boole.InvertedIndex;
import de.uni_koeln.spinfo.textengineering.ir.preprocess.Preprocessor;

public class TolerantRetrieval extends InvertedIndex {

	public TolerantRetrieval(Corpus corpus) {
		super(corpus);
	}

	public Set<Integer> searchTolerant(String query) {
		
		Set<Integer> result = new HashSet<Integer>();
		List<String> queries = Arrays.asList(query.split("\\P{L}+"));

		System.out.println(queries);
		// erstmal für jede Teilquery das Zwischenergebnis sammeln:
		List<Set<Integer>> postingsLists = new ArrayList<Set<Integer>>();
		for (String q : queries) {
			Set<Integer> zwischenergebnis = super.index.get(q);
			System.out.println(zwischenergebnis);

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
