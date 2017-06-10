import static by.gsu.epamlab.print.Print.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import org.xml.sax.SAXException;

import by.gsu.epamlab.constants.Errors;
import by.gsu.epamlab.constants.SqlString;
import by.gsu.epamlab.constants.Title;
import by.gsu.epamlab.constants.User;
import by.gsu.epamlab.beans.AbstractMark;
import by.gsu.epamlab.beans.Mark;
import by.gsu.epamlab.beans.Result;
import by.gsu.epamlab.data.ConnectionMySql;
import by.gsu.epamlab.data.DataBase;
import by.gsu.epamlab.data.DataBaseProperties;
import by.gsu.epamlab.interfaces.ReadData;
import by.gsu.epamlab.readers.Reader;

public class RunnerN1 {

	public static void main(String[] args) {

		AbstractMark mark=new Mark(1, 10);

		ReadData data = null;		
		ConnectionMySql connection = null;
		final String IN = "src/results.csv";
		try {

			DataBaseProperties properties = new DataBaseProperties(User.URL, User.LOGIN, User.PASSWORD, User.DB_NAME, IN, mark);
			data = Reader.read(properties);
			connection = ConnectionMySql.creatConnection(properties);
			
			DataBase db = new DataBase(properties, connection,properties.getMarkType());
			db.createTables();
			db.updateTables(data);
			LinkedList<String> meanValue = new LinkedList<String>();
			db.meanValue(meanValue, -2);
			printList(meanValue, Title.MEAN_VAL);
			List<Result> listResults = new LinkedList<Result>();
			int month = db.getLastMonth();
			db.getResults(listResults,SqlString.SQL_MONTH + month, SqlString.SQL_ORDER);
			printList(listResults,Title.RESULTS);
			resultsLatestDay(listResults,Title.LATEST);

		} catch (SQLException e) {
			System.err.println(Errors.BD_ERROR+e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (SAXException e) {
			System.err.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			System.err.println(e.getMessage()+Errors.CLASS_NOT_FOUND);
		} finally {
			if (connection != null) {
				connection.closeConnection();
			}
			if (data != null) {
				data.close();
			}
		}
	}
}
