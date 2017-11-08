package de.uni_koeln.spinfo.textengineering.ir.basic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Corpus {

	private String text;
	private List<Work> works;

	/**
	 * Einlesen einer Textdatei, die in ein Korpus von Dokumenten (hier: Werke) überführt wird.
	 * 
	 * @param location
	 *            Der Pfad zur Datei, die eingelesen wird.
	 * @param delimiter
	 *            Regulärer Ausdruck, an dem der gelesene Text getrennt wird.
	 */
	public Corpus(String location, String delimiter) {

		StringBuilder sb = new StringBuilder();
		try {
			Scanner scanner = new Scanner(new File(location));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				sb.append(line);
				sb.append("\n"); // explizites newline (für regex)
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		text = sb.toString();
		/*
		 * Wir splitten den eingelesenen Text in Teilstrings, diese werden selbst nochmals in Titel und Text gesplittet
		 * und in einem Work-Objekt gekapselt. Dabei lassen wir das erste "Werk" weg (Lizenzvereinbarung etc.):
		 */
		works = new ArrayList<Work>();
		String[] split = text.split(delimiter);
		// wir wandeln das Array in eine Liste um ...
		List<String> worksAsList = Arrays.asList(split);
		// ... so können wir beim Iterieren mit dem zweiten Element beginnen (das erste "Werk" ist der Disclaimer von
		// gutenberg.org)
		for (String work : worksAsList.subList(1, worksAsList.size())) {
			/*
			 * trim() schneidet überschüssige Leerzeichen ab, indexOf() gibt die erste Position des delimiters im Text
			 * zurück - damit erhalten wir einen bereinigten Teilstring, der vom Beginn des Dokuments bis zum ersten
			 * Vorkommen des delimiters reicht (= der Titel)
			 */
			String title = work.trim().split("\n")[0];
			works.add(new Work(title, work));
		}
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	public List<Work> getWorks() {
		return works;
	}

}
