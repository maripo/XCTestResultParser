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
	
	private static Pattern patternYearAgo = Pattern.compile("(\\d+) +years? +ago$");
	private static Date parseRelativeDate(String str) throws DateParserException {
		Matcher matcherYear = patternYearAgo.matcher(str);
		if (matcherYear.matches()) {
			int yearsGap = Integer.parseInt(matcherYear.group(1));
			Calendar cal = getNowCalendar();
			cal.add(Calendar.YEAR, -yearsGap);
			return cal.getTime();
		}
		throw new DateParserException();
	}
	private static Calendar getNowCalendar () {
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		return cal;
	}
}
