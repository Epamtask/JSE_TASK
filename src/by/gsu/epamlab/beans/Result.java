package by.gsu.epamlab.beans;

import java.text.DateFormat;

public class Result {
	private String login;
	private String test;
	private java.sql.Date date;
	private int mark;

	private AbstractMark markType;
	private static final DateFormat OUTPUT_DATE_FORMAT = DateFormat.getDateInstance(DateFormat.MEDIUM);

	public Result(String login, String test, String date, String mark, AbstractMark markHandler) {
		super();
		this.login = login;
		this.test = test;
		this.date = java.sql.Date.valueOf(date);
		this.markType = markHandler;
		this.mark = markHandler.getMark(mark);

	}

	public Result() {		
		super();
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @return the test
	 */
	public String getTest() {
		return test;
	}

	/**
	 * @return the date
	 */
	public java.sql.Date getDate() {
		return date;
	}

	/**
	 * @return the mark
	 */
	public int getMark() {
		return mark;
	}

	/**
	 * @param login
	 *            the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @param test
	 *            the test to set
	 */
	public void setTest(String test) {
		this.test = test;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(String date) {
		this.date = java.sql.Date.valueOf(date);
	}

	public void setDate(java.sql.Date date) {
		this.date = date;
	}

	/**
	 * @param mark
	 *            the mark to set
	 */
	public void setMark(int mark) {
		this.mark = mark;
	}

	public void setMark(String mark) {
		this.mark = markType.getMark(mark);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {		
		return login + ";" + test + ";" + OUTPUT_DATE_FORMAT.format(date) + ";" + markType.markToString(mark);
	}

	/**
	 * @param markHandler the markHandler to set
	 */
	public void setMarkType(AbstractMark markHandler) {
		this.markType = markHandler;
	}

}
