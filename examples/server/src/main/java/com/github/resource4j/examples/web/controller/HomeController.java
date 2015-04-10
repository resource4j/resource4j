package com.github.resource4j.examples.web.controller;

import static com.github.resource4j.resources.resolution.ResourceResolutionContext.in;

import java.util.Locale;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.resource4j.examples.domain.model.WeatherEnum;
import com.github.resource4j.examples.domain.service.Clock;
import com.github.resource4j.examples.domain.service.WeatherService;
import com.github.resource4j.resources.references.ResourceValueReference;
import com.github.resource4j.spring.AutowiredResource;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value="/")
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@AutowiredResource
	private int answer;
	
	@AutowiredResource
	private ResourceValueReference message;
	
	@Inject
	private Clock clock;
	
	@Inject
	private WeatherService weatherService;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 * @param locale resolved locale
	 * @param model view model
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		WeatherEnum weather = weatherService.getCurrentWeather();
		model.addAttribute("currentDate", clock.today());
		model.addAttribute("weather", weather);
		model.addAttribute("answer", answer);
		model.addAttribute("message", message.fetch(in(locale)).asIs());
		return "home";
	}
	
}
