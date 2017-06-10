package by.gsu.epamlab.print;

import java.sql.Date;
import java.util.List;
import java.util.ListIterator;
import by.gsu.epamlab.beans.Result;

public class Print {
	public static <T> void printList(List<T> list, String title) {
		System.out.println(title);
		for (T obj : list) {
			System.out.println(obj);
		}
		System.out.println();
	}

	public static void resultsLatestDay(List<Result> list, String title) {
		Result result = null;
		Date date = null;
		System.out.println(title);
		ListIterator<Result> iter = list.listIterator(list.size());
		if (iter.hasPrevious()) {
			result = iter.previous();
			date = result.getDate();
			System.out.println(result);

			while (iter.hasPrevious()) {
				result = iter.previous();
				if (date.equals(result.getDate())) {
					System.out.println(result);
				} else {
					break;
				}
			}
		}
	}
}
