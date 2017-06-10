package by.gsu.epamlab.readers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.security.InvalidParameterException;
import java.util.Scanner;

import by.gsu.epamlab.beans.AbstractMark;
import by.gsu.epamlab.beans.Result;
import by.gsu.epamlab.interfaces.ReadData;

public class CsvReader implements ReadData {

	public CsvReader(String input, AbstractMark mark) throws FileNotFoundException {
		super();
		this.mark = mark;
		scr = new Scanner(new FileReader(input));
		if (scr.hasNext()) {
			csvResult = nextCsv();		
		}
	}
	private final AbstractMark mark;
	private Result csvResult;
	private static final String SEPARATOR = ";";
	private final static int LOGIN = 0;
	private final static int TEST = 1;
	private final static int DATE = 2;
	private final static int MARK = 3;

	Scanner scr = null;

	@Override
	public Result next() {
		Result temp = csvResult;
		csvResult = nextCsv();
		return temp;
	}

	private Result nextCsv() {
		Result resultNext = null;
		if (scr.hasNext()) {
			String csv = scr.next();
			String[] values = csv.split(SEPARATOR);
			try {
				resultNext = new Result(values[LOGIN], values[TEST], values[DATE], values[MARK], mark);
			} catch (InvalidParameterException e) {
				System.err.println(e.getMessage() + " " + csv);
				if (scr.hasNext())
					resultNext=nextCsv();
			}
		}

		return resultNext;
	}

	@Override
	public boolean hasNext() {		
		return  csvResult != null;		
	}

	@Override
	public void close() {
		if (scr != null) {
			scr.close();
		}

	}

}
