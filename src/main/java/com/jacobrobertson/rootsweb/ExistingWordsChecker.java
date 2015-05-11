package com.jacobrobertson.rootsweb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Finds roots with few words
 * @author Jacob
 */
public class ExistingWordsChecker {

	public static void main(String[] args) throws Exception {
		checkExistingWords();
	}
	
	public static void checkExistingWords() throws Exception {
		Map<String, Set<String>> rootWords = new HashMap<String, Set<String>>();
		Map<String, Item> words = JsonDataMaker.downloadWordItems();
		for (String wordName: words.keySet()) {
			Item word = words.get(wordName);
			for (String root: word.getRoots()) {
				Set<String> oneRootWords = rootWords.get(root);
				if (oneRootWords == null) {
					oneRootWords = new HashSet<String>();
					rootWords.put(root, oneRootWords);
				}
				oneRootWords.add(wordName);
			}
		}
		List<String> lines = new ArrayList<String>();
		for (String root: rootWords.keySet()) {
			Set<String> oneRootWords = rootWords.get(root);
			String num = "" + oneRootWords.size();
			if (num.length() < 2) {
				num = "0" + num;
			}
			lines.add(num + " - " + root);
		}
		Collections.sort(lines);
		for (String line: lines) {
			System.out.println(line);
		}
	}
	
}
