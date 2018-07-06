package com.moviefinder.application;

import javax.inject.Inject;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.moviefinder.service.MovieService;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

	@Inject
	MovieService movieService;
	
	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {
		movieService.loadData();
	}


}
