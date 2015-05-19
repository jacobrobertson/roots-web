package com.jacobrobertson.rootsweb;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class BubbleParser {

	public static void main(String[] args) throws Exception {
//		new BubbleParser().parseBubblFromFile("C:\\Users\\Jacob\\git\\roots-web\\Octopus_wlg1.htm");
//		new BubbleParser().convertCsvToJson();
	}
	
	
	public void parseBubblFromFile(String f) throws Exception {
		String page = fileToString(f);
//		page = "<funk>house</funk>";
		Bubble bubble = parseBubblFile(page);
		printBubble(bubble, 0);
		convertBubblesToItems(bubble);
	}
	private Bubble parseBubblFile(String contents) throws Exception {
		return parseBubblFileAsFlatFile(contents);
	}
	private Bubble parseBubblFileAsFlatFile(String doc) throws Exception {
		doc = replace(doc, "<!DOCTYPE.*<ul class=\"topnocolors\">", "");
		doc = replace(doc, "<div class=\"footer\">.*>bubbl.us", "");
		
//		<a href="#207" class="anchor">

		doc = replace(doc, "<a name=\"[0-9]+\"/>", "");
		doc = replace(doc, " id=\"[0-9]+\" pid=\"[0-9]+\"", "");
		doc = replace(doc, " class=\"directionalLines\"", "");
		
		// this needs to replace with an indentation
		doc = replace(doc, "<a href=\"#[0-9]+\" class=\"anchor\">", "    ");
		
		doc = replace(doc, "</?body>", "");
		doc = replace(doc, "</?html>", "");
		doc = replace(doc, "</?label>", "");
		doc = replace(doc, "</?div>", "");
		doc = replace(doc, "</?ul>", "");
		doc = replace(doc, "</?li>", "");
		doc = replace(doc, "</?a>", "");
		doc = replace(doc, "(\\r|\\n)+\\(", " (");
		doc = replace(doc, "&lt;br/&gt;\\(?", " (");
		doc = replace(doc, "\\) \\(", "; ");
		
		doc = replace(doc, "\\n[\\s]+\\n", "\n");
		
//		doc = replace(doc, "^ {6}", "");
		
		doc = "      " + doc.trim();
		
		System.out.println(doc);
		
		// now everything is in a tree-indented format
		
		Bubble parent = new Bubble(null, "dummy", null, null);
		Bubble root = parent;
		String[] lines = doc.split("\n");
		int lastDepth = -1;
		for (String line: lines) {
			String name;
			String definition;
			int paren = line.indexOf('(');
			if (paren > 0) {
				name = line.substring(0, paren);
				definition = line.substring(paren + 1).trim();
				if (definition.endsWith(")")) {
					definition = definition.substring(0, definition.length() - 1);
				}
			} else {
				name = line;
				definition = null;
			}
			name = name.trim();
			
			System.out.println(line);
			int depth = 0;
			while (line.charAt(depth) == ' ') {
				depth++;
			}
			depth = (depth - 6) / 4;
			
			int toWalk = lastDepth - depth + 1;
			// walk up the proper place on the tree before adding this node
			for (int i = 0; i < toWalk; i++) {
				parent = parent.getTreeParent();
			}

			// TODO turn the tree into a graph later
			Bubble thisBubble = new Bubble(parent, name, definition, null);
//					bubbleMap.get(name);
//			if (thisBubble == null) {
//				thisBubble = new Bubble(parent, name, definition, null);
//				bubbleMap.put(name, thisBubble);
//			}
			parent.getChildren().add(thisBubble);
			parent = thisBubble;
			lastDepth = depth;
		}
		return root.getChildren().get(0);
	}
	private void convertBubblesToItems(Bubble parent) {
		Map<String, Item> rootsMap = new HashMap<String, Item>();
		Map<String, Item> wordsMap = new HashMap<String, Item>();
		convertBubblesToItems(parent, rootsMap, wordsMap, false);
		JsonDataMaker.printItems(rootsMap, wordsMap, System.out);
	}
	private void convertBubblesToItems(Bubble bubble, Map<String, Item> rootsMap, Map<String, Item> wordsMap, boolean isRoot) {
		Map<String, Item> map;
		if (isRoot) {
			map = rootsMap;
		} else {
			map = wordsMap;
		}
		Item item = map.get(bubble.getName());
		String bdef = bubble.getDefinition();
		if (bdef.endsWith("...")) {
			bdef = bdef.substring(0, bdef.length() - 3);
		}
		if (item == null) {
			item = new Item(bubble.getName(), bdef);
			map.put(item.getName(), item);
		} else {
			String idef = item.getDefinition();
			if (bdef.length() > idef.length()) {
				item.setDefinition(bdef);
			}
		}
		// for each child and parent, add those roots
		if (!isRoot) {
			if (bubble.getTreeParent() != null && !bubble.getTreeParent().getName().equals("dummy")) {
				item.getRoots().add(bubble.getTreeParent().getName());
			}
			for (Bubble child: bubble.getChildren()) {
				item.getRoots().add(child.getName());
			}
		}

		// recurse
		for (Bubble child: bubble.getChildren()) {
			convertBubblesToItems(child, rootsMap, wordsMap, !isRoot);
		}

	}
	private String replace(String s, String f, String r) {
		return Pattern.compile(f, Pattern.DOTALL).matcher(s).replaceAll(r);
	}

	/*
	private void parseBubblFileAsXml(String contents) throws Exception {

		contents = cleanDocument(contents);
		
		SAXBuilder jdomBuilder = new SAXBuilder();
        Document doc = jdomBuilder.build(new StringReader(contents));
		
		//
//		<body><ul class="topnocolors">
//  <li id="1" pid="0">
//    <label>
//      <a name="1"/>
//      octopus&lt;br/&gt;(eight foot)
//    </label>		
		
        // get the first item
        Element root = doc.getRootElement();
        Element body = root.getChild("body");
        Element rootList = body.getChild("ul").getChild("li");
        Bubble b = buildWords(rootList);
        printBubble(b, 0);
	}
	*/
	
	/*

 	example 1
 	
	 <li id="186" pid="128">
        <label>
          <a name="186"/>
          ornithophobia&lt;br/&gt;(fear of birds)
        </label>
        <ul>
          <li id="187" pid="186">
            <label>
              <a name="187"/>
              ornith&lt;br/&gt;(bird)
            </label>
            <ul>
              <li id="188" pid="187">
                <label>
                  <a name="188"/>
                  ornithology&lt;br/&gt;(study of birds)
                </label>
                <div class="directionalLines">
                  <a href="#50" class="anchor">logy
					(any branch of science)</a>
                </div>
              </li>
            </ul>
          </li>
        </ul>
      </li>

 	structure
 		li
 			label
 				a (throw away
 				text: name
			ul
				(recursive)
				
		OR
		
		li
			label
				a (throw away)
				text: name
			div
				a
					text: name
 
	 */
	/*
	private Bubble buildWords(Element li) {
		// get the name
		String name = li.getChild("label").getText();
		Bubble bubble = new Bubble(name, null, null);
		
		// will either have a list of divs (terminals)
		// OR a ul with lis (recursive)
		List<Element> children = li.getChildren();
		for (Element child: children) {
			if (child.getName().equals("ul")) {
				List<Element> ulChildren = child.getChildren();
				for (Element lic: ulChildren) {
					Bubble b = buildWords(lic);
					bubble.getChildren().add(b);
				}
			} else if (child.getName().equals("div")) {
				String childName = child.getChild("a").getText();
				Bubble childBubble = new Bubble(childName, null, null);
				bubble.getChildren().add(childBubble);
			}
		}
		return bubble;
	}
	*/
	private void printBubble(Bubble b, int indent) {
		String spaces = "                                                                                                                         ".replaceAll(" ", "\t");
		System.out.println(spaces.substring(0, indent) + b.getName() + " (" + b.getDefinition() + ")");
		indent++;
		for (Bubble c: b.getChildren()) {
			printBubble(c, indent);
		}
	}
	/**
	 * NO - this didn't work - it's either an "attachment", or we need a cookie or something.
	 */
	static String getLatestBubblFile() throws Exception {
		
		// download this URL
		// https://bubbl.us/_sys/export.php?sessID=&e=h&fn=Octopus&nm=Octopus
		// it will return a string like this
		// <response><export status="success" reason="">Octopus_1191m.htm</export></response>
		
		String export = download("https://bubbl.us/_sys/export.php?sessID=&e=h&fn=Octopus&nm=Octopus");
		
		int pos = export.indexOf("Octopus");
		int pos2 = export.indexOf("<", pos);
		String fileName = export.substring(pos, pos2);
		
		// then download the file with this url
		// https://bubbl.us/download/Octopus_huwn.htm
		String file = download("https://bubbl.us/download/" + fileName);
		System.out.println(file);
		
		return  file;
	}
	private static String download(String u) throws Exception {
		URL url = new URL(u);
		InputStream in = url.openStream();
		StringBuilder buf = new StringBuilder();
		int r;
		while ((r = in.read()) >= 0) {
			buf.append((char) r);
		}
		in.close();
		return buf.toString();
	}
	private static String fileToString(String f) throws Exception {
		File file = new File(f);
		InputStream in = new FileInputStream(file);
		StringBuilder buf = new StringBuilder();
		int r;
		while ((r = in.read()) >= 0) {
			buf.append((char) r);
		}
		in.close();
		return buf.toString();
	}
}
