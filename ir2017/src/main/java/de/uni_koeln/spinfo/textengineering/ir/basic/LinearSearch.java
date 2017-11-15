package de.uni_koeln.spinfo.textengineering.ir.basic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LinearSearch implements Searcher {

	private List<Work> works;

	public LinearSearch(Corpus corpus) {
		works = corpus.getWorks();
	}

	/* (non-Javadoc)
	 * @see de.uni_koeln.spinfo.textengineering.ir.basic.Searcher#search(java.lang.String)
	 */
	@Override
	public Set<Integer> search(String query) {

		long start = System.currentTimeMillis();
		Set<Integer> result = new HashSet<>();
		// Anstelle eines Query-Parsers hier ein einfaches split:
		String[] queries = query.split("\\P{L}+");

		for (String q : queries) {
			for (Work work : works) {
				String text = work.getText();
				List<String> tokens = Arrays.asList(text.split("\\P{L}+"));
				for (String t : tokens) {
					if (t.compareTo(q) == 0) {
						result.add(works.indexOf(work));
						break;
					}
				}
			}
		}
		System.out.println("Dauer: " + (System.currentTimeMillis() - start) + " ms.");
		return result;
	}
}
