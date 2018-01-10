package de.uni_koeln.spinfo.textengineering.ir.ranked;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import de.uni_koeln.spinfo.textengineering.ir.basic.Work;
import de.uni_koeln.spinfo.textengineering.ir.boole.PositionalIndex;

public class Ranker {

	/*
	 * Query und Index werden als Klassenvariablen direkt bei der Instantiierung gesetzt (siehe Konstruktor), da sie bei
	 * Aufruf der Methode rank() für jede Vergleichsoperation in sort benötigt werden, um die Cosinus-Ähnlichkeit jedes
	 * Works des result-Sets zur Query zu ermitteln.
	 */
	private Work query;
	private PositionalIndex index;

	public Ranker(String query, PositionalIndex index) {
		// hier wird aus der query ein kleines Document erzeugt (text = query, title = "QUERY")
		this.query = new Work("QUERY", query);
		this.index = index;
	}

	/**
	 * Ranking des Ergebnis-Sets, implementiert als einfache Sortierung nach Ähnlichkeit zum Query-Dokument.
	 * 
	 * @param result
	 * @return Ergebnis als Liste, sortiert nach Ähnlichkeit.
	 */
	public List<Integer> rank(Set<Integer> result) {

		// Das result-Set wird für die Sortierung zunächst in eine Liste von Werken umgewandelt:
		List<Work> toSort = new ArrayList<>();
		for (Integer integer : result) {
			Work work = index.getWorks().get(integer);
			toSort.add(work);
		}

		/*
		 * Java stellt für Collections (Listen, Maps, etc) die Methode sort() bereit, der man einen Sortierschlüssel
		 * (einen Comparator) übergeben kann. Wir wollen Dokumente anhand ihrer Ähnlichkeit zur query sortieren, deshalb
		 * müssen wir uns zunächst einen geeigneten Comparator schreiben:
		 */
		Collections.sort(toSort, new Comparator<Work>() {
			public int compare(Work w1, Work w2) {
				/*
				 * Wir sortieren alle Vektoren nach ihrer (Cosinus-) Ähnlichkeit zur Anfrage (query), dazu benötigen wir
				 * zunächst die Ähnlichkeiten von w1 zur Query und w2 zur Query:
				 */
				Double s1 = similarity(query, w1);
				Double s2 = similarity(query, w2);
				/*
				 * Anschließend sortieren wir nach diesen beiden Ähnlichkeiten. Wir wollen absteigende Ähnlichkeit, d.h.
				 * s2.compareTo(s1) statt s1.compareTo(s2) d.h. die höchsten Werte und damit besten Treffer zuerst:
				 */
				return s2.compareTo(s1);
			}
		});

		// abschließend müssen wir die sortierte Liste wieder in eine Liste von Integer umwandeln:
		List<Integer> toReturn = new ArrayList<>();
		for (Work work : toSort) {
			int indexOf = index.getWorks().indexOf(work);
			toReturn.add(indexOf);
		}
		return toReturn;
	}

	/**
	 * Die Cosinus-Ähnlichkeit eines Werks zu einer query. Die eigentliche Ähnlichkeitsberechnung delegieren wir an
	 * eine Vergleichstrategie, implementiert in der Klasse VectorComparison.
	 * 
	 * @param query
	 * @param index
	 * 
	 * @return Die Cosinus-Ähnlichkeit dieses Dokuments zu einer query.
	 */
	public Double similarity(Work query, Work document) {

		// TODO: wie erhalten wir die Ähnlichkeit zwischen Query und Dokument?
		
		return null;
	}

}
