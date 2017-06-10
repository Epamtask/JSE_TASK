package by.gsu.epamlab.readers;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayDeque;
import java.util.Queue;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import by.gsu.epamlab.beans.AbstractMark;
import by.gsu.epamlab.beans.Result;
import by.gsu.epamlab.interfaces.ReadData;

public class XmlReader extends DefaultHandler implements ReadData {

	public XmlReader(String xmlName, AbstractMark mark) throws SAXException, IOException {
		super();
		this.mark=mark;
		XMLReader reader = XMLReaderFactory.createXMLReader();
		reader.setContentHandler(this);
		reader.parse(xmlName);

	}

	private static enum ResultEnum {
		RESULTS, STUDENT, LOGIN, TESTS, TEST;
	}
	private final AbstractMark mark;
	private static final String SEMICOLON=";";
	private Queue<Result> results = new ArrayDeque<Result>();
	private ResultEnum currentEnum;
	private String login;

	public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		currentEnum = ResultEnum.valueOf(localName.toUpperCase());
		Result student = null;
		if (currentEnum == ResultEnum.TEST) {
			final int TEST_INDEX = 0, DATE_INDEX = 1, MARK_INDEX = 2;
			try {
				student = new Result(login, attrs.getValue(TEST_INDEX), attrs.getValue(DATE_INDEX),
						attrs.getValue(MARK_INDEX),mark);
				results.add(student);
			} catch (InvalidParameterException e) {
				System.err.println(e.getMessage() +SEMICOLON+ login+SEMICOLON + attrs.getValue(TEST_INDEX)+SEMICOLON
			+ attrs.getValue(DATE_INDEX)+SEMICOLON
						+ attrs.getValue(MARK_INDEX));
			}
		}

	}

	public void characters(char[] ch, int start, int length) {
		if (currentEnum == ResultEnum.LOGIN) {
			String str = new String(ch, start, length).trim();
			if (!str.isEmpty()) {
				login = str;
			}
		}
	}

	@Override
	public Result next() {
		// TODO Auto-generated method stub
		return results.poll();
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return !results.isEmpty();
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		

	}
}
