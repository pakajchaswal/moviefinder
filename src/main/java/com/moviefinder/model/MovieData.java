package com.moviefinder.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MovieData {

	private int id;
	private String titleId;
	private int ordering;
	private String title;
	private String region;
	private String language;
	private String types;
	private String attributes;
	private String isOriginalTitle;
	
}
