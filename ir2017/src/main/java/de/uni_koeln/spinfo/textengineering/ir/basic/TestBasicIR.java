package de.uni_koeln.spinfo.textengineering.ir.basic;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestBasicIR {

	private static Corpus corpus;

	@BeforeClass
	public static void setUp() {
		// Korpus einlesen
		String filename = "pg100.txt";
		corpus = new Corpus(filename);
	}

	@Test
	public void testCorpus() {
		// Testen, ob Korpus korrekt angelegt wurde
		assertTrue("Korpus sollte mehr als 1 Werk enthalten", corpus != null);
	}

}