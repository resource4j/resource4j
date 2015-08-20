package com.github.resource4j;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.junit.Test;

public abstract class MandatoryStringContracts extends MandatoryValueContracts {

	@Override
	protected abstract MandatoryString createMandatoryValue(
			String resolvedSource, ResourceKey key, String object);

	
	@Test
	public void testAsCalendarReturnsDateParsedInGivenFormat() {		
		Calendar calendar = new GregorianCalendar();	
		DateFormat format = new SimpleDateFormat("MMM d yyyy", Locale.US);
		String value = format.format(calendar.getTime()); 
		
		MandatoryString string = createMandatoryValue(anyResolvedSource(), anyKey(), value);
		Calendar actual = string.as(Calendar.class, format);
		
		assertEquals(calendar.get(Calendar.DATE), actual.get(Calendar.DATE));
		assertEquals(calendar.get(Calendar.MONTH), actual.get(Calendar.MONTH));
		assertEquals(calendar.get(Calendar.YEAR), actual.get(Calendar.YEAR));
	}

}
