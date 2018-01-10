package de.uni_koeln.spinfo.textengineering.ir.ranked;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uni_koeln.spinfo.textengineering.ir.basic.Work;
import de.uni_koeln.spinfo.textengineering.ir.boole.PositionalIndex;

public class TermWeighting {

	/**
	 * Umsetzung der tfIdf-Formel aus dem Seminar (siehe Folien).
	 * 
	 * @param t
	 * @param work
	 * @param index
	 * 
	 * @return Der tf-idf-Wert für t in Werk work.
	 */
	public static double tfIdf(String t, Work work, PositionalIndex index) {

		double tf;
		if (work.getTitle().equals("QUERY")) {
			tf = findMatches(t, work);// workaround, da das Query-Doc nicht im Index enthalten
		} else {
			tf = index.getTf(t, work);
		}
		double df = index.getDf(t);
		double n = index.getWorks().size();
		double idf = Math.log(n / df);

		double tfidf = Math.log(1 + tf) * idf;

		return tfidf;
	}

	/*
	 * Workaround: Ermittlung der Termfrequenz mittels Regex, weil die tf für in Bezug auf das Query-Dokument w nicht
	 * aus dem Index bezogen werden kann.
	 */
	private static double findMatches(String t, Work w) {
		Pattern pattern = Pattern.compile("\\b" + t + "\\b");
		Matcher matcher = pattern.matcher(w.getText());
		double tf = 0;
		// Zählt die Anzahl der matches des Patterns im Text des Werks
		while (matcher.find())
			tf++;
		return tf;
	}
}
