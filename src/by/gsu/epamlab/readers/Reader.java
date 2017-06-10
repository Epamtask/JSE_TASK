package by.gsu.epamlab.readers;

import java.io.IOException;
import org.xml.sax.SAXException;
import by.gsu.epamlab.constants.Errors;
import by.gsu.epamlab.data.DataBaseProperties;
import by.gsu.epamlab.interfaces.ReadData;


public class Reader {
	private enum Source{
		XML,CSV				
	}
	
	static private String getTypeSource(String str) {
		final int afterPoint = 1;

		return str.substring(str.lastIndexOf('.') + afterPoint, str.length());
	}

	static public ReadData read(DataBaseProperties prop) throws SAXException, IOException {
		Source source = null;
		try{
		 source = Source.valueOf(getTypeSource(prop.getSource()).toUpperCase());
		} catch(IllegalArgumentException e) {
			throw new IOException(Errors.WRONG_DATA + prop.getSource());			
		}
		ReadData reader = null;
		switch (source) {
		case XML: {
			reader = new XmlReader(prop.getSource(), prop.getMarkType());
		}
			break;
		case CSV: {
			reader = new CsvReader(prop.getSource(), prop.getMarkType());
		}
			break;
		}
		return reader;
	}

}
