package de.uni_koeln.spinfo.textengineering.ir.basic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Corpus {

	private String text;

	public Corpus(String location) {

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
		setText(sb.toString());
		
		//TODO: Corpusstruktur?
		
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

}
