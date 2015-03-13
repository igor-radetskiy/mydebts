package iradetskyi.app.debt.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Utils {
	
	public static long[] toLongArray(Collection<Long> list) {
		long[] retArray = new long[list.size()];
		int i = 0;
		for (long item : list) {
			retArray[i] = item;
			i++;
		}
		return retArray;
	}
	
	public static List<Long> toLongList(long[] array) {
		List<Long> retList = new ArrayList<Long>(array.length);
		for(int i = 0; i < array.length; i++) {
			retList.add(array[i]);
		}
		return retList;
	}
}
