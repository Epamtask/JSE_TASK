package by.gsu.epamlab.beans;

public class DecimalMark extends AbstractMark {
	
	public DecimalMark(int minValue, int maxValue) {
		super(minValue, maxValue);		
	}
	
	private static final int  MULTIPLIER=10;

	@Override
	public String markToString(int mark) {	
		return mark / MULTIPLIER + "." + mark % MULTIPLIER;
	}

	@Override
	public int getMark(String value) {
		double val = Double.parseDouble(value);
		IntervalCheck(val);
		ValueCheck(val, MULTIPLIER);
		
		return (int) (val * MULTIPLIER);
	}

	@Override
	public int getMultiplier() {
		return MULTIPLIER;
	}
		
}
