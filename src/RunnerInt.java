import by.gsu.epamlab.beans.AbstractMark;
import by.gsu.epamlab.beans.Mark;

public class RunnerInt {

	public static void main(String[] args) {

		AbstractMark mark = new Mark(1, 10);
		final String IN = "src/results.csv";
		RunnerLogic.execute(mark, IN);

	}
}