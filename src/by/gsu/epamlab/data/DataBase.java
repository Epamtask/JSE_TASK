package by.gsu.epamlab.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import by.gsu.epamlab.beans.AbstractMark;
import by.gsu.epamlab.beans.Result;
import by.gsu.epamlab.constants.Constants;
import by.gsu.epamlab.interfaces.ReadData;
import by.gsu.epamlab.data.ConnectionMySql;

public class DataBase {

	private Connection cn;	
	private final AbstractMark markHandler;	

	public DataBase(DataBaseProperties properties, ConnectionMySql cnMySql, AbstractMark mark) {
		super();		
		this.cn = cnMySql.getConnection();
		this.markHandler = mark;		
	}

	private void closeResultSets(ResultSet... rs) {
		for (ResultSet resultSet : rs) {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {					
					System.err.println(e);
				}
			}
		}
	}

	private void closeStatements(Statement... stm) {
		for (Statement statement : stm) {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					System.err.println(e);
				}
			}
		}
	}

	private void closePreparedStatements(PreparedStatement... pStm) {
		for (PreparedStatement preparedStm : pStm) {
			if (preparedStm != null) {
				try {
					preparedStm.close();
				} catch (SQLException e) {					
					System.err.println(e);
				}
			}
		}
	}

	public void getResults(List<Result> listResult, String WHERE, String orderOrGroup) throws SQLException {
		if (!WHERE.isEmpty()) {
			WHERE = " AND " + WHERE;
		}
		String results = "SELECT logins.name, tests.name,  dat, mark  FROM results, logins, tests WHERE loginId = idLogin AND testId = idTest" + WHERE + " " + orderOrGroup;		
		Statement stmResults = null;
		ResultSet rsResults = null;
		Result dbResult = null;
		try {
			stmResults = cn.createStatement();
			rsResults = stmResults.executeQuery(results);
			while (rsResults.next()) {
				dbResult = new Result();
				dbResult.setLogin(rsResults.getString(Constants.LOGIN_INDEX));
				dbResult.setTest(rsResults.getString(Constants.TEST_INDEX));
				dbResult.setDate(rsResults.getDate(Constants.DATE_INDEX));
				dbResult.setMark(rsResults.getInt(Constants.MARK_INDEX));
				dbResult.setMarkType(markHandler);

				listResult.add(dbResult);
			}

		} finally {
			closeResultSets(rsResults);
			closeStatements(stmResults);
		}
	}

	public List<String> meanValue(List<String> meanValue, int roundingBit) throws SQLException {
		Statement stmResult = null;
		ResultSet rsResult = null;
		String meanVal;
		final int NAME_INDEX = 1;
		final String MEAN = "mean";
		final String GET_AVERAGE_FROM_RESULTS_TABLE = "SELECT logins.name, AVG(mark) as mean FROM results, results.logins WHERE loginId=idLogin GROUP BY loginId";
		
		try {
			stmResult = cn.createStatement();
			rsResult = stmResult.executeQuery(GET_AVERAGE_FROM_RESULTS_TABLE);
			while (rsResult.next()) {
				meanVal = rsResult.getString(NAME_INDEX) + Constants.DELIMETER;
				meanVal += AbstractMark.Round(rsResult.getDouble(MEAN) / markHandler.getMultiplier(), roundingBit);								

				meanValue.add(meanVal);
			}
		} finally {
			closeResultSets(rsResult);
			closeStatements(stmResult);
		}
		return meanValue;
	}

	public int getLastMonth() throws SQLException {	
		final String LAST_MONTH = "SELECT max(Month(dat)) as maxMonth FROM results";
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
			closeResultSets(rsDate);
			closeStatements(stDate);
		}
		return max;
	}
	
	public void clearTable(String clearQuery) throws SQLException{
		Statement st = null;		
		try {
			st = cn.createStatement();
			st.executeUpdate(clearQuery);
		} finally {
			closeStatements(st);
		}
	}
	
	private int getId(String name, PreparedStatement psCheck, PreparedStatement psUpdate, Statement st, String lastId, ResultSet rs)
			throws SQLException {
		final int index = 1;
		int id = 0;		
			
		try {
			psCheck.setString(index, name);	
			rs = psCheck.executeQuery();			
			
			final int ID = 1;		

			if (rs.next()) {
				id = rs.getInt(ID);
			} else {				
				psUpdate.setString(ID, name);
				psUpdate.executeUpdate();	
				
				rs = st.executeQuery(lastId);
				
				if (rs.next()) {
					id = rs.getInt("id");
				}				
			}			
		} finally {
			closeResultSets(rs);
		}
		
		return id;
	}

	public void updateTables(ReadData data) throws SQLException {
		clearTable(Constants.DELETE_RESULTS);
		clearTable(Constants.DELETE_LOGINS);		
		clearTable(Constants.DELETE_TESTS);
		
		Result result = null;
		String loginName = null;
		String testName = null;
		String INSERT = "INSERT INTO ";
		
		final String INSERT_LOGIN = INSERT + "logins (name) VALUES (?)";
		final String INSERT_TEST = INSERT + "tests (name) VALUES (?)";
		final String INSERT_RESULT = INSERT + "results (loginId, testId, dat, mark) VALUES (?,?,?,?)";
		final String SELECT_LOGIN = "SELECT idLogin FROM logins WHERE name = ?";
		final String SELECT_TEST = "SELECT idTest FROM tests WHERE name = ?";		
		final String LAST_ID_LOGIN = "SELECT last_insert_id() as id FROM Logins ";
		final String LAST_ID_TEST = "SELECT last_insert_id() as id FROM Tests ";
		
		PreparedStatement pstmteLogins = null;
		PreparedStatement pstmTests = null;
		PreparedStatement pstmResults = null;
		PreparedStatement psCheckLogin = null;
		PreparedStatement psCheckTest = null;
		Statement stMaxId = null;		
		ResultSet rs = null;
				
		int idTest = 0;
		int idLogin = 0;		

		try {		
			pstmteLogins = cn.prepareStatement(INSERT_LOGIN);
			pstmTests = cn.prepareStatement(INSERT_TEST);
			
			pstmResults = cn.prepareStatement(INSERT_RESULT);	
			
			psCheckLogin = cn.prepareStatement(SELECT_LOGIN);
			psCheckTest = cn.prepareStatement(SELECT_TEST);
			
			stMaxId = cn.createStatement();							
																	
			while (data.hasNext()) {
				result = data.next();
				loginName = result.getLogin();								
				testName = result.getTest();													
			
				idTest = getId(testName, psCheckTest, pstmTests, stMaxId, LAST_ID_TEST, rs);
				idLogin = getId(loginName, psCheckLogin, pstmteLogins, stMaxId, LAST_ID_LOGIN, rs);
				
				pstmResults.setInt(Constants.LOGIN_INDEX, idLogin);
				pstmResults.setInt(Constants.TEST_INDEX, idTest);
				pstmResults.setDate(Constants.DATE_INDEX, result.getDate());
				pstmResults.setInt(Constants.MARK_INDEX, result.getMark());
				pstmResults.executeUpdate();
			}
		} finally {
			closePreparedStatements(psCheckLogin, psCheckTest, pstmResults, pstmteLogins, pstmTests);
			closeStatements(stMaxId);
			closeResultSets(rs);
		}
	}
}
