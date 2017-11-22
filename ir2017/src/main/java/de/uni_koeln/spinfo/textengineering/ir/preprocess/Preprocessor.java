package de.uni_koeln.spinfo.textengineering.ir.preprocess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Ein sehr einfacher Preprocessor: splittet und gibt Tokens oder sortierte Types zurück.
 */

public final class Preprocessor {

	/*
	 * Regulärer Ausdruck für einfache Telefonnummern (0221-4701751), Versionsnummern (8.04), Geldbeträge (3,50) und
	 * Uhrzeiten (15:15).
	 */
	private static final String COMPOUND_NUMBER = "\\d+[-.,:]\\d+";
	/*
	 * Regulärer Ausdruck für einfache Zahlen, wenn man die etwa von obigen unterscheiden will.
	 */
	private static final String SIMPLE_NUMBER = "\\d+";
	/*
	 * Emailadressen für einige Domains, mit Unterstützung von Punkten im Domainnamen (wie in
	 * neuefeind@spinfo.uni-koeln.de), als Beispiel was man sonst so mit regulären Ausdrücken machen kann.
	 */
	private static final String EMAIL = "[^@\\s]+@.+?\\.(de|com|eu|org|net)";

	/*
	 * Ein Unicode-wirksamer Ausdruck für "Nicht-Buchstabe", der auch Umlaute berücksichtigt; die einfache (ASCII)
	 * Version ist: "\\W"
	 */
	private static final String UNICODE_DELIMITER = "[^\\p{L}]";

	private List<String> specialCases;
	private String delimiter;

	/*
	 * default Konstruktor mit Spezialfällen und Standard-Regex für splits
	 */
	Preprocessor() {
		// Einige Spezialfälle:
		this.specialCases = new ArrayList<String>();
		specialCases.add(COMPOUND_NUMBER);
		specialCases.add(SIMPLE_NUMBER);
		specialCases.add(EMAIL);
		
		// Und der Standardfall: Splitten am Whitespace
		delimiter = UNICODE_DELIMITER;
	}

	public List<String> tokenize(String text) {
		/* Einheitliches lower-casing */
		text = text.toLowerCase();
		List<String> result = new ArrayList<String>();

		for (String regex : specialCases) {
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(text);
			while (matcher.find()) {
				String group = matcher.group();
				result.add(group);
				text = text.replace(group, "");
			}
		}
		System.out.println(result);
		/* den Rest nochmal splitten, leere Strings filtern: */
		List<String> tokens = Arrays.asList(text.split(delimiter));
		for (String s : tokens) {
			if (s.trim().length() > 0) {
				result.add(s.trim());
			}
		}
		System.out.println(result);
		return result;
	}

	/*
	 * Gibt eine Liste aller Terme zurück (enthält jeden Term nur ein Mal)
	 */
	public List<String> getTerms(String text) {

		// TODO

		return null;
	}

	/*
	 * Gibt eine Liste der Wortstämme zurück
	 */
	public List<String> getStems(String testString) {

		// TODO Stemming

		return null;
	}

}
