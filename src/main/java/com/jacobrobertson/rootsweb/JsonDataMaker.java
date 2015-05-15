package com.jacobrobertson.rootsweb;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;

public class JsonDataMaker {

	public static void main(String[] args) throws Exception {
		createJsonFiles("./words.js");
	}

	public static Map<String, Item> downloadRootItems() throws Exception {
		String rpage = downloadRoots();
		Iterable<CSVRecord> lines = parseCsvFile(rpage);
		Map<String, Item> rootsMap = new HashMap<String, Item>();
		for (CSVRecord line : lines) {
			Item item = new Item(line.get(0).trim(), line.get(1).trim());
			if (rootsMap.containsKey(item.getName())) {
//				throw new IllegalArgumentException(item.getName() + " contained twice in the list of words");
				System.out.println(">>>>>>>>>>>> " + item.getName() + " contained twice in the list of words");
			}
			rootsMap.put(item.getName(), item);
		}
		// now link up the parents
		for (Item root: rootsMap.values()) {
			String def = root.getDefinition();
			while (def.startsWith(">")) {
				String parentRoot = def.substring(1);
				Item parent = rootsMap.get(parentRoot);
				if (parent == null) {
					System.out.println(">>>>>>>>>>>> " + root.getName() + " has an unknown parent " + def);
					break;
				} else {
					root.setParent(parent);
					def = parent.getDefinition();
					root = parent;
				}
			}
		}
		validateRoots(rootsMap.values());
		return rootsMap;
	}

	public static void validateRoots(Collection<Item> roots) {
		for (Item r1: roots) {
			String s1 = r1.getSimpleName();
			for (Item r2: roots) {
				if (r2 == r1) {
					continue;
				}
				String s2 = r2.getSimpleName();
				if (s1.equals(s2)) {
					String d1 = getComparableDef(r1.getDefinition());
					String d2 = getComparableDef(r2.getDefinition());
					if (d1.contains(d2) || d2.contains(d1)) {
						System.out.println(">>>> SIMILAR: " + s2 + " - " + d1 + " / " + d2);
					}
				}
			}
		}
	}
	private static String getComparableDef(String s) {
		String[] split = s.split(";|,");
		for (int i = 0; i < split.length; i++) {
			split[i] = split[i].trim().toUpperCase();
		}
		Arrays.sort(split);
		StringBuilder buf = new StringBuilder();
		for (String one: split) {
			buf.append(one);
		}
		return buf.toString();
	}
	
