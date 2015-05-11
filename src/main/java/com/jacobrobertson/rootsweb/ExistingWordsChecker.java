package com.jacobrobertson.rootsweb;

import java.util.Map;

/**
 * Finds roots with few words
 * @author Jacob
 */
public class ExistingWordsChecker {

	public static void main(String[] args) throws Exception {
		checkExistingWords();
	}
	
	public static void checkExistingWords() throws Exception {
		Map<String, Item> words = JsonDataMaker.downloadWordItems();
		Map<String, Item> roots = JsonDataMaker.downloadRootItems();
	}
	
}
