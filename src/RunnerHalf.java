import by.gsu.epamlab.beans.AbstractMark;
import by.gsu.epamlab.beans.HalfMark;

public class RunnerHalf {
	public static void main(String[] args) {
		
		AbstractMark mark = new HalfMark(0, 20);
		final String IN = "src/results.csv";
		RunnerLogic.execute(mark, IN);
	}
}
