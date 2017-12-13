package de.uni_koeln.spinfo.textengineering.ir.tolerant;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class EditDistance implements StringSimilarity {

	@Override
	public List<String> getVariants(String q, List<String> terms) {
		List<String> variants = new ArrayList<>();
		for (String term : terms) {
			int ld = StringUtils.getLevenshteinDistance(term, q);
			if (ld <= 1) {
				variants.add(term);
			}
		}
		return variants;
	}

}
