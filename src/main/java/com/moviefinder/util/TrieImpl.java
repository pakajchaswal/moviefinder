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
	public void insertAll(List<String> values){
		for (String value : values) {
			insert(value);
		}
	}
	
	/**
	 * Method that allows to set a single String value in the trie
	 * @throws UnsupportedEncodingException 
	 */
	@Override
	public void insert(String value){
	    //value = new String(value.getBytes("UTF-8"), "UTF-8");
		if(Objects.isNull(value) || value.isEmpty()) {
			throw new IllegalArgumentException("Invalid value");
		}
		insert(value, value);
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
		TrieNode<String> currentNode = root;
		for (int i = 0; i < key.length(); ++i) {
			Character letter = Character.toLowerCase(key.charAt(i));
			if (currentNode.childs.get(letter) == null) {
				currentNode.key = value.substring(0, i);
				if(letter.equals(' ')) {
					currentNode.wordEnds = true;
				}
				currentNode.childs.put(letter, new TrieNode<String>(letter, currentNode));
			}

			currentNode = currentNode.childs.get(letter);
		}
		currentNode.sentenceEnds = true;
		currentNode.key = value;
	}

	/**
	 * A recursive method to iterate the trie.
	 * @param character
	 * @param node
	 */
	private void checkNode(Character character, TrieNode<String> node, String key) {
		List<TrieNode<String>> children = new ArrayList<>();
		children.addAll(node.childs.values());
		TrieNode<String> presentNode = (TrieNode<String>)node.childs.get(character);
		if (presentNode != null) {
			if(Objects.nonNull(presentNode.key)){
				nodesFound.add(presentNode);
				children.remove(presentNode);
			}
		}
		for (TrieNode<String> child : children) {
			checkNode(character, child, key);
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
				checkNode(Character.toLowerCase(key.charAt(i)), node, key);
			}
		}
		/*
		 * Iterate over the matching nodes and record the leaf value, which is the complete string
		 */
		for (TrieNode<String> node : nodesFound) {
			for (TrieNode<String> leaf : node.getChildrens()) {
				String keySubstring = null;
				if(key.length()>2) {
					keySubstring = key.substring(0, key.length()-2);
				}else {
					keySubstring = key;
				}
				if(leaf.key.toLowerCase().startsWith(keySubstring.toLowerCase())) {
					foundNodes.add((String) leaf.key);
				}else if(leaf.key.toLowerCase().contains(key.toLowerCase())) {
					foundNodes.add((String) leaf.key);
				}
			}
		}
		return foundNodes;
	}
	
	protected TrieNode<String> findNode(String key) {
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
		protected boolean sentenceEnds;
		protected String key;
		protected Character character;
		protected TrieNode<T> parent;

		public TrieNode(Character letter, TrieNode<T> parent) {
			this.id++;
			this.wordEnds = false;
			this.sentenceEnds = false;
			this.character = letter;
			this.parent = parent;
		}

		public List<TrieNode<T>> getChildrens() {
			List<TrieNode<T>> tmpList = new ArrayList<>();
			if (sentenceEnds) {
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
            if (node.sentenceEnds) {
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
                    if (node.sentenceEnds) {
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
