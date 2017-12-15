package de.uni_koeln.spinfo.textengineering.ir.tolerant;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class EditDistance implements StringSimilarity {

	/**
	 * Stringvergleich mittels Levenshtein-Distanz, hier unter Verwendung der StringUtils (Apache Commons).
	 * 
	 * @see de.uni_koeln.spinfo.textengineering.ir.tolerant.StringSimilarity#getVariants(java.lang.String,
	 *      java.util.Set)
	 */
	@Override
	public List<String> getVariants(String q, List<String> terms) {
		
		List<String> variants = new ArrayList<>();
		for (String term : terms) {
			/*
			 * Die Levenshtein-Distanz entspricht der Anzahl an Operationen (insert, delete, replace), die nötig sind um
			 * einen String in einen anderen String zu überführen (hier: jeweils q und t).
			 */
			int ld = StringUtils.getLevenshteinDistance(term, q);
			if (ld <= 2) {
				variants.add(term);
			}
		}
		return variants;
	}

}
