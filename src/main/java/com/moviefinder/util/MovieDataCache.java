package com.moviefinder.util;

import java.util.HashMap;
import java.util.Map;

import com.moviefinder.model.MovieData;

public final class MovieDataCache implements Cache<String, MovieData> {

	private Map<String, MovieData> cache;

	private static MovieDataCache instance = new MovieDataCache();
	
	// Only once instance is kept per classloader
	public static MovieDataCache getInstance() {
		return instance;
	}

	// Private constructor to prevent instantiation
	private MovieDataCache() {
		cache = new HashMap<String, MovieData>();
	}

	@Override
	public void put(String key, MovieData movieData) {
		cache.put(key, movieData);
	}

	@Override
	public MovieData get(String key) {
		return cache.get(key);
	}
	
	public boolean contains(String key) {
		return cache.containsKey(key);
	}

}
