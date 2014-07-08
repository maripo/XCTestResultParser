package org.maripo.xctestresultparser.tests;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;
import org.maripo.xctestresultparser.exception.DateParserException;
import org.maripo.xctestresultparser.util.DateParser;

public class DateParserTests {

	static DateFormat dateFormatSec = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	/**
	 * Tests for relative time
	 */
	@Test
	public void testParseYearAgo() {
		try 
		{
			{
				Date resultDate = DateParser.parse("1 year ago");
				Calendar cal = getNowCalendar();
				cal.add(Calendar.YEAR, -1);
				assertEquals(dateFormatSec.format(cal.getTime()), dateFormatSec.format(resultDate));
			}
		} catch (DateParserException e) {
			e.printStackTrace();
			fail("Exception is unexpected!");
		}
	}
	@Test
	public void testParseYearsAgo() {
		try 
		{
			{
				Date resultDate = DateParser.parse("2 years ago");
				Calendar cal = getNowCalendar();
				cal.add(Calendar.YEAR, -2);
				assertEquals(dateFormatSec.format(cal.getTime()), dateFormatSec.format(resultDate));
			}
			{
				Date resultDate = DateParser.parse("12 years ago");
				Calendar cal = getNowCalendar();
				cal.add(Calendar.YEAR, -12);
				assertEquals(dateFormatSec.format(cal.getTime()), dateFormatSec.format(resultDate));
			}
		} catch (DateParserException e) {
			e.printStackTrace();
			fail("Exception is unexpected!");
		}
	
	}
	@Test
	public void testParseYearAgoWithSingleQuotes() {
		try 
		{
			{
				Date resultDate = DateParser.parse("'1 year ago'");
				Calendar cal = getNowCalendar();
				cal.add(Calendar.YEAR, -1);
				assertEquals(dateFormatSec.format(cal.getTime()), dateFormatSec.format(resultDate));
			}
			{
				Date resultDate = DateParser.parse("'2 years ago'");
				Calendar cal = getNowCalendar();
				cal.add(Calendar.YEAR, -2);
				assertEquals(dateFormatSec.format(cal.getTime()), dateFormatSec.format(resultDate));
			}
			{
				Date resultDate = DateParser.parse("'12 years ago'");
				Calendar cal = getNowCalendar();
				cal.add(Calendar.YEAR, -12);
				assertEquals(dateFormatSec.format(cal.getTime()), dateFormatSec.format(resultDate));
			}
		} catch (DateParserException e) {
			e.printStackTrace();
			fail();
		}
	
	}

	@Test
	public void testParseMonthsAgo() {
		fail("Not yet implemented");
	}

	@Test
	public void testParseDatesAgo() {
		fail("Not yet implemented");
	}

	@Test
	public void testParseHoursAgo() {
		fail("Not yet implemented");
	}

	@Test
	public void testParseMinutesAgo() {
		fail("Not yet implemented");
	}

	@Test
	public void testParseComplexAgo() {
		fail("Not yet implemented");
	}
	
	/**
	 * Test absolute dates
	 */
	@Test
	public void testAbsoluteDates() {
		fail("Not yet implemented");
	}
	
	
	private Calendar getNowCalendar () {
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		return cal;
	}

}
