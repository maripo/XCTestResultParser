package org.maripo.xctestresultparser.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.maripo.xctestresultparser.exception.DateParserException;

public class DateParser {
	
	public static Date parse (String str) throws DateParserException {
		if (str.startsWith("'") && str.endsWith("'")) {
			str = str.substring(1, str.length()-1);
		}
		str = str.toLowerCase();
		if (str.endsWith(" ago")) {
			return parseRelativeDate(str);
		} else {
			return parseAbsoluteDate(str);
		}
		
		//throw new DateParserException();
	}
	private static Date parseAbsoluteDate(String str) {
		// TODO Auto-generated method stub
		return null;
	}

	private static Pattern patternYearAgo = Pattern.compile(".* (\\d++) +years? .+");
	private static Pattern patternMonthAgo = Pattern.compile(".* (\\d++) +(mon|month)s? .+");
	private static Pattern patternDaysAgo = Pattern.compile(".* (\\d++) +(day|date)s? .+");
	private static Pattern patternHoursAgo = Pattern.compile(".* (\\d++) +(hour|hr)s? .+");
	private static Pattern patternMinutesAgo = Pattern.compile(".* (\\d++) +(minute|min)s? .+");
	
	private static Date parseRelativeDate(String str) throws DateParserException {
		str = " " + str;
		Calendar cal = getNowCalendar();
		boolean matched = false;
		matched |= applyRelativeExpressionToCalendar (cal, str, patternYearAgo, Calendar.YEAR);
		matched |= applyRelativeExpressionToCalendar (cal, str, patternMonthAgo, Calendar.MONTH);
		matched |= applyRelativeExpressionToCalendar (cal, str, patternDaysAgo, Calendar.DATE);
		matched |= applyRelativeExpressionToCalendar (cal, str, patternHoursAgo, Calendar.HOUR);
		matched |= applyRelativeExpressionToCalendar (cal, str, patternMinutesAgo, Calendar.MINUTE);
		if (matched) {
			return cal.getTime();
		}
		throw new DateParserException();
	}
	private static boolean applyRelativeExpressionToCalendar(Calendar cal, String str, Pattern pattern, int field) {
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			int gap = Integer.parseInt(matcher.group(1));
			cal.add(field, -gap);
			return true;
		}
		return false;
	}
	private static Calendar getNowCalendar () {
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		return cal;
	}
}
