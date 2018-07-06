package com.moviefinder.exception;

import com.moviefinder.util.TrieImpl;

/**
 * exception thrown if a duplicate key is inserted in a {@link TrieImpl}
 * 
 * @author Pankaj Chaswal
 */
public class KeyAlreadyPresentException extends RuntimeException
{
	private static final long serialVersionUID = 3141795907493885706L;

	public KeyAlreadyPresentException(String msg)
	{
		super(msg);
	}
}
