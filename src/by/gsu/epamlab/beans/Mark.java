package by.gsu.epamlab.beans;

public class Mark extends AbstractMark{

	public Mark(int minValue, int maxValue) {
		super(minValue, maxValue);
		// TODO Auto-generated constructor stub
	}
	private static final int MULTIPLIER = 1;
	@Override
	public String markToString(int mark) {
		// TODO Auto-generated method stub
		return String.valueOf(mark);
	}

	@Override
	public int getMark(String value) {
		// TODO Auto-generated method stub
		int val=Integer.parseInt(value);
		IntervalCheck(val);
		return val;
	}

	@Override
	public int getMultiplier() {
		// TODO Auto-generated method stub
		return MULTIPLIER;
	}	
}
