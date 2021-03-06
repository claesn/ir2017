/**
 * 
 */
package de.uni_koeln.spinfo.textengineering.ir.boole;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import org.junit.BeforeClass;
import org.junit.Test;

import de.uni_koeln.spinfo.textengineering.ir.basic.Corpus;
import de.uni_koeln.spinfo.textengineering.ir.basic.Searcher;

/**
 * @author spinfo
 *
 */
public class TestPositionalIndex {

	private static Corpus corpus;
	private String query;
	private Searcher searcher;

	@BeforeClass
	public static void setUp() throws Exception {
		// Korpus einlesen und in Werke unterteilen:
		String filename = "pg100.txt";
		String delimiter = "1[56][0-9]{2}\n";
		corpus = new Corpus(filename, delimiter);
	}

	@Test
	public void testPositionalIndex() {
		// Testen, ob Suche in invertiertem Index ein Ergebnis liefert

		System.out.println();
		System.out.println("Positional Index:");
		System.out.println("-------------------");
		searcher = new PositionalIndex(corpus);

		/*
		 * Standard-Suche (wie bisher):
		 */
		query = "Brutus Caesar";
		Set<Integer> result = searcher.search(query);
		assertTrue("Ergebnis sollte nicht leer sein", result.size() > 0);
		System.out.println("Ergebnis für " + query + ": " + result);
		for (Integer id : result) {
			System.out.println("id: " + id + " - " + corpus.getWorks().get(id));
		}

		/*
		 * Proximity-Suche:
		 */
		query = "to be or not to be";
		int range = 1;
		System.out.println("-------- Suche mit range = " + range + " ----------");
		SortedMap<Integer, List<Integer>> posResult = ((PositionalIndex) searcher).proximitySearch(query, range);
		assertTrue("ergebnis sollte nicht leer sein!", posResult.size() > 0);
		System.out.println("Ergebnis für " + query + ": " + posResult.keySet());
		((PositionalIndex) searcher).printSnippets(query, posResult, 1);

	}

}
