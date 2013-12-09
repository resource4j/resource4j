package com.github.resource4j.examples.domain.service.impl;

import javax.inject.Named;

import com.github.resource4j.examples.domain.model.WeatherEnum;
import com.github.resource4j.examples.domain.service.WeatherService;

@Named
public class WeatherServiceImpl implements WeatherService {

	@Override
	public WeatherEnum getCurrentWeather() {
		return null;
	}

}
