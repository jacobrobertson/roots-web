package com.jacobrobertson.rootsweb;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
		printWordsUsedTwice(roots);
//		printCompleteRootsReplacement(roots);
	}
	
	public static List<Item> downloadRoots() throws Exception {
		String page = download();
		return parse(page);
	}
	
	public static List<Item> parse(String page) {
		
		Pattern tablePattern = Pattern.compile("\\| (.*?) \\|\\| (.*?) \\|\\| .*?\\|\\| .*? \\|\\| (.*)");
		Matcher matcher = tablePattern.matcher(page);
		
		List<Item> roots = new ArrayList<Item>();
		
		while (matcher.find()) {
			String words = matcher.group(3);
			String def = matcher.group(2);
			def = cleanDefinition(def);
			Item root = new Item(null, def);
			parseName(matcher.group(1), root);	
			roots.add(root);
			String[] split = words.split(",");
			for (String word: split) {
				word = word.trim();
				if (word.startsWith("[")) {
					word = word.substring(2, word.length() - 2);
				}
				int bar = word.indexOf("|");
				if (bar > 0) {
					word = word.substring(bar + 1);
				}
				word = word.replaceAll("'''", "");
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
	
	
	private static List<Item> merge(List<Item> wikiRoots, Collection<Item> driveRoots) {
		// we start with the drive roots, because we've manually fixed a lot there - don't want to undo that
		List<Item> merged = new ArrayList<Item>(driveRoots);
		for (Item wikiRoot: wikiRoots) {
			String wikiSimpleName = wikiRoot.getSimpleName();
			String wikiDef = wikiRoot.getDefinition();
			boolean found = false;
			for (Item driveRoot: driveRoots) {
				if (driveRoot.getSimpleName().equals(wikiSimpleName)) {
					String driveDef = driveRoot.getDefinition();
					if (driveDef.equals(wikiDef)) {
						found = true;
						break;
					}
					if (wikiDef.contains(driveDef)) {
						System.out.println(">>>>>> consider manual merge of " + wikiSimpleName + " wikiDef(" + wikiDef + "), driveDef(" + driveDef + ")");
						found = true;
						break;
					}
					if (driveDef.contains(wikiDef)) {
						found = true;
						break;
					}
				}
			}
			if (!found) {
				merged.add(wikiRoot);
			}
		}
		
		return merged;
	}
	
	/**
	 * Due to the way there are duplicates, we will just print a brand-new list
	 */
	public static void printCompleteRootsReplacement(List<Item> wikiRoots) throws Exception {
		List<Item> alternates = generateAlternates(wikiRoots);
		wikiRoots.addAll(alternates);
		Map<String, Item> existing = JsonDataMaker.downloadRootItems();
		List<Item>roots = merge(wikiRoots, existing.values());
		assignNumbersToAllRoots(roots);
		
		List<String> lines = new ArrayList<String>();
		for (Item root: roots) {
			String name = root.getName();
			if (root.getParent() == null) {
				lines.add(name + "\t" + root.getDefinition());
			} else {
				lines.add(name + "\t>" + root.getParent().getName());
			}
		}
		Collections.sort(lines);
		for (String line: lines) {
			System.out.println(line);
		}
	}
	
	private static List<Item> generateAlternates(List<Item> roots) {
		List<Item> alts = new ArrayList<Item>();
		for (Item root: roots) {
			List<Item> some = generateAlternates(root);
			alts.addAll(some);
		}
		return alts;
	}
	private static List<Item> generateAlternates(Item root) {
		List<Item> alts = new ArrayList<Item>();
		List<String> strings = root.getAlternatives();
		for (String alt: strings) {
			Item i = new Item(alt, root.getDefinition());
			i.setParent(root);
			alts.add(i);
		}
		return alts;
	}
	
	/**
	 * TODO assign numbers to alternate roots (in fact the code here doesn't do anything - apparently all changes are to alternates)
	 * 	- will need to get the right definition, and assign numbers based on the sorting of those
	 * TODO to make this truly effective, will need to load in existing roots as well, because we might have some preexisting numbers
	 * @param roots
	 */
	private static void assignNumbersToAllRoots(List<Item> roots) {
		Collections.sort(roots, new ItemNameComparator());
		Map<String, Item> map = new HashMap<String, Item>();
		Map<String, Integer> counts = new HashMap<String, Integer>();
		for (Item root: roots) {
			Integer count = counts.get(root.getSimpleName());
			if (count == null) {
				count = 0;
			}
			count++;
			if (count > 1) {
				// fix the first count if it wasn't already fixed
				Item first = map.get(root.getSimpleName());
				if (first != null) {
					String newName = root.getSimpleName() + "(1)";
					first.setName(newName);
				}
				// update this guys's number
				String newName = root.getSimpleName() + "(" + count + ")";
				root.setName(newName);
			} else {
				// put in map so we can look back and fix later if needed
				map.put(root.getSimpleName(), root);
			}
			counts.put(root.getSimpleName(), count);
		}
		// now that the base roots are assigned, assign to all the alternates
		
	}
	private static class ItemNameComparator implements Comparator<Item> {
		@Override
		public int compare(Item i1, Item i2) {
			String s1 = i1.getSimpleName();
			String s2 = i1.getSimpleName();
			int comp = s1.compareTo(s2);
			if (comp != 0) {
				return comp;
			}
			Integer n1 = i1.getNumber();
			Integer n2 = i1.getNumber();
			if (n1 != n2) {
				if (n1 == 0) {
					return 1;
				} else if (n2 == 0) {
					return -1;
				} else {
					return n1.compareTo(n2);
				}
			}
			return i1.getDefinition().compareTo(i2.getDefinition());
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
	/**
	 * Worst case scenarios are possible:
	 *  '''ab-''', '''a-''', '''abs-''', '''au-''' <ref>{{Cite web | url=http://www.perseus.tufts.edu/hopper/text?doc=Perseus%3Atext%3A1999.04.0059%3Aentry%3Dab | title=Charlton T. Lewis, Charles Short, A Latin Dictionary, *badchars* | accessdate=2015-03-31}}</ref>
	 *  
	 *   need to handle this too
	 *   '''{{nowrap|-cid-}}'''
	 */
	private static void parseName(String text, Item item) {
		
//		if (text.contains("nowrap")) {
//			text = text.trim();
//		}
		
		// remove all citations and links (the order here matters)
//		text = text.replaceAll("'''\\{\\{nowrap\\|", "");
//		text = text.replaceAll("\\}\\}'''", "");
		text = text.replaceAll("\\{\\{nowrap\\|(.*?)\\}\\}", "$1");
		text = text.replaceAll("\\{\\{.*?\\}\\}", "");
		text = text.replaceAll("\\[\\[(.*?)\\]\\]", "$1");
		text = text.replaceAll("\\(.*?\\)", "");
		text = text.replaceAll("(<|&lt;).*?>", "");
		
		// remove all boldings
		text = text.replaceAll("'''", "");
		
		// split
		String[] split = text.split(",");
		
		// remove all dashes and spaces
		String first = null;
		for (String name: split) {
			name = name.trim();
			if (name.startsWith("-")) {
				name = name.substring(1);
			}
			if (name.endsWith("-")) {
				name = name.substring(0, name.length() - 1);
			}
			if (first == null) {
				first = name;
			} else {
				item.getAlternatives().add(name);
			}
		}
		item.setName(first);
	}
	
	public static void printWordsUsedTwice(List<Item> roots) throws Exception {
		Map<String, Item> existing = JsonDataMaker.downloadWordItems();
		Map<String, Item> driveRoots = JsonDataMaker.downloadRootItems();
		Set<String> found = new HashSet<String>();
		List<String> strings = new ArrayList<String>();
		for (Item root: roots) {
			if (root.getName() == null) {
				continue;
			}
			Set<String> set = root.getRoots();
//			System.out.println(">>>> Start >>>> " + set);
			for (Item checkRoot: roots) {
				// these "roots" are actually the words
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
						
						// try to determine the actual root numbers
						root = getDriveRoot(root, driveRoots);
						checkRoot = getDriveRoot(checkRoot, driveRoots);
						
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
	private static List<Item> getSimpleMatches(Item wikiRoot, Map<String, Item> driveRoots) {
		List<Item> matches = new ArrayList<Item>();
		String wikiName = wikiRoot.getName();
		for (Item driveRoot: driveRoots.values()) {
			String driveName = driveRoot.getSimpleName();
			if (driveName.equals(wikiName)) {
				matches.add(driveRoot);
			}
		}
		return matches;
	}
	private static Item getDriveRoot(Item wikiRoot, Map<String, Item> driveRoots) {
		String wikiDef = JsonDataMaker.getComparableDef(wikiRoot.getDefinition());
		List<Item> matches = getSimpleMatches(wikiRoot, driveRoots);
		if (!matches.isEmpty()) {
			for (Item match: matches) {
				String matchDef = JsonDataMaker.getComparableDef(match.getDefinition());
				if (wikiDef.equals(matchDef)) {
					return match;
				}
			}
			for (Item match: matches) {
				String matchDef = JsonDataMaker.getComparableDef(match.getDefinition());
				if (wikiDef.contains(matchDef) || matchDef.contains(wikiDef)) {
					return match;
				}
			}
			System.out.println(">>>>> could not figure out match for : " + wikiRoot.getName());
		}
		return wikiRoot;
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
