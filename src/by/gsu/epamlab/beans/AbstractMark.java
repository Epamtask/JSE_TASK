package by.gsu.epamlab.beans;

import java.security.InvalidParameterException;
import by.gsu.epamlab.constants.Errors;

public abstract class AbstractMark {

	public AbstractMark(int minValue, int maxValue) {
		super();
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	protected final int minValue;
	protected final int maxValue;

	public abstract String markToString(int mark);
	public abstract int getMark(String value);
	public abstract int getMultiplier();
	

	public void IntervalCheck(int value) {

		if (value > maxValue || value < minValue) {
			throw new InvalidParameterException(Errors.WRONG + value);
		}
	}

	public void IntervalCheck(double value) {

		if (value > maxValue || value < minValue) {
			throw new InvalidParameterException(Errors.WRONG + value);
		}
	}
	
	public void ValueCheck(double value , int multiplier) {
		double integer=value*multiplier;
		
		if (integer!=(int)integer) {
			throw new InvalidParameterException(Errors.WRONG + value);
		}
	}
	public static double Round(double value, int roundingBit) {
		int precision = 1;

		for (int i = 0; i < Math.abs(roundingBit); i++) {
			precision *= 10;
		}

		if (roundingBit > 0) {
			value = Math.round(value / precision ) * precision;
		} else {
			value = Math.round(value * precision ) / (double) precision;
		}
		return value;
	}
}
