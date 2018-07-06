package com.moviefinder.application;

import java.util.Set;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moviefinder.service.MovieService;
import com.moviefinder.util.SuggestionWrapper;

@Controller
@RequestMapping("/moviefinder")
public class MovieFinderController {

	@Inject
	MovieService movieService;

	@GetMapping("/")
	public String autocomplete(Model model) {
		model.addAttribute("title", "MovieFinder");
		return "moviefinder";
	}

	@RequestMapping(value = "/suggestion", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public SuggestionWrapper getMovieSuggestions(@RequestParam("key") String key) {
		Set<String> suggestions = movieService.getData(key);
		SuggestionWrapper sw = new SuggestionWrapper();
		sw.setSuggestions(suggestions);
		return sw;
		//return new ResponseEntity<>(suggestions, HttpStatus.OK);
	}

}
