package com.moviefinder.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moviefinder.model.MovieData;
import com.moviefinder.model.MovieResponseWrapper;
import com.moviefinder.model.SuggestionWrapper;
import com.moviefinder.service.MovieService;
import com.moviefinder.util.JqgridResponse;

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
		Set<String> suggestions = movieService.getSuggestions(key);
		SuggestionWrapper suggestionWrapper = SuggestionWrapper.builder().suggestions(suggestions).build();
		suggestionWrapper.setSuggestions(suggestions);
		return suggestionWrapper;
	}
	
	@RequestMapping(value = "/movie", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public JqgridResponse<MovieResponseWrapper> getMovie(@RequestParam("key") String key) {
		MovieData movieData = movieService.getMovieData(key.toLowerCase());
		List<MovieResponseWrapper> movieResponseWrappers = new ArrayList<>();
		MovieResponseWrapper movieResponseWrapper = MovieResponseWrapper.builder()
				.id(String.valueOf(movieData.getId())).titleId(movieData.getTitleId())
				.ordering(String.valueOf(movieData.getOrdering()))
				.title(movieData.getTitle())
				.region(movieData.getRegion())
				.language(movieData.getLanguage())
				.types(movieData.getTypes())
				.attributes(movieData.getAttributes())
				.isOriginalTitle(movieData.getIsOriginalTitle()).build();
		movieResponseWrappers.add(movieResponseWrapper);
		JqgridResponse<MovieResponseWrapper> response = new JqgridResponse<MovieResponseWrapper>();
		response.setRow(movieResponseWrappers);
		return response;
	}

}
