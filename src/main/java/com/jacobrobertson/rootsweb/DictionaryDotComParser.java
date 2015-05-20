package com.jacobrobertson.rootsweb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;


public class DictionaryDotComParser {
	
	public static void main(String[] args) throws Exception {
		DictionaryDotComParser p = new DictionaryDotComParser();
		p.useCache = true;
		p.doVocabularyList();
//		p.doVocabularyList(Arrays.asList("abbreviate"));
	}
	
	private boolean useCache = true;
	private Set<String> legalRoots;
	private Map<String, Item> driveWords;
	private Map<String, Item> driveRoots;
	
	public DictionaryDotComParser() throws Exception {
		driveRoots = JsonDataMaker.downloadRootItems();
		driveWords = JsonDataMaker.downloadWordItems();
		// we want to restrict the roots we're willing to look at to avoid massive false positives
		legalRoots = new HashSet<String>();
		for (String root: driveRoots.keySet()) {
			if (root.length() > 2) {
				legalRoots.add(root);
			}
		}
	}
	
	public void doVocabularyList() throws Exception {
		List<String> list = getVocabularyList();
		doVocabularyList(list);
	}
	public void doVocabularyList(List<String> list) throws Exception {
		int rowNum = 1;
		EasyDefineUtils define = new EasyDefineUtils();
		Map<String, String> definitions = define.getDefinitions(list);
		for (String word: list) {
			Item driveWord = driveWords.get(word);
			if (driveWord != null) {
				continue;
//				System.out.println("\t>" + word + " > " + driveWord.getRoots() 
//						+ " / " + parsedWord.getName() + parsedWord.getRoots());
			}
			Item parsedWord = parseOneWord(word);
			if (parsedWord == null) {
				continue;
			}
			
			Set<String> itemRoots = new HashSet<String>(parsedWord.getRoots());
			itemRoots.retainAll(legalRoots);
			
			// get rid of redundancies
			Set<String> finalRoots = new HashSet<String>();
			for (String root: itemRoots) {
				// get all matching roots (i.e. with numbers)
				List<Item> matches = WikipediaParser.getSimpleMatches(root, driveRoots);
				// for each, determine if there is a parent in the parsed word's roots
				boolean foundParent = false;
				for (Item match: matches) {
					if (match.getParent() != null) {
						String pname = match.getParent().getSimpleName();
						if (itemRoots.contains(pname)) {
							foundParent = true;
							break;
						}
					}
				}
				if (!foundParent) {
					finalRoots.add(root);
				}
			}
			
			if (finalRoots.size() > 1) {
//				System.out.println(parsedWord.getName() + finalRoots + " // " + parsedWord.getRoots());
				
				List<String> sortRoots = new ArrayList<String>(finalRoots);
				Collections.sort(sortRoots);
				StringBuilder buf = new StringBuilder();
				for (String sortRoot: sortRoots) {
					if (buf.length() > 0) {
						buf.append(", ");
					} else {
						buf.append(word);
						buf.append("\t");
					}
					buf.append(sortRoot);
				}
				buf.append("\t");
				String wordDefinition = definitions.get(word);
				buf.append(wordDefinition);
				buf.append("\t");
				// we need this to be a formula
//				buf.append(wordDefinition.length());
				buf.append("=len(c" + (rowNum++) + ")");
				buf.append("\t");
				String def = getMatchingRootDefs(parsedWord, finalRoots);
				buf.append(def);
				System.out.println(buf);
			}
//			Item driveWord = driveWords.get(word);
//			if (driveWord != null) {
//				System.out.println("\t>" + word + " > " + driveWord.getRoots() 
//						+ " / " + parsedWord.getName() + parsedWord.getRoots());
//			}
		}
	}
	private String getMatchingRootDefs(Item word, Collection<String> simpleRootsList) {
		StringBuilder rootDefs = new StringBuilder();
		for (String sroot: simpleRootsList) {
			List<Item> matches = WikipediaParser.getSimpleMatches(sroot, driveRoots);
			for (Item match: matches) {
				if (rootDefs.length() > 0) {
					rootDefs.append(" / ");
				}
				while (match.isChild()) {
					match = match.getParent();
				}
				rootDefs.append(match.getDefinition());
			}
		}
		return rootDefs.toString();
	}
	
