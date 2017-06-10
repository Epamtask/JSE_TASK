package by.gsu.epamlab.interfaces;

import by.gsu.epamlab.beans.Result;

public interface ReadData {
	Result next();
	boolean hasNext();
	void close();
		
}
