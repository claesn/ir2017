package de.uni_koeln.spinfo.textengineering.ir.ranked;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.uni_koeln.spinfo.textengineering.ir.basic.Corpus;
import de.uni_koeln.spinfo.textengineering.ir.boole.PositionalIndex;

public class TestRankedIR {

	private Corpus corpus;
	private String query;
	private PositionalIndex index;
	private Set<Integer> result;
	// Neu: Wir benutzen einen Ranker, um das Ergebnis zu bewerten
	private Ranker ranker;

	@Before
	public void setUp() throws Exception {
		corpus = new Corpus("pg100.txt", "1[56][0-9]{2}\n");
		index = new PositionalIndex(corpus);
		query = "caesar brutus";
		/*
		 * Das Ranking erfolgt relativ zu einer Anfrage, deshalb initialisieren wir den Ranker mit der query:
		 */
		ranker = new Ranker(query, index);
	}

	@Test
	public void unrankedResults() {
		result = index.search(query);
		assertTrue("Ergebnis sollte nicht leer sein!", result.size() > 0);
		System.out.println(result.size() + " ungerankte Treffer für " + query);
		print(new ArrayList<Integer>(result));
	}

	@Test
	public void rankedResult() {
		result = index.search(query);
		assertTrue("Ergebnis sollte nicht leer sein!", result.size() > 0);
		System.out.println(result.size() + " gerankte Treffer für " + query);
		// Ergebnis ranken ...
		List<Integer> rankedResult = ranker.rank(result);
		print(rankedResult);
	}

	/*
	 * Hilfsmethode, um Ergebnisse übersichtlicher darzustellen.
	 */
	public void print(List<Integer> result) {
		System.out.println("-------------------------------");
		for (Integer i : result) {
			System.out.println(corpus.getWorks().get(i));
		}
		System.out.println("-------------------------------");
	}

}
