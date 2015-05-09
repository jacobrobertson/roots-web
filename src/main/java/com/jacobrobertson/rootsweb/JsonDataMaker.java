package com.jacobrobertson.rootsweb;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
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

	public static void createJsonFiles(String outFilePath) throws Exception {
		String rpage = downloadRoots();
		Iterable<CSVRecord> lines = parseCsvFile(rpage);
		Map<String, Item> rootsMap = new HashMap<String, Item>();
		for (CSVRecord line : lines) {
			Item item = new Item(line.get(0), line.get(1));
			rootsMap.put(item.getName(), item);
		}

		String wpage = downloadWords();
		lines = parseCsvFile(wpage);
		Map<String, Item> wordsMap = new HashMap<String, Item>();
		for (CSVRecord line : lines) {
			Item item = new Item(line.get(0), line.get(2));
			String[] roots = line.get(1).split(",");
			for (String root : roots) {
				item.getRoots().add(root);
			}
			wordsMap.put(item.getName(), item);
		}
		printItems(rootsMap, wordsMap, new PrintStream(new File(
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
		List<String> keys = new ArrayList<String>(rootsMap.keySet());
		Collections.sort(keys);
		out.print("var roots = {\n");
		for (String name: keys) {
			Item root = rootsMap.get(name);
			out.print("\t\"");
			out.print(root.getName());
			out.print("\": { \"definition\": \"");
			out.print(root.getDefinition());
			out.print("\", \"words\": {} },\n");
		}
		out.print("};\n\n");
		
		out.print("var words = {\n");
		keys = new ArrayList<String>(wordsMap.keySet());
		Collections.sort(keys);
		for (String name: keys) {
			Item word = wordsMap.get(name);
			out.print("\t\"");
			out.print(word.getName());
			out.print("\": { \"rootNames\": [ ");
			List<String> roots = new ArrayList<String>(word.getRoots());
			Collections.sort(roots);
			for (String root: roots) {
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
