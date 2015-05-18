package com.jacobrobertson.rootsweb;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

public class GoogleUtils {

	public static void main(String[] args) throws Exception {
		createSitemap();
	}
	
	public static void createSitemap() throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date = format.format(new Date());
		StringBuffer buf = new StringBuffer();
		buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		buf.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");
		Map<String, Item> words = JsonDataMaker.downloadWordItems();
		List<Item> list = new ArrayList<Item>(words.values());
		Collections.sort(list, new WikipediaParser.ItemNameComparator());
		for (Item word: list) {
			buf.append("\t<url>\n");
			
			buf.append("\t\t<loc>");
			buf.append("http://jacobrobertson.com/roots/w/");
			buf.append(word.getName());
			buf.append("</loc>\n");
			
			buf.append("\t\t<lastmod>" + date + "</lastmod>\n");
			buf.append("\t</url>\n");
		}
		buf.append("</urlset>\n");
		
		FileOutputStream out = new FileOutputStream("sitemap.xml");
		IOUtils.write(buf.toString(), out);
	}
	
}
