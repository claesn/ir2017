package de.uni_koeln.spinfo.textengineering.ir.eval;

import java.util.List;

/*
 * Evaluation (Precision, Recall, F-Maß) einer Query und einer Dokumentenmenge gegen einen Goldstandard.
 */
public class Evaluation {


	public Evaluation(List<Integer> goldstandard) {
		
	}

	public EvaluationResult evaluate(List<Integer> retrieved) {
		/*
		 * Für das Suchergebnis müssen Precision, Recall und F-Maß errechnet werden. Grundlage sind die
		 * "true positives" , die anhand des Goldstandards ermittelt werden.
		 */
		
		//TODO
		
		double p = 0.0;
		double r = 0.0;
		double f = 0.0;

		return new EvaluationResult(p, r, f);
	}

}
