package com.moviefinder.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import com.moviefinder.exception.KeyAlreadyPresentException;

/**
 * Trie implementation of String. For searching a String from the bunch of strings stored in 
 * trie tree.
 * @author pankaj.chaswal
 *
 */
public final class TrieImpl implements Trie<String>{

	/**
	 * Empty root node
	 */
	private TrieNode<String> root = new TrieNode<String>(null, null);
	
	private static TrieImpl instance = new TrieImpl();
	
	//private constructor
	private TrieImpl() {
		
	}
	/**
	 * Set of nodes which represents the matching node found during a key search
	 */
	private Set<TrieNode<String>> nodesFound = new LinkedHashSet<>();
	
	// Only once instance is kept per classloader
	public static TrieImpl getInstance() {
		return instance;
	}
	
	/**
	 * Method that inserts multiple String values in the trie
	 */
	@Override
	public void insertAll(List<String> values) throws KeyAlreadyPresentException{
		for (String value : values) {
			insert(value);
		}
	}
	
	/**
	 * Method that allows to set a single String value in the trie
	 * @throws UnsupportedEncodingException 
	 */
	@Override
	public void insert(String value) throws KeyAlreadyPresentException{
	    //value = new String(value.getBytes("UTF-8"), "UTF-8");
		if(Objects.isNull(value) || value.isEmpty()) {
			throw new IllegalArgumentException("Invalid value");
		}
		//StringUtils.stripAccents(value).toLowerCase();
		insert(value, value);
	}

	@Override
	public boolean delete(String key) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void clear() {
		instance = null;
		instance = new TrieImpl();
	}

	/**
     * Generates a list of all words in the corpus that begin with the given
     * prefix.
     * 
     * @param prefix
     *            the prefix to autocomplete
     * @return an sorted set of all words in the corpus that begin with the
     *         given prefix
     */
	@Override
	public Set<String> prefixSearch(String prefix) {
		Set<String> suggestions = new LinkedHashSet<String>();
        TrieNode<String> node = findNode(prefix);
        if (node == null) {
            return suggestions;
        }
        Iterator<String> iterator = new TrieIterator(node, prefix);
        while (iterator.hasNext()) {
            suggestions.add(iterator.next());
        }
        return suggestions;
	}

	/**
	 * It first checks if the String is already present in the trie. If it is present
	 * it throws {@link KeyAlreadyPresentException} else
	 * First it create an empty rootNode (this will be parent node of the whole tree)
	 * Then it starts creating and attaching childnodes.Single characters from the passed
	 * String is stored in further TrieNodes one after the other. 
	 * 
	 * Like shows in figure below
	 * 
	 * For Strings "Raj","Raje","Raja","Rajdeep","Rajasthan","Rajhans" the data in TrieNode will look like below:
	 *	|root
	 *	 |-r
	 *	  |--a
	 *	   |---j
	 *	    |----a
	 *	     |-----s
	 *	      |------t
	 *	       |-------h
	 *	        |--------a
	 *	         |---------n
	 *	    |----d
	 *	     |-----e
	 *	      |------e
	 *	       |-------p
	 *	    |----e
	 *	    |----h
	 *	     |-----a
	 *	      |------n
	 *	       |-------s
	 * 
     *
	 * 
	 * @param key
	 * @param value
	 */
	private void insert(String key, String value) {
		//if(!search(key).isEmpty()) {
			//throw new KeyAlreadyPresentException("String "+key+" already exists in the trie");
		//}
		TrieNode<String> currentNode = root;

		for (int i = 0; i < key.length(); ++i) {
			Character letter = key.charAt(i);
			if (currentNode.childs.get(letter) == null) {
				currentNode.childs.put(letter, new TrieNode<String>(letter, currentNode));
			}

			currentNode = currentNode.childs.get(letter);
		}
		currentNode.wordEnds = true;
		currentNode.key = value;
	}

