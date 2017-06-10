package by.gsu.epamlab.beans;

public class Mark extends AbstractMark{

	public Mark(int minValue, int maxValue) {
		super(minValue, maxValue);		
	}
	private static final int MULTIPLIER = 1;
	@Override
	public String markToString(int mark) {		
		return String.valueOf(mark);
	}

	@Override
	public int getMark(String value) {		
		int val=Integer.parseInt(value);
		IntervalCheck(val);
		return val;
	}

	@Override
	public int getMultiplier() {		
		return MULTIPLIER;
	}	
}
