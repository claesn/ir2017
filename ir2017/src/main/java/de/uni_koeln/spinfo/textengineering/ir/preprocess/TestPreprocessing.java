/**
 * 
 */
package de.uni_koeln.spinfo.textengineering.ir.preprocess;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

/**
 * @author spinfo
 *
 */
public class TestPreprocessing {

	private String testString = 
			  "Meine Mail: mail@spinfo.uni-koeln.de "
			+ "Meine Telefonnummer: 0221-4706675" 
			+ "Kann man auch so schreiben: 470 6675"
			+ "Meine Raumnummer: 3.014"
			+ "Dort bin ich zwischen 8.30 und 17 Uhr."
			+ "noch einiges an Genitiven und ähnlichen morphologischen Spielchen";

	@Test
	public void testPreprocessing() {
		System.out.println("Test Tokenizer:");
		System.out.println("-------------------");

		Preprocessor p = new Preprocessor();
		List<String> tokens = p.tokenize(testString);
		List<String> terms = p.getTerms(testString);
		List<String> stems = p.getStems(testString);
		
		// Assertions, z.B.
		assertTrue("Mehr Tokens als Terme erwartet!", tokens.size() >= terms.size());
		System.out.println("tokens:\t"+tokens);
		System.out.println("terms:\t"+terms);
		
		assertTrue("Mehr Terme als Stems erwartet!", terms.size() >= stems.size());
		System.out.println("stems:\t"+stems);
	}

}
