package com.moviefinder.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Iterables;
import com.moviefinder.exception.KeyAlreadyPresentException;

public class TrieImplTest {

	TrieImpl trie;

	@Before
	public void createTree() {
		trie = TrieImpl.getInstance();
	}

	@Test
	public void testSearchWhenSomeOverlapExists() {
		trie.clear();
		trie.insert("abcd");
		trie.insert("abce");

		assertEquals(1, trie.search("abe").size());
		assertEquals(1, trie.search("abd").size());

		assertTrue(trie.search("abe").stream().anyMatch(e -> e.equals("abce")));
		assertFalse(trie.search("abe").stream().anyMatch(e -> e.equals("abcd")));
	}

	@Test
	public void testSearchFullOverlapExists() {
		trie.clear();
		trie.insert("abcd");
		trie.insert("abce");

		assertEquals("abcd", Iterables.getOnlyElement(trie.search("abcd")));
		assertEquals("abce", Iterables.getOnlyElement(trie.search("abce")));

	}
	
	@Test(expected=IllegalArgumentException.class)
    public void testInsertNullValue() {
        trie.insert(null);
    }
	
	@Test(expected=IllegalArgumentException.class)
    public void testInsertEmptyValue() {
        trie.insert("");
    }
	
	@Test
    public void testFindAllMatch() {
		trie.clear();
        trie.insert("Raj");
		trie.insert("Raje");
		trie.insert("Raja");
		trie.insert("Rajdeep");
		trie.insert("Rajasthan");
		trie.insert("Rajhans");
		assertEquals(6, trie.search("aj").size());
    }
	
	@Test
    public void testFindNoneMatch() {
		trie.clear();
        trie.insert("Raj");
		trie.insert("Raje");
		trie.insert("Raja");
		trie.insert("Rajdeep");
		trie.insert("Rajasthan");
		trie.insert("Rajhans");
		assertEquals(0, trie.search("xo").size());
    }
	
	@Test
    public void testFindSomeMatch() {
		trie.clear();
        trie.insert("Raj");
		trie.insert("Raje");
		trie.insert("Raja");
		trie.insert("Rajdeep");
		trie.insert("Rajasthan");
		trie.insert("Rajhans");
		assertEquals(2, trie.search("an").size());
    }
	
	@Test
    public void testFindRootNode() {
        assertNull(Iterables.getOnlyElement(trie.search(null)));
    }
	
	@Test
    public void testFindEmptySTring() {
		assertNull(Iterables.getOnlyElement(trie.search("")));
    }
}
