package org.maripo.xctestresultparser.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
	public void testParseMonthAgo() {
		try 
		{
			{
				Date resultDate = DateParser.parse("1 month ago");
				Calendar cal = getNowCalendar();
				cal.add(Calendar.MONTH, -1);
				assertEquals(dateFormatSec.format(cal.getTime()), dateFormatSec.format(resultDate));
			}
			{
				Date resultDate = DateParser.parse("'1 month ago'");
				Calendar cal = getNowCalendar();
				cal.add(Calendar.MONTH, -1);
				assertEquals(dateFormatSec.format(cal.getTime()), dateFormatSec.format(resultDate));
			}
		} catch (DateParserException e) {
			e.printStackTrace();
			fail("Exception is unexpected!");
		}
	}
	@Test
	public void testParseMonthsAgo() {
		try 
		{
			{
				Date resultDate = DateParser.parse("2 months ago");
				Calendar cal = getNowCalendar();
				cal.add(Calendar.MONTH, -2);
				assertEquals(dateFormatSec.format(cal.getTime()), dateFormatSec.format(resultDate));
			}
			{
				Date resultDate = DateParser.parse("12 months ago");
				Calendar cal = getNowCalendar();
				cal.add(Calendar.MONTH, -12);
				assertEquals(dateFormatSec.format(cal.getTime()), dateFormatSec.format(resultDate));
			}
		} catch (DateParserException e) {
			e.printStackTrace();
			fail("Exception is unexpected!");
		}
	}

	@Test
	public void testParseDateAgo() {
		try 
		{
			{
				Date resultDate = DateParser.parse("1 date ago");
				Calendar cal = getNowCalendar();
				cal.add(Calendar.DATE, -1);
				assertEquals(dateFormatSec.format(cal.getTime()), dateFormatSec.format(resultDate));
			}
			{
				Date resultDate = DateParser.parse("1 day ago");
				Calendar cal = getNowCalendar();
				cal.add(Calendar.DATE, -1);
				assertEquals(dateFormatSec.format(cal.getTime()), dateFormatSec.format(resultDate));
			}
		} catch (DateParserException e) {
			e.printStackTrace();
			fail("Exception is unexpected!");
		}
	}

	@Test
	public void testParseDatesAgo() {
		try 
		{
			{
				Date resultDate = DateParser.parse("2 dates ago");
				Calendar cal = getNowCalendar();
				cal.add(Calendar.DATE, -2);
				assertEquals(dateFormatSec.format(cal.getTime()), dateFormatSec.format(resultDate));
			}
			{
				Date resultDate = DateParser.parse("12 dates ago");
				Calendar cal = getNowCalendar();
				cal.add(Calendar.DATE, -12);
				assertEquals(dateFormatSec.format(cal.getTime()), dateFormatSec.format(resultDate));
			}
			{
				Date resultDate = DateParser.parse("2 days ago");
				Calendar cal = getNowCalendar();
				cal.add(Calendar.DATE, -2);
				assertEquals(dateFormatSec.format(cal.getTime()), dateFormatSec.format(resultDate));
			}
			{
				Date resultDate = DateParser.parse("12 days ago");
				Calendar cal = getNowCalendar();
				cal.add(Calendar.DATE, -12);
				assertEquals(dateFormatSec.format(cal.getTime()), dateFormatSec.format(resultDate));
			}
		} catch (DateParserException e) {
			e.printStackTrace();
			fail("Exception is unexpected!");
		}
	}

	@Test
	public void testParseHourAgo() {
		try 
		{
			{
				Date resultDate = DateParser.parse("1 hour ago");
				Calendar cal = getNowCalendar();
				cal.add(Calendar.HOUR, -1);
				assertEquals(dateFormatSec.format(cal.getTime()), dateFormatSec.format(resultDate));
			}
		} catch (DateParserException e) {
			e.printStackTrace();
			fail("Exception is unexpected!");
		}
	}
	@Test
	public void testParseHoursAgo() {
		try 
		{
			{
				Date resultDate = DateParser.parse("2 hours ago");
				Calendar cal = getNowCalendar();
				cal.add(Calendar.HOUR, -2);
				assertEquals(dateFormatSec.format(cal.getTime()), dateFormatSec.format(resultDate));
			}
			{
				Date resultDate = DateParser.parse("12 hours ago");
				Calendar cal = getNowCalendar();
				cal.add(Calendar.HOUR, -12);
				assertEquals(dateFormatSec.format(cal.getTime()), dateFormatSec.format(resultDate));
			}
		} catch (DateParserException e) {
			e.printStackTrace();
			fail("Exception is unexpected!");
		}
	}
	@Test
	public void testParseMinuteAgo() {
		try 
		{
			{
				Date resultDate = DateParser.parse("1 minute ago");
				Calendar cal = getNowCalendar();
				cal.add(Calendar.MINUTE, -1);
				assertEquals(dateFormatSec.format(cal.getTime()), dateFormatSec.format(resultDate));
			}
			{
				Date resultDate = DateParser.parse("1 min ago");
				Calendar cal = getNowCalendar();
				cal.add(Calendar.MINUTE, -1);
				assertEquals(dateFormatSec.format(cal.getTime()), dateFormatSec.format(resultDate));
			}
		} catch (DateParserException e) {
			e.printStackTrace();
			fail("Exception is unexpected!");
		}
	}

	@Test
	public void testParseMinutesAgo() {
		try 
		{
			{
				Date resultDate = DateParser.parse("12 minutes ago");
				Calendar cal = getNowCalendar();
				cal.add(Calendar.MINUTE, -12);
				assertEquals(dateFormatSec.format(cal.getTime()), dateFormatSec.format(resultDate));
			}
			{
				Date resultDate = DateParser.parse("12 mins ago");
				Calendar cal = getNowCalendar();
				cal.add(Calendar.MINUTE, -12);
				assertEquals(dateFormatSec.format(cal.getTime()), dateFormatSec.format(resultDate));
			}
		} catch (DateParserException e) {
			e.printStackTrace();
			fail("Exception is unexpected!");
		}
	}

	@Test
	public void testParseComplexAgo() {
		try 
		{
			{
				Date resultDate = DateParser.parse("2 years 10 months ago");
				Calendar cal = getNowCalendar();
				cal.add(Calendar.YEAR, -2);
				cal.add(Calendar.MONTH, -10);
				assertEquals(dateFormatSec.format(cal.getTime()), dateFormatSec.format(resultDate));
			}
		} catch (DateParserException e) {
			e.printStackTrace();
			fail("Exception is unexpected!");
		}
	}

	@Test
	public void testParseIllegalAgo() {
		try 
		{
			{
				Date resultDate = DateParser.parse("foo ago");
				fail("Exception is expected!");
			}
		} catch (DateParserException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test absolute dates
	 */
	@Test
	public void testAbsoluteDates_yyyyMMdd() {
		fail("Not yet implemented");
	}
	@Test
	public void testAbsoluteDates() {
		//http://www.w3.org/TR/NOTE-datetime
		fail("Create a list!");
	}
	
	
	private Calendar getNowCalendar () {
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		return cal;
	}

}
