package com.moviefinder.service;

import java.util.Set;

import com.moviefinder.model.MovieData;

/**
 * Basic interface that exposes functionality of loading the provided data,
 * and then run find function of that data.
 * 
 * @author pankaj.chaswal
 *
 * @param <E>
 */
public interface FinderService<E> {
	
	/**
	 * Load provided data into a concrete data structure of 
	 * programmer's choice depending on the type of data, and
	 * the nature of problem.
	 */
	public void loadData();
	
	/**
	 * Returns the list of closely matching data. This could be empty list.
	 * or a list with a single or multiple element(s).
	 *
	 * @param e
	 * @return
	 */
	public Set<E> getSuggestions(E e);
	
	/**
	 * Returns the movie data matching the key
	 * @param e
	 * @return
	 */
	public MovieData getMovieData(E e);
	
}
