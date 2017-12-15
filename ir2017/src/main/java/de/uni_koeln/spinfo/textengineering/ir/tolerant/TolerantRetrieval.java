package de.uni_koeln.spinfo.textengineering.ir.tolerant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import de.uni_koeln.spinfo.textengineering.ir.basic.Corpus;
import de.uni_koeln.spinfo.textengineering.ir.boole.InvertedIndex;
import de.uni_koeln.spinfo.textengineering.ir.preprocess.Preprocessor;

public class TolerantRetrieval extends InvertedIndex {

	public TolerantRetrieval(Corpus corpus) {
		super(corpus);
	}

	/**
	 * Tolerante Suche: entspricht im Grundaufbau der 'normalen' Suche. Unterschied: wird für eine (Teil-)query kein
	 * Treffer gefunden, werden Alternativen ermittelt.
	 * 
	 * @param query
	 *            das Suchwort
	 * @param sim
	 *            Das Objekt, in dem der Stringvergleich umgesetzt ist (z.B. EditDistance)
	 * @return Das Ergebnis der unscharfen Suche
	 * 
	 */
	public Set<Integer> searchTolerant(String query, StringSimilarity sim) {
		List<String> queries = new Preprocessor().getTerms(query);
		List<Set<Integer>> allPostingsLists = new ArrayList<Set<Integer>>();
		for (String q : queries) {
			Set<Integer> zwischenergebnis = index.get(q);
			/*
			 * Im Unterschied zur 'normalen' Suche machen wir hier einen null-Check und ermitteln ggf. Alternativen.
			 */
			if (zwischenergebnis == null) {
				System.out.println("Keine Treffer für Suchwort " + q + ", suche Varianten...");
				// Wenn kein Ergebnis, dann Varianten holen - und zwar davon die beste
				String best = getBestVariant(q, sim);
				System.out.println("Zeige stattdessen Ergebnisse für: " + best);
				zwischenergebnis = index.get(best);
			}
			allPostingsLists.add(zwischenergebnis);
		}
		Set<Integer> result = allPostingsLists.get(0);
		for (Set<Integer> pl : allPostingsLists) {
			result.retainAll(pl);// AND-Verknüpfung
			// result.addAll(pl);// OR-Verknüpfung
		}
		return result;
	}

	private String getBestVariant(String q, StringSimilarity sim) {

		// Wir holen uns zunächst Varianten zur query (abweichend je nach konkreter Implementierung):
		List<String> variants = sim.getVariants(q, new ArrayList<>(index.keySet()));
		System.out.println(sim.getClass().getSimpleName()+", Varianten: " + variants);
		/*
		 * Und ermitteln dann die 'beste' Variante, indem wir zu jedem Element der Liste eine eigene Suche starten und
		 * das Element mit der längsten Trefferliste behalten. Hierfür müssen wir die Ergebnisse absteigend sortieren:
		 */
		Map<Integer, String> map = new TreeMap<>(Collections.reverseOrder());
		for (String v : variants) {
			// Wir können hier die 'normale' Suche einsetzen, da wir sicher wissen, dass die Varianten im Index sind:
			Set<Integer> result = search(v);
			map.put(result.size(), v);
		}
		// das erste Element aus der sortierten Map:
		String best = map.values().iterator().next();
		System.out.println("Varianten und Trefferzahl: " + map);
		return best;
	}

}
