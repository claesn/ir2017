package de.uni_koeln.spinfo.textengineering.ir.basic;

public class Work {

	private String title;
	private String text;

	/**
	 * Erzeugt ein neues Dokument bestehend aus Titel und Text. 
	 * @param title
	 * @param work
	 */
	public Work(String title, String text) {
		this.setTitle(title);
		this.setText(text);
	}

	/**
	 * Zugriff auf den Titel
	 * 
	 * @return Der Titel des Dokuments.
	 */
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Zugriff auf den Text
	 * 
	 * @return Der Inhalt des Dokuments als String.
	 */
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String toString() {
		return String.format("Titel: %s", title);
	}

}
