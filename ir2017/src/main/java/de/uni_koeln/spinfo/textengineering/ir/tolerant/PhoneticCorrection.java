package de.uni_koeln.spinfo.textengineering.ir.tolerant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;

public class PhoneticCorrection implements StringSimilarity {

	@Override
	public List<String> getVariants(String q, List<String> terms) {
		
		List<String> variants = new ArrayList<>();
		Soundex soundex = new Soundex();
		try {
			for (String term : terms) {
				int diff;
				diff = soundex.difference(term, q);
				if (diff == 4) {
					variants.add(term);
				}
			}
		} catch (EncoderException e) {
			e.printStackTrace();
		}
		return variants;
	}

}
