package com.github.resource4j.generic;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.junit.Test;

import com.github.resource4j.ResourceKey;

public class GenericMandatoryStringTest {

	private static final String A_SOURCE = "";
	private static final ResourceKey THE_KEY = ResourceKey.key("example", "id");

	@Test
	public void testDateParsedInGivenFormat() {		
		Calendar calendar = new GregorianCalendar();	
		DateFormat format = new SimpleDateFormat("MMM d yyyy", Locale.US);
		String value = format.format(calendar.getTime()); 
		
		GenericMandatoryString string = new GenericMandatoryString(A_SOURCE, THE_KEY, value);
		Calendar actual = string.as(Calendar.class, format);
		
		assertEquals(calendar.get(Calendar.DATE), actual.get(Calendar.DATE));
		assertEquals(calendar.get(Calendar.MONTH), actual.get(Calendar.MONTH));
		assertEquals(calendar.get(Calendar.YEAR), actual.get(Calendar.YEAR));
	}
	
}
