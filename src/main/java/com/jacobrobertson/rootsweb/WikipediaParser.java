package com.jacobrobertson.rootsweb;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

public class WikipediaParser {

	public static void main(String[] args) throws Exception {
		String page = download();
		parse(page);
	}
	
	public static void parse(String page) {
		
		Pattern tablePattern = Pattern.compile("\\| '''.*?''' \\|\\| .* \\|\\| .*?\\|\\| .*? \\|\\| (.*)");
		Matcher matcher = tablePattern.matcher(page);
		
		List<Set<String>> sets = new ArrayList<Set<String>>();
		
		while (matcher.find()) {
			String words = matcher.group(1);
//			String line = matcher.group();
//			System.out.println(words);// + " => " + line);
			Set<String> set = new HashSet<String>();
			String[] split = words.split(",");
			for (String word: split) {
				word = word.trim();
				if (word.startsWith("[")) {
					word = word.substring(2, word.length() - 2);
				}
				word = word.toUpperCase();
				set.add(word);
			}
			sets.add(set);
		}
		for (Set<String> set: sets) {
//			System.out.println(">>>> Start >>>> " + set);
			for (Set<String> checkSet: sets) {
				if (set == checkSet) {
					continue;
				}
				Set<String> test = new HashSet<String>(checkSet);
				test.retainAll(set);
				if (!test.isEmpty()) {
					System.out.println(test + " =====> " + checkSet);
				}
			}
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
