package com.jacobrobertson.rootsweb;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EasyDefineUtils {

	public static void main(String[] args) throws Exception {
		EasyDefineUtils u = new EasyDefineUtils();
		u.getDefinitions(Arrays.asList("abscond"));
	}

	public Map<String, String> getDefinitions(Collection<String> words) throws Exception {
		String page = download(words);
		return parseDefinitions(page);
	}
	/*
<b>abscond</b> - run away;  usually includes taking something or somebody along<br />
	 */
	public Map<String, String> parseDefinitions(String page) throws Exception {
		Map<String, String> defs = new HashMap<String, String>();
		Pattern p = Pattern.compile("<b>(.*?)</b> - (.*?)<br />");
		Matcher m = p.matcher(page);
		while (m.find()) {
			String word = m.group(1);
			String def = m.group(2);
			defs.put(word, def);
		}
		return defs;
	}
	
	/**
	 * http://www.easydefine.com/modules/test.inc.php
	 * par1=abscond%0Aabstract%0Aacronym
	 * &numlist=false&alpha=false&numlistyn=Yes&
	 * max=1&random=false&check=0&partof=true
	 */
	public String download(Collection<String> words) throws Exception {
		StringBuilder params = new StringBuilder();
		for (String word: words) {
			if (params.length() > 0 ) {
				params.append("%0A");
			} else {
				params.append("par1=");
			}
			params.append(word);
		}
		params.append("&numlist=false&alpha=false&numlistyn=Yes&max=1&random=false&check=0&partof=true");
		String urlParameters = params.toString();
		byte[] postData = urlParameters.getBytes("UTF-8");
		int postDataLength = postData.length;
		String request = "http://www.easydefine.com/modules/test.inc.php";
		URL url = new URL(request);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setInstanceFollowRedirects(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type",	"application/x-www-form-urlencoded");
		conn.setRequestProperty("charset", "utf-8");
		conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
		conn.setUseCaches(false);
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream()); 
		wr.write(postData);
		
		StringBuilder page = new StringBuilder();
		Reader in = new BufferedReader(new InputStreamReader(
				conn.getInputStream(), "UTF-8"));
		for (int c = in.read(); c != -1; c = in.read()) {
			page.append((char) c);
		}
		
		return page.toString();
	}
}