	public static Map<String, Item> downloadWordItems() throws Exception {
		String wpage = downloadWords();
		Iterable<CSVRecord> lines = parseCsvFile(wpage);
		Map<String, Item> wordsMap = new HashMap<String, Item>();
		for (CSVRecord line : lines) {
			Item item = new Item(line.get(0).trim(), line.get(2).trim());
			String[] roots = line.get(1).split(",");
			for (String root : roots) {
				item.getRoots().add(root.trim());
			}
			if (wordsMap.containsKey(item.getName())) {
				throw new IllegalArgumentException(item.getName() + " contained twice in the list of words");
			}
			wordsMap.put(item.getName(), item);
		}
		return wordsMap;
	}
	public static void createJsonFiles(String outFilePath) throws Exception {
		printItems(downloadRootItems(), downloadWordItems(), new PrintStream(new File(
				outFilePath
//				"C:\\Users\\Jacob\\git\\roots-web\\words.js"
				)));
	}
	/**
	 * Follows standard format 
	 * - if commas are in the field, put quotes around it.
	 * - if quotes are in the field, escape by doubling them up
	 * 
	 * Actual example - as seen in docs:
	 * cell 1 - "this", "line, is""jacked"up,,,
	 * cell 2 - "this", "line, is""jacked"up,,,
	 * 
	 * As downloaded as csv:
	 * """this"", ""line, is""""jacked""up,,,","""this"", ""line, is""""jacked""up,,,"
	 */
	public static Iterable<CSVRecord> parseCsvFile(String page) throws Exception {
		return CSVFormat.RFC4180.parse(new StringReader(page));
	}
	/**
	 *
		var words = {
			"pachycephalosaurus": { "rootNames": ["pach", "cephal", "saurus"], "definition": "thick headed lizard", "roots": {} },
		};
		var roots = {
			"octo": { "definition": "eight", "words": {} },
		};
	 */
	public static void printItems(Map<String, Item> rootsMap, Map<String, Item> wordsMap, PrintStream out) {
		List<String> rootKeys = new ArrayList<String>(rootsMap.keySet());
		Collections.sort(rootKeys);
		out.print("var roots = {\n");
		for (String name: rootKeys) {
			Item root = rootsMap.get(name);
			// we'll skip all roots with parents - we only need the parents
			if (root.getParent() == null) {
				out.print("\t\"");
				out.print(root.getName());
				out.print("\": { \"definition\": \"");
				out.print(root.getDefinition());
				out.print("\", \"words\": {} },\n");
			}
		}
		out.print("};\n\n");
		
		out.print("var words = {\n");
		List<String> wordKeys = new ArrayList<String>(wordsMap.keySet());
		Collections.sort(wordKeys);
		for (String name: wordKeys) {
			Item word = wordsMap.get(name);
			out.print("\t\"");
			out.print(word.getName());
			out.print("\": { \"rootNames\": [ ");
			List<String> roots = new ArrayList<String>(word.getRoots());
			Collections.sort(roots);
			for (String root: roots) {
				root = root.trim();
				Item rootItem = rootsMap.get(root);
				// validate the root exists
				if (rootItem == null) {
					// throw new IllegalArgumentException("Word: " + name + " has unknown root " + root);
//					System.out.println(">>>> Word: " + name + " has unknown root " + root);
					printUnkownRootMessage(rootsMap, word, root);
				}
				while (rootItem != null) {
					root = rootItem.getName();
					rootItem = rootItem.getParent();
				}
				out.print("\"");
				out.print(root);
				out.print("\", ");
			}
			out.print("], \"definition\": \"");
			out.print(word.getDefinition());
			out.print("\", \"roots\": {} },\n");
		}
		out.print("};\n");
	}
	public static void printUnkownRootMessage(Map<String, Item> rootsMap, Item word, String badRoot) {
		System.out.println(">>>> Word: " + word.getName() + "(" + word.getDefinition() + ") has unknown root " + badRoot);
		for (Item root: rootsMap.values()) {
			String sn = root.getSimpleName();
			if (sn.equals(badRoot)) {
				Item foundRoot = root;
				StringBuilder buf = new StringBuilder(">>>> ? Match ? >>> " + root.getName());
				while (root.getParent() != null) {
					buf.append(" > " + root.getParent().getName());
					root = root.getParent();
				}
				StringBuilder rootHelp = new StringBuilder();
				List<String> rootStrings = new ArrayList<String>(word.getRoots());
				Collections.sort(rootStrings);
				for (String rootString: rootStrings) {
					if (rootHelp.length() > 0) {
						rootHelp.append(", ");
					}
					if (rootString.equals(badRoot)) {
						rootHelp.append(foundRoot.getName());
					} else {
						rootHelp.append(rootString);
					}
				}
				System.out.println(buf.toString() + ": " + root.getDefinition() + " // replace with:" + rootHelp);
			}
		}
	}
	public static String downloadRoots() throws Exception {
		return downloadCsv("15bMwZUa5efnmJ1tE5YP5DeIpkV5di7ORJMlIGFH0Uvc", "1855861729");
	}
	public static String downloadWords() throws Exception {
		return downloadCsv("15bMwZUa5efnmJ1tE5YP5DeIpkV5di7ORJMlIGFH0Uvc", "0");
	}
	public static String downloadCsv(String docId, String gid) throws Exception {
		String path = "https://docs.google.com/spreadsheets/d/" + docId + "/export?format=csv&id=" + docId + "&gid=" + gid;
		URL u = new URL(path);
		InputStream in = u.openStream();
		String page = IOUtils.toString(in);
//		System.out.println(page);
		return page;
	}

}
