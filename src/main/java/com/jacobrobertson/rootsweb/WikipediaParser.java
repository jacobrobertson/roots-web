package com.jacobrobertson.rootsweb;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

public class WikipediaParser {

	public static void main(String[] args) throws Exception {
		List<Item> roots = downloadRoots();
		cleanRoots(roots, true);
		printWordsUsedTwice(roots);
	}
	
	public static List<Item> downloadRoots() throws Exception {
		String page = download();
		return parse(page);
	}
	
	public static List<Item> parse(String page) {
		
		Pattern tablePattern = Pattern.compile("\\| '''(.*?)''' \\|\\| (.*?) \\|\\| .*?\\|\\| .*? \\|\\| (.*)");
		Matcher matcher = tablePattern.matcher(page);
		
		List<Item> roots = new ArrayList<Item>();
		
		while (matcher.find()) {
			String words = matcher.group(3);
			
			Item root = new Item(matcher.group(1), matcher.group(2));
			roots.add(root);
//			String line = matcher.group();
//			System.out.println(words);// + " => " + line);
			String[] split = words.split(",");
			for (String word: split) {
				word = word.trim();
				if (word.startsWith("[")) {
					word = word.substring(2, word.length() - 2);
				}
				root.getRoots().add(word);
			}
		}
		return roots;
	}
	/*
	 * One of
	 * simple
	 * words [[with]] brackets
	 * words [[with|some]] more [[brackets like|this]]
	 */
	private static Pattern definitionPattern = Pattern.compile("\\[\\[(.*?)\\]\\]");
	public static void printSimpleRoots(List<Item> roots) throws Exception {
		cleanRoots(roots, false);
		Map<String, Item> existing = JsonDataMaker.downloadRootItems();
		
		for (Item root: roots) {
			String name = root.getName();
			name = cleanName(name);
			if (existing.containsKey(name)) {
				continue;
			}
			if (name != null) {
				System.out.println(name + "\t" + root.getDefinition());
			}
		}
	}
	public static void cleanRoots(List<Item> roots, boolean makeNamesNull) throws Exception {
		for (Item root: roots) {
			String name = root.getName();
			name = cleanName(name);
			String def = cleanDefinition(root.getDefinition());
			root.setDefinition(def);
			if (name != null || makeNamesNull) {
				root.setName(name);
			}
		}
	}
	private static String cleanDefinition(String def) {
		StringBuilder buf = new StringBuilder();
		Matcher m = definitionPattern.matcher(def);
		int last = 0;
		while (m.find()) {
			String sub = def.substring(last, m.start()).trim();
			buf.append(sub);
			buf.append(" ");
			last = m.end() + 1;
			String part = m.group(1);
			int pos = part.indexOf("|");
			if (pos > 0) {
				part = part.substring(pos + 1);
			}
			buf.append(part);
		}
		if (last < def.length()) {
			String sub = def.substring(last);
			buf.append(sub);
		}
		return buf.toString().trim();
	}
	private static String cleanName(String name) {
		if (name.startsWith("-")) {
			name = name.substring(1);
		}
		if (name.endsWith("-")) {
			name = name.substring(0, name.length() - 1);
		}
		boolean nameOkay = true;
		for (int i = 0; i < name.length(); i++) {
			if (!Character.isLetter(name.charAt(i))) {
				nameOkay = false;
				break;
			}
		}
		if (nameOkay) {
			return name;
		} else {
			return null;
		}
	}
	public static void printWordsUsedTwice(List<Item> roots) throws Exception {
		Map<String, Item> existing = JsonDataMaker.downloadWordItems();
		Set<String> found = new HashSet<String>();
		List<String> strings = new ArrayList<String>();
		for (Item root: roots) {
			if (root.getName() == null) {
				continue;
			}
			Set<String> set = root.getRoots();
//			System.out.println(">>>> Start >>>> " + set);
			for (Item checkRoot: roots) {
				Set<String> checkSet = checkRoot.getRoots();
				if (set == checkSet) {
					continue;
				}
				Set<String> test = new HashSet<String>(checkSet);
				test.retainAll(set);
				if (!test.isEmpty()) {
					String testString = test.iterator().next();
					if (
							!found.contains(testString) 
							&& !existing.containsKey(testString)
							&& checkRoot.getName() != null
							&& root.getName() != null
							) {
						found.add(testString);
						strings.add(testString + "\t" + root.getName() + ", " + checkRoot.getName());
					}
				}
			}
		}
		Collections.sort(strings);
		for (String s: strings) {
			System.out.println(s);
		}
	}
	
	public static String download() throws Exception {
		String path = "https://en.wikipedia.org/w/index.php?title=List_of_Greek_and_Latin_roots_in_English&action=edit";
		URL u = new URL(path);
		InputStream in = u.openStream();
		String page = IOUtils.toString(in);
//		System.out.println(page);
		return page;
	}
	
}
