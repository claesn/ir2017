/**
 * 
 */
package de.uni_koeln.spinfo.textengineering.ir.boole;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import de.uni_koeln.spinfo.textengineering.ir.basic.Corpus;

/**
 * @author spinfo
 *
 */
public class TestBooleanIR {

	private static Corpus corpus;
	private String query;

	@BeforeClass
	public static void setUp() throws Exception {
		// Korpus einlesen und in Werke unterteilen:
		String filename = "pg100.txt";
		String delimiter = "1[56][0-9]{2}\n";
		corpus = new Corpus(filename, delimiter);
	}

	@Test
	public void testMatrix() {
		// Aufbau der Matrix testen
		TermDokumentMatrix searcher = new TermDokumentMatrix(corpus);
		((TermDokumentMatrix) searcher).printMatrix();
	}

	@Test
	public void testMatrixSearch() {
		// Testen, ob Suche in Term-Dokument-Matrix ein Ergebnis liefert:

		System.out.println();
		System.out.println("Term-Dokument-Matrix:");
		System.out.println("-------------------");

		TermDokumentMatrix searcher = new TermDokumentMatrix(corpus);
		
		query = "Brutus";

		Set<Integer> result = searcher.search(query);
		assertTrue("Ergebnis sollte nicht leer sein", result.size() > 0);
		System.out.println("OR-Ergebnis für " + query + ": " + result);
	}
}
