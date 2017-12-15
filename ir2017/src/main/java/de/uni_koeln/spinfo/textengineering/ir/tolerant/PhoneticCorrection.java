package de.uni_koeln.spinfo.textengineering.ir.tolerant;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;

public class PhoneticCorrection implements StringSimilarity {

	/**
	 * Phonetischer Stringvergleich mit dem Soundex-Algorithmus, hier unter Nutzung der entsprechenden
	 * Codec-Implementation von Apache Commons.
	 * 
	 * @see de.uni_koeln.spinfo.textengineering.ir.tolerant.StringSimilarity#getVariants(java.lang.String,
	 *      java.util.Set)
	 */
	@Override
	public List<String> getVariants(String q, List<String> terms) {

		List<String> variants = new ArrayList<>();
		Soundex soundex = new Soundex();
		for (String term : terms) {
			/*
			 * Soundex.difference() ergibt Werte von 0-4; hier bedeutet 4 höchste Ähnlichkeit
			 */
			try {
				int diff = soundex.difference(term, q);
				if (diff == 4) {
					variants.add(term);
				}
			} catch (EncoderException e) {
				e.printStackTrace();
			}
		}
		return variants;
	}

}
