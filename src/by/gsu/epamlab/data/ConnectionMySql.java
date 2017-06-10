package by.gsu.epamlab.data;

import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionMySql {

	private java.sql.Connection connection;
	private final static ConnectionMySql cnMySql = new ConnectionMySql();
	private static boolean loadDriver = false;
	public static final String driver = "org.gjt.mm.mysql.Driver";
	private ConnectionMySql() {
		super();
	}

	public static ConnectionMySql creatConnection(DataBaseProperties properties)
			throws SQLException, ClassNotFoundException {
		if (cnMySql.connection == null) {			
			// not necessary
			if (!loadDriver) {				
				Class.forName(driver);
				loadDriver = true;
			}
			
			cnMySql.connection = DriverManager.getConnection(properties.getUrl(),
					properties.getLogin(), properties.getPassword());			
		}
		return cnMySql;
	}

	public void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}
		connection = null;	
	}

	/**
	 * @return the connection
	 */
	public java.sql.Connection getConnection() {
		return connection;
	}

}