	public String downloadOneWord(String word) throws Exception {
		String cached = getCachedWord(word);
		if (cached != null) {
//			System.out.println("From cache:" + word);
			return cached;
		}
		// don't want to DDOS
		Thread.sleep(500);
		
//		System.out.println("Downloading:" + word);
		String uPath = "http://dictionary.reference.com/browse/" + word;
		URL url = new URL(uPath);
		URLConnection con = url.openConnection();
		con.setRequestProperty("Accept-Charset", "UTF-8");
		con.setRequestProperty("Content-Type", "text/html; charset=utf-8");
//		String encoding = con.getContentEncoding();
//		String props = con.getRequestProperties().toString();
		String page;
		try {
			// TODO instead of returning null, should "cache" the file as "not found" so
			//		we don't look it up again
			InputStreamReader reader = new InputStreamReader(con.getInputStream(), "UTF-8");
			page = IOUtils.toString(reader);
		} catch (Exception e) {
			page = "<html>PAGE NOT FOUND BY PARSER</html>";
		}
//		byte[] bytes = IOUtils.toByteArray(con.getInputStream());
//		String page = new String(bytes, "UTF-8");
		
		File f = new File(getCachePath(word));
		f.getParentFile().mkdirs();
		FileOutputStream out = new FileOutputStream(f);
		OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
		IOUtils.write(page, writer);
		
		return page;
	}
	
	private String getCachePath(String word) {
		return "cache/" + word.substring(0, 1) + "/" + word.substring(0, 2) + "/" + word;
	}
	
	public String getCachedWord(String word) throws Exception {
		if (!useCache) {
			return null;
		}
		File f = new File(getCachePath(word));
		if (f.exists()) {
			String page = IOUtils.toString(new FileInputStream(f));
			return page;
		} else {
			return null;
		}
	}
	public Item parseOneWord(String word) throws Exception {
		String page = downloadOneWord(word);
		if (page == null) {
			return null;
		}
		Item item = parsePage(word, page);
		return item;
	}
	

	/*
	 * TODO restrict these even more - like choose "from <.." and "+ <..."
	 */
	private Pattern[] xrefPatterns = { 
			Pattern.compile("<a class=\"dbox-xref\" href=\"/browse/.*?\">(.*?)</a>"),
			Pattern.compile("<a class=\"dbox-xref dbox-roman\" href=\"http://dictionary.reference.com/browse/.*?\">(.*?)</a>"),
			Pattern.compile("<span class=\"dbox-italic\">(.*?)</span>"),
//			Pattern.compile("<span class=\"dbox-bold\">(.*?)</span>"), // this is returning the "phonetic part at the top
	};
	
	/*
	 * <span class="dbox-italic">breviÄ?tus</span> 
	 * (<span class="dbox-italic">brevi</span>(<span class="dbox-italic">s</span>)
	 * 
	 * <a class="dbox-xref" href="/browse/cardio-">cardio-</a> + 
	 * <a class="dbox-xref" href="/browse/-logy">-logy</a>
	 * 
	 * <a class="dbox-xref dbox-roman" href="http://dictionary.reference.com/browse/cardio-">cardio-</a> + 
	 * <a class="dbox-xref dbox-roman" href="http://dictionary.reference.com/browse/-logy">-logy</a> 
	 * 
	 */
	
	/*	<div class="tail-box tail-type-origin">
 	<div class="tail-header">
 	<span>
 		<span class="oneClick-link oneClick-available">Word</span> 
 		<span class="oneClick-link oneClick-available">Origin</span> 
 	</span>
 	</div>
	<div class="tail-content">
		<div class="tail-elements">
			<span>
			<span class="oneClick-link">C19:</span> 
			<span class="oneClick-link oneClick-available">New</span> 
			<span class="oneClick-link oneClick-available">Latin,</span> 
			<span class="oneClick-link">from</span> 
			</span>
			<span class="dbox-sc">
			<span>
			<span class="oneClick-link oneClick-available">epi-</span> 
			</span>
			</span>
			<span>+ <span class="oneClick-link oneClick-available">Greek</span> </span>
			<span class="dbox-italic"><span><span class="oneClick-link oneClick-available">kardia</span> </span></span>
			<span><span class="oneClick-link oneClick-available">heart</span> </span>
		</div>
	</div>
	</div>
*/
	
	public Item parsePage(String word, String text) {
		Item item = new Item(word, null);
		for (Pattern pattern: xrefPatterns) {
			Set<String> found = findAll(text, pattern);
			item.getRoots().addAll(found);
		}
		return item;
	}
	public Set<String> findAll(String text, Pattern pattern) {
		Set<String> roots = new HashSet<String>();
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			String found = matcher.group(1);
			found = cleanRoot(found);
			roots.add(found);
		}
		return roots;
	}
	public String cleanRoot(String root) {
		root = root.replaceAll("-", "");
		return root;
	}
	public List<String> getVocabularyList() throws Exception {
		Set<String> set = new HashSet<String>();
		File file = new File("src/data/vocabulary.com-list-52473.txt");
		String page = IOUtils.toString(new FileInputStream(file));
		String[] split = page.split("\\s+");
		for (String one: split) {
			set.add(one);
		}
		List<String> list = new ArrayList<String>();
		list.addAll(set);
		Collections.sort(list);
		return list;
	}
	
}
