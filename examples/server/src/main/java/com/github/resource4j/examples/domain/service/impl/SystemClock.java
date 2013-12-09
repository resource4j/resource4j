package com.github.resource4j.examples.domain.service.impl;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.inject.Named;

import com.github.resource4j.examples.domain.service.Clock;

@Named
public class SystemClock implements Clock {

	@Override
	public Calendar now() {
		return new GregorianCalendar();
	}

	@Override
	public Calendar today() {
		Calendar date = new GregorianCalendar();
		date.set(Calendar.HOUR, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);
		return date;
	}

}
