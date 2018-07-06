package com.moviefinder.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.moviefinder.model.MovieData;
import com.moviefinder.util.MovieDataCache;
import com.moviefinder.util.TrieImpl;

@Service
public class MovieService implements FinderService<String> {

	private static final Logger logger = LoggerFactory.getLogger(MovieService.class);
	private static final String _classpath = "classpath:";
	private static final String _resourcePath = "assets/data/moviedata.tsv";

	@Inject
	private ResourceLoader resourceLoader;

	public Set<String> getData(String key) {
		TrieImpl trie = TrieImpl.getInstance();
		return trie.search(key);
	}

	public void loadData() {
		Resource resource = resourceLoader.getResource(_classpath + _resourcePath);
		try(InputStream in = resource.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF8"));) {
			logger.info(":: Loading movie data ::");
			
			String line;
			/**
			 * Looping the read block until all lines in the file are read.
			 */
			int i = 0;
			MovieDataCache movieDataCache = MovieDataCache.getInstance();
			TrieImpl trie = TrieImpl.getInstance();
			while ((line = reader.readLine()) != null) {
				/**
				 * Splitting the content of tabbed separated line
				 */
				if (i > 0) {
					String datavalue[] = line.split("\t");
					String titleId = datavalue[0];
					int ordering = Integer.valueOf(datavalue[1]);
					String title = datavalue[2];
					String region = datavalue[3];
					String language = datavalue[4];
					String types = datavalue[5];
					String attributes = datavalue[6];
					String isOriginalTitle = datavalue[7];
					MovieData movieData = MovieData.builder().titleId(titleId).ordering(ordering).title(title).region(region).language(language)
							.types(types).attributes(attributes).isOriginalTitle(isOriginalTitle).build();
					if(!movieDataCache.contains(title)){
						trie.insert(title);
						movieDataCache.put(title, movieData);
					}
				} else {
					i++;
				}
			}
			logger.info(":: Completed loading movie data ::");
			//trie.printTrie();
		} catch (Throwable t) {
			logger.error(t.getMessage());
		} 
	}

}
