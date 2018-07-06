package com.moviefinder.util;

import java.util.List;
import java.util.Set;

import com.moviefinder.exception.KeyAlreadyPresentException;

/**
 * This interface represent the operation of a trie. A trie is a specialized set data structure 
 * that is used to store a set of strings, it is a kind of search tree.no node in the tree stores 
 * the key associated with that node; instead, its position in the tree defines the key with which
 * it is associated. All the descendants of a node have a common prefix of the string associated with that node, 
 * and the root is associated with the empty string. Keys are associated with leaves
 * 
 * 
 * @author Pankaj Chaswal 
 */

public interface Trie<T> {
	
	/**
     * Insert a new item to the tree.
     * 
     * @param value
     *            The value that need to be stored.
     * @throws KeyAlreadyPresentException
     */
    public void insert(T value) throws KeyAlreadyPresentException;
    
    
    /**
     * Insert multiple items to the tree.
     * 
     * @param value
     *            The collection of values to be stored
     * @throws KeyAlreadyPresentException
     */
    public void insertAll(List<T> value) throws KeyAlreadyPresentException;
    
    /**
     * Delete a key and its associated value from the tree.
     * @param key The key of the node that need to be deleted
     * @return
     */
    public boolean delete(T key);
    
    /**
     * Search for all the keys that start with given prefix.
     * 
     * @param prefix The prefix for which keys need to be search
     * @return The set of values those key start with the given prefix
     */
    public Set<T> prefixSearch(T prefix);
    
    
    /**
     * Search for all the keys that start or contains with given key. 
     * 
     * @param key The key for which keys need to be search
     * @return The set of values those key start with the given prefix
     */
    public Set<T> search(T key);
    
    /**
     * Clears the trie.
     */
    public void clear();

}