	private void keepHistoryOfMatchingStrings(List<TrieNode<String>> matchingNodes, TrieNode<String> node) {
		matchingNodes.addAll(node.childs.values());
	}

	/**
	 * A recursive method to iterate the trie.
	 * @param character
	 * @param node
	 */
	private void checkNode(Character character, TrieNode<String> node) {
		List<TrieNode<String>> children = new ArrayList<>();
		List<TrieNode<String>> matchingNodes = new ArrayList<>();
		children.addAll(node.childs.values());
		TrieNode<String> presentNode = (TrieNode<String>)node.childs.get(character);
		if (presentNode != null) {
			matchingNodes.clear();
			keepHistoryOfMatchingStrings(matchingNodes, presentNode);
			nodesFound.add(presentNode);
			children.remove(presentNode);
		}

		for (TrieNode<String> child : children) {
			checkNode(character, child);
		}
		if (!matchingNodes.isEmpty()) {
			nodesFound.addAll(matchingNodes);
		}
	}

	/**
	 * The String(key) to be searched is broken down into characters and is
	 * iterated over one character after the other, and starting from root node
	 * every character is matched with the character in subsequent trie node.
	 * This search includes both prefix and well as multi-string search.
	 * 
	 */
	public Set<String> search(String key) {
		Set<String> foundNodes = new LinkedHashSet<>();
		if(Objects.isNull(key) || key.isEmpty()) {
			foundNodes.add(root.key);
			return foundNodes;
		}
		//key = StringUtils.stripAccents(key).toLowerCase();
		nodesFound.clear();
		nodesFound.add(root);
		int length = key.length();
		//Keep track of the nodes to be iterated over
		List<TrieNode<String>> currentNodes = new ArrayList<>();

		for (int i = 0; i < length; i++) {
			currentNodes.clear();
			currentNodes.addAll(nodesFound);
			nodesFound.clear();
			for (TrieNode<String> node : currentNodes) {
				checkNode(key.charAt(i), node);
			}
		}
		/*
		 * Iterate over the matching nodes and record the leaf value, which is the complete string
		 */
		for (TrieNode<String> node : nodesFound) {
			for (TrieNode<String> leaf : node.getChildrens()) {
				foundNodes.add((String) leaf.key);
			}
		}
		return foundNodes;
	}
	
	protected TrieNode<String> findNode(String key) {
		//key = StringUtils.stripAccents(key).toLowerCase();
        TrieNode<String> node = root;
        int len = key.length();
        for (int i = 0; i < len; i++) {
            Character c = key.charAt(i);
            node = node.childs.get(c);
            if (node == null) {
                return null;
            }
        }
        return node;
    }
	
	
	public static void main(String[] args) throws KeyAlreadyPresentException, UnsupportedEncodingException {
		TrieImpl trie = new TrieImpl();
		/*trie.insert("Pankaj");
		trie.insert("JakPank");
		trie.insert("RaPark");
		trie.insert("PACk");*/
		/*trie.insert("Raj");*/
		trie.insert("Raje");
		trie.insert("Raja");
		trie.insert("Rajdeep");
		trie.insert("Rajhans");
		trie.insert("Rajasthan");
		trie.insert("Chaswal");
		trie.insert("Карменсита");
		trie.insert("Carmencita - spanyol tánc");
		/*System.out.println(trie.search("pank"));
		System.out.println(trie.search("Parkaj"));
		System.out.println(trie.search("ank"));
		
		System.out.println(trie.search("asw"));
		System.out.println(trie.search("сит"));
		System.out.println(trie.search("Карм"));
		System.out.println(trie.search("c"));*/
		//System.out.println(trie.search("Carmencita - etyspanyol tánc"));
		System.out.println(trie.search("aj"));
		/*System.out.println(trie.search("Raje"));
		System.out.println(trie.prefixSearch("Raj"));
		System.out.println(trie.prefixSearch("aj"));*/
		trie.printTrie();
		// trie.find("xb");
	}

