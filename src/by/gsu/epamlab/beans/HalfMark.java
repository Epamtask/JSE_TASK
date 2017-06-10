package by.gsu.epamlab.beans;

public class HalfMark extends AbstractMark {

	public HalfMark(int minValue, int maxValue) {
		super(minValue, maxValue);
		// TODO Auto-generated constructor stub
	}

	private static final int MULTIPLIER = 2;
	private static final String REMAINDER = ".5";

	@Override
	public int getMark(String value) {
		double val = Double.parseDouble(value);
		IntervalCheck(val);
		ValueCheck(val, MULTIPLIER);

		return (int) (val * MULTIPLIER);
	}

	@Override
	public String markToString(int mark) {
		// TODO Auto-generated method stub

		String result = String.valueOf(mark / 2);
		if (mark % 2 == 1) {
			result += REMAINDER;
		}
		return result;
	}

	@Override
	public int getMultiplier() {
		// TODO Auto-generated method stub
		return MULTIPLIER;
	}
}
