import by.gsu.epamlab.beans.AbstractMark;
import by.gsu.epamlab.beans.DecimalMark;

public class RunnerDecimal {
	public static void main(String[] args) {

		AbstractMark mark = new DecimalMark(1, 10);
		final String IN = "src/results.xml";
		RunnerLogic.execute(mark, IN);
	}	
}
