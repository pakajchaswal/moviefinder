package com.moviefinder.util;

public interface Cache<K,V> {
	
	public void put(K k, V v);
	
	public V get(K k);
	
	public boolean contains(K k);

}
