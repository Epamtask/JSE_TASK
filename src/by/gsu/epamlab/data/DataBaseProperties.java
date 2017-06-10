package by.gsu.epamlab.data;

import by.gsu.epamlab.beans.AbstractMark;


public class DataBaseProperties {

	public DataBaseProperties(String url, String login, String password, String dbName, String source, AbstractMark markType) {
		super();
		this.url = url;
		this.login = login;
		this.password = password;
		this.source=source;		
		this.markType = markType;		
		this.dbName=dbName ;
	}	
	private String login;
	private String password;
	private String url;
	private AbstractMark markType;	
	private String dbName;
	private String source;

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return the mark
	 */
	public AbstractMark getMarkType() {
		return markType;
	}

	/**
	 * @return the dbName
	 */
	public String getDbName() {
		return dbName;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

}
