package iradetskyi.app.debt.utils;

import java.util.Calendar;

public class DateUtil {
	private static DateUtil sDateUtil = null;
	
	public int day;
	public int month;
	public int year;
	
	private DateUtil() {}
	
	public static DateUtil getCurrentDate() {
		if (sDateUtil == null) {
			sDateUtil = new DateUtil();
		}
		sDateUtil.updateCurrentDate();
		return sDateUtil;
	}
	
	public static DateUtil parse(String date) {
		if (sDateUtil == null) {
			sDateUtil = new DateUtil();
		}
		String[] dateStruct = date.split("/");
		sDateUtil.day = Integer.parseInt(dateStruct[0]);
		sDateUtil.month = Integer.parseInt(dateStruct[1]) - 1;
		sDateUtil.year = Integer.parseInt(dateStruct[2]);
		return sDateUtil;
	}
	
	public void updateCurrentDate() {
		Calendar calendar = Calendar.getInstance();
		sDateUtil.day = calendar.get(Calendar.DAY_OF_MONTH);
		sDateUtil.month = calendar.get(Calendar.MONTH);
		sDateUtil.year = calendar.get(Calendar.YEAR);
	}
	
	@Override
	public String toString() {
		return day + "/" + (month + 1) + "/" + year;
	}
}
