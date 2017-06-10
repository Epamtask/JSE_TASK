package by.gsu.epamlab.constants;

public class Constants {
	public final static String DELIMETER = ";";
	public final static String CLASSNAME = "org.gjt.mm.mysql.Driver";
	public final static String URL_QUERY = "jdbc:mysql://localhost/results";
	public final static String USER = "jse";
	public final static String PASSWORD = "jse";
	public static final String DELETE_RESULTS = "DELETE FROM results";
	public static final String DELETE_LOGINS = "DELETE FROM logins";
	public static final String DELETE_TESTS = "DELETE FROM tests";
	public final static String INSERT_DATA_INTO_LOGIN_TABLE = "INSERT INTO logins(name) VALUES (?)";
	public final static String INSERT_DATA_INTO_TESTS_TABLE = "INSERT INTO tests(name) VALUES (?)";
	public final static String SELECT_LOGIN_BY_ID = "SELECT * FROM logins WHERE name = ?";
	public final static String SELECT_TEST_BY_ID = "SELECT * FROM tests WHERE name = ?";
	public final static String INSERT_DATA_INTO_RESULTS_TABLE = "INSERT INTO results(loginId, testId, dat, mark) VALUES (?, ?, ?, ?)";
	public final static int LOGIN_IND = 1;
	public final static int TEST_IND_FOR_TEST_TABLE = 1;
	public final static int TEST_IND_FOR_RESULT_TABLE = 2;
	public final static int DATE_IND = 3;
	public final static int MARK_IND = 4;
	public final static String CHECK_LOGIN_QUERY = "SELECT count(*) as count, idLogin as id FROM Logins where name = ?";
	public final static String CHECK_TEST_QUERY = "SELECT count(*) as count, idTest as id FROM Tests where name = ?";
	public final static String INSERT_ID_LOGIN = "SELECT last_insert_id() as id FROM Logins ";
	public final static String INSERT_ID_TEST = "SELECT last_insert_id() as id FROM Tests ";
	public final static String LOAD_DATA_FROM_RESULTS_TABLE_FOR_CURRENT_MONTH = "select logins.name as 'login', tests.name  as 'test', dat, mark from results,"
	+ " logins, tests where loginId = idLogin and testId = idTest and month(dat) = month(curdate()) order by dat";
	public final static String GET_AVERAGE_FROM_RESULTS_TABLE = "SELECT loginId, loginId AS id, AVG(mark) AS avg FROM results GROUP BY loginId";
	public final static String SELECT_LOGIN_BY_IDLOGIN= "SELECT name AS name FROM logins where idLogin = ?";
	public final static String ID = "id";
	public final static String AVERAGE = "avg";
	public final static String NAME = "name";
	public final static String LOGIN = "login";
	public final static String MARK = "mark";
	public final static String DATE = "dat";
	public final static String TEST = "test";
	
}
