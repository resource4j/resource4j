package com.github.resource4j.examples.domain.service.impl;

import java.util.Random;

import javax.inject.Named;

import com.github.resource4j.examples.domain.model.WeatherEnum;
import com.github.resource4j.examples.domain.service.WeatherService;

@Named
public class WeatherServiceImpl implements WeatherService {

	@Override
	public WeatherEnum getCurrentWeather() {
		WeatherEnum[] values = WeatherEnum.values();
		return values[new Random().nextInt(values.length)];
	}

}
