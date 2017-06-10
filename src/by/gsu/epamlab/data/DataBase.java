package by.gsu.epamlab.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import by.gsu.epamlab.beans.AbstractMark;
import by.gsu.epamlab.beans.Result;
import by.gsu.epamlab.interfaces.ReadData;

public class DataBase {

	private String dbName = null;
	private Connection cn;
	private boolean defaultDbName = false;
	private final AbstractMark markHandler;

	public DataBase(DataBaseProperties properties, ConnectionMySql cnMySql, AbstractMark mark) {
		super();

		this.dbName = properties.getDbName();
		this.cn = cnMySql.getConnection();
		this.markHandler = mark;
		defaultDbName = true;
	}

	private void ResultSetClose(ResultSet... rs) {
		for (ResultSet resultSet : rs) {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.err.println(e);
				}
			}
		}
	}

	private void StatementClose(Statement... stm) {
		for (Statement statement : stm) {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.err.println(e);
				}
			}
		}
	}

	private void preparedStmClose(PreparedStatement... pStm) {
		for (PreparedStatement preparedStm : pStm) {
			if (preparedStm != null) {
				try {
					preparedStm.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.err.println(e);
				}
			}
		}
	}

	public enum Logins {

		TABLE(".logins", 0), ID("idLogin", 1), NAME("name", 2);
		private Logins(String colName, int colIndex) {
			this.name = colName;
			this.index = colIndex;
		}

		private String name;
		private int index;

		@Override
		public String toString() {
			return this.name;
		}
	}

	public enum Tests {

		TABLE(".tests", 0), ID("idTest", 1), NAME("name", 2);
		private Tests(String colName, int colIndex) {
			this.name = colName;
			this.index = colIndex;
		}

		private String name;
		private int index;

		@Override
		public String toString() {
			return this.name;
		}
	}

	public enum Results {
		TABLE(".results", 0), L_ID("loginId", 1), T_ID("testId", 2), DAT("dat", 3), MARK("mark", 4);
		private Results(String colName, int colIndex) {
			this.name = colName;
			this.index = colIndex;
		}

		private String name;
		private int index;

		@Override
		public String toString() {
			return this.name;
		}
	}

	public void getResults(List<Result> listResult, String WHERE, String orderOrGroup) throws SQLException {
		if (!WHERE.isEmpty()) {
			WHERE = " AND " + WHERE;
		}
		String results = "SELECT logins.name, tests.name,  dat, mark  FROM " + dbName + ".results," + dbName
				+ " .logins," + dbName + ".tests WHERE  loginId=idLogin AND testId=idTest" + WHERE + " " + orderOrGroup;
		final int loginsName = 1;
		final int testsName = 2;
		final int dat = 3;
		final int mark = 4;
		Statement stmResults = null;
		ResultSet rsResults = null;
		Result dbResult = null;
		try {
			stmResults = cn.createStatement();
			rsResults = stmResults.executeQuery(results);
			while (rsResults.next()) {
				dbResult = new Result();
				dbResult.setLogin(rsResults.getString(loginsName));
				dbResult.setTest(rsResults.getString(testsName));
				dbResult.setDate(rsResults.getDate(dat));
				dbResult.setMark(rsResults.getInt(mark));
				dbResult.setMarkType(markHandler);

				listResult.add(dbResult);
			}

		} finally {
			ResultSetClose(rsResults);
			StatementClose(stmResults);
		}
	}

	public List<String> meanValue(List<String> meanValue, int roundingBit) throws SQLException {

		Statement stmResult = null;
		ResultSet rsResult = null;
		String meanVal;
		final int NAME = 1;
		final String MEAN = "mean";
		final String sumMark = "SELECT logins.name ,  SUM(mark)/ Count(*) as mean   FROM " + dbName
				+ ".results ,results.logins WHERE loginId=idLogin GROUP BY loginId ORDER BY mean DESC";

		try {

			stmResult = cn.createStatement();
			rsResult = stmResult.executeQuery(sumMark);
			while (rsResult.next()) {

				meanVal = rsResult.getString(NAME) + ";";
				meanVal += AbstractMark.Round(rsResult.getDouble(MEAN) / markHandler.getMultiplier(), roundingBit);

				meanValue.add(meanVal);
			}

		} finally {
			ResultSetClose(rsResult);
			StatementClose(stmResult);
		}
		return meanValue;
	}

	public int getLastMonth() throws SQLException {

		final String LAST_MONTH = "SELECT max(Month(dat)) as maxMonth FROM " + dbName + ".results";
		Statement stDate = null;
		ResultSet rsDate = null;
		int max = 0;
		try {
			stDate = cn.createStatement();
			rsDate = stDate.executeQuery(LAST_MONTH);
			if (rsDate.next()) {
				max = rsDate.getInt("maxMonth");
			}
		} finally {
			ResultSetClose(rsDate);
			StatementClose(stDate);
		}
		return max;
	}

	public void createTables() throws SQLException {

		final String CREAT_DB = "CREATE DATABASE IF NOT EXISTS ";
		final String NOT_UNIQUE = ")";
		final String UNIQUE = " , UNIQUE KEY (loginId , testId , dat , mark ))";
		String add;
		if (!defaultDbName) {
			add = UNIQUE;
		} else {
			add = NOT_UNIQUE;
		}

		String createLogins = "create table IF NOT EXISTS " + dbName + ".logins "
				+ "(idLogin int AUTO_INCREMENT NOT NULL , " + "name varchar(32) NOT NULL, " + "PRIMARY KEY (idLogin))";
		String createTests = "create table IF NOT EXISTS " + dbName + ".tests "
				+ "(idTest int AUTO_INCREMENT NOT NULL , " + "name varchar(32) NOT NULL, " + "PRIMARY KEY (idTest))";
		String createResults = "create table IF NOT EXISTS " + dbName + ".results " + "(loginId int NOT NULL, "
				+ "testId int NOT NULL , " + "dat DATE NOT NULL, " + "mark int NOT NULL, "
				+ "FOREIGN KEY (loginId) REFERENCES " + dbName + ".logins (idLogin),  "
				+ "FOREIGN KEY (testId) REFERENCES " + dbName + ".tests (idTest) " + add;

		final String DELETE = "DROP TABLE IF  EXISTS  ";

		String deleteLogins = DELETE + dbName + ".logins ";
		String deleteTests = DELETE + dbName + ".tests ";
		String deleteResult = DELETE + dbName + ".results ";

		Statement stmt = null;
		try {
			stmt = cn.createStatement();
			stmt.executeUpdate(CREAT_DB + this.dbName);
			if (defaultDbName) {
				stmt.executeUpdate(deleteResult);
				stmt.executeUpdate(deleteLogins);
				stmt.executeUpdate(deleteTests);
			}

			stmt.executeUpdate(createLogins);
			stmt.executeUpdate(createTests);
			stmt.executeUpdate(createResults);

		} finally {
			StatementClose(stmt);
		}
	}

	private static String sqlWrap(StringBuilder sb, String insertion, String preSql) {
		final String WRAP = "\"";
		sb.delete(0, sb.length());
		sb.append(preSql);
		sb.append(WRAP);
		sb.append(insertion);
		sb.append(WRAP);

		return sb.toString();
	}

	private static int maxId(String sql, Connection cn) throws SQLException {
		Statement id = null;
		ResultSet rsId = null;
		int max = 0;
		final int ID = 1;
		try {
			id = cn.createStatement();
			rsId = id.executeQuery(sql);
			if (rsId.next()) {
				max = rsId.getInt(ID);
			}
		} finally {
			if (rsId != null) {
				rsId.close();
			}
			if (id != null) {
				id.close();
			}
		}
		return max;
	}

	public int getId(Integer counter, String name, ResultSet rs, Statement stm, String querySelect, PreparedStatement psUpdate, String queryInsert)
			throws SQLException {
		StringBuilder sb = new StringBuilder();
		System.out.println(counter);
		querySelect = sqlWrap(sb, name, querySelect);
		rs = stm.executeQuery(querySelect);
		int id = 0;
		final int ID = 1;

		if (rs.next()) {
			id = rs.getInt(ID);
		} else {
			System.out.println(name);
			psUpdate.setString(ID, name);
			psUpdate.executeUpdate();
			id = ++counter;
			
		}
		
		return id;
	}

	public void updateTables(ReadData data) throws SQLException {
		Result result = null;
		String loginName = null;
		String testName = null;
		String INSERT = "INSERT INTO ";
		// String INSERT_IGNORE = "INSERT IGNORE INTO ";
		// String INSERT;
		// if (!defaultDbName) {
		// INSERT = INSERT_IGNORE;
		// } else {
		// INSERT = INSERT;
		// }

		final String INSERT_LOGIN = INSERT + dbName + ".logins (name) VALUES (?)";
		final String INSERT_TEST = INSERT + dbName + ".tests (name) VALUES (?)";
		final String INSERT_RESULT = INSERT + dbName + ".results (loginId, testId, dat, mark) VALUES (?,?,?,?)";
		final String SELECT_LOGIN = "SELECT idLogin  FROM " + dbName + ".logins WHERE name=";
		final String SELECT_TEST = "SELECT idTest  FROM " + dbName + ".tests WHERE name=";
		final String maxIdLogin = "SELECT max(idLogin) FROM " + dbName + ".logins";
		final String maxIdTest = "SELECT max(idTest) FROM " + dbName + ".tests";

		PreparedStatement pstmteLogins = null;
		PreparedStatement pstmTests = null;
		PreparedStatement pstmResults = null;

		Statement stmLogin = null;
		Statement stmTest = null;
		ResultSet rsLogin = null;
		ResultSet rsTest = null;

		int counterLog = maxId(maxIdLogin, cn);
		//Integer counterTest = maxId(maxIdTest, cn);
		Integer counterTest =0;

		Integer idTest = 0;
		int idLog = 0;

		StringBuilder sb = null;

		try {
			stmLogin = cn.createStatement();
			stmTest = cn.createStatement();

			pstmteLogins = cn.prepareStatement(INSERT_LOGIN);
			pstmTests = cn.prepareStatement(INSERT_TEST);
			pstmResults = cn.prepareStatement(INSERT_RESULT);
			sb = new StringBuilder();
			final int ID = 1;
			while (data.hasNext()) {
				result = data.next();
				loginName = result.getLogin();
				String login = sqlWrap(sb, loginName, SELECT_LOGIN);

				rsLogin = stmLogin.executeQuery(login);
				if (rsLogin.next()) {
					idLog = rsLogin.getInt(ID);
				} else {

					pstmteLogins.setString(ID, loginName);
					pstmteLogins.executeUpdate();
					idLog = ++counterLog;

				}
				testName = result.getTest();
				String getTest = sqlWrap(sb, testName, SELECT_TEST);
				rsTest = stmTest.executeQuery(getTest);
				if (rsTest.next()) {
					idTest = rsTest.getInt(ID);
				} else {

					pstmTests.setString(1, testName);
					pstmTests.executeUpdate();
					idTest = ++counterTest;
				}
				//idTest = getId(counterTest, testName, rsTest, stmTest, SELECT_TEST, pstmTests, INSERT_TEST);
				pstmResults.setInt(Results.L_ID.index, idLog);
				pstmResults.setInt(Results.T_ID.index, idTest);
				pstmResults.setDate(Results.DAT.index, result.getDate());
				pstmResults.setInt(Results.MARK.index, result.getMark());
				pstmResults.executeUpdate();
			}
		} finally {
			if (rsLogin != null) {
				rsLogin.close();
			}
			if (rsTest != null) {
				rsTest.close();
			}
			if (pstmResults != null) {
				pstmResults.close();
			}
			if (pstmTests != null) {
				pstmTests.close();
			}
			if (pstmteLogins != null) {
				pstmteLogins.close();
			}
			if (stmTest != null) {
				stmTest.close();
			}
			if (stmLogin != null) {
				stmLogin.close();
			}
			sb = null;
		}

	}

}