	public void printTrie() {
		printTrie(new Formatter(System.out), 0, root);
	}

	private void printTrie(Formatter f, int level, TrieNode<String> node) {
		for (int i = 0; i < level; i++) {
			f.format(" ");
		}
		f.format("|");
		for (int i = 0; i < level; i++) {
			f.format("-");
		}

		f.format("%s%n", node.character == null ? "root" : node.character);

		for (Entry<Character, TrieNode<String>> child : node.childs.entrySet()) {
			printTrie(f, level + 1, child.getValue());
		}
	}
	
	/**
	 * Represents a node of a trie tree {@link TrieImpl}
	 * This stores a character and the children in a HashMap
	 * For Strings "Raj","Raje","Raja","Rajdeep","Rajasthan","Rajhans" the data in TrieNode will look like below:
	 *	|root
	 *	 |-r
	 *	  |--a
	 *	   |---j
	 *	    |----a
	 *	     |-----s
	 *	      |------t
	 *	       |-------h
	 *	        |--------a
	 *	         |---------n
	 *	    |----d
	 *	     |-----e
	 *	      |------e
	 *	       |-------p
	 *	    |----e
	 *	    |----h
	 *	     |-----a
	 *	      |------n
	 *	       |-------s
	 * @author pankaj.chaswal
	 *
	 * @param <T>
	 */
	public class TrieNode<T> {
		
		/**
		 * Represents children of this node in the trie.
		 * Internal Structure of the trieNode, its a map which contains the Character of a word as key, and
		 * reference to next node as the value.The 
		 */
		protected Map<Character, TrieNode<T>> childs = new HashMap<>();
		protected int id;
		protected boolean wordEnds;
		protected String key;
		protected Character character;
		protected TrieNode<T> parent;

		public TrieNode(Character letter, TrieNode<T> parent) {
			id++;
			wordEnds = false;
			this.character = letter;
			this.parent = parent;
		}

		public List<TrieNode<T>> getChildrens() {
			List<TrieNode<T>> tmpList = new ArrayList<>();
			if (wordEnds) {
				tmpList.add(this);
				if(this.childs.isEmpty())
				return tmpList;
			}
			for (TrieNode<T> child : this.childs.values()) {
				tmpList.addAll(child.getChildrens());
			}
			return tmpList;
		}

		public boolean equals(Object o) {
			if (o.getClass() == this.getClass()) {
				@SuppressWarnings("unchecked")
				TrieNode<T> node = (TrieNode<T>) o;
				if (node.id == this.id) {
					return true;
				} 
			}
			return false;
		}
	}
	
	
	 /* Pre-order iterator */
    protected static final class TrieIterator implements Iterator<String> {

        private String next;
        private Deque<Iterator<Entry<Character, TrieNode<String>>>> q = new ArrayDeque<>();
        private StringBuilder sb = new StringBuilder();

        public TrieIterator(TrieNode<String> node, String prefix) {
            sb.append(prefix);
            q.push(node.childs.entrySet().iterator());
            if (node.wordEnds) {
                next = prefix;
            } else {
                findNext();
            }
        }

        private void findNext() {
            next = null;
            Iterator<Entry<Character, TrieNode<String>>> iterator = q.peek();
            while (iterator != null) {
                while (iterator.hasNext()) {
                    Entry<Character, TrieNode<String>> e = iterator.next();
                    char key = e.getKey();
                    sb.append(key);
                    TrieNode<String> node = e.getValue();
                    iterator = node.childs.entrySet().iterator();
                    q.push(iterator);
                    if (node.wordEnds) {
                        next = sb.toString();
                        return;
                    }
                }
                q.pop();
                int len = sb.length();
                if (len > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                iterator = q.peek();
            }
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public String next() {
            String ret = next;
            findNext();
            return ret;
        }

    }


}
