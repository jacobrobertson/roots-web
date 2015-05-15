package com.jacobrobertson.rootsweb;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Item {

	private int number;
	private Item parent;
	private String name;
	private String simpleName;
	private String definition;
	private Set<String> roots = new HashSet<String>(); // only applies to words
	private List<String> alternatives = new ArrayList<String>(); // only applies to words
	public Item(String name, String definition) {
		setName(name);
		this.definition = definition;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		this.simpleName = name;
		int number = 0;
		if (name != null) {
			int paren = name.indexOf('(');
			if (paren > 0) {
				String snum = name.substring(paren + 1, name.length() - 1);
				number = Integer.parseInt(snum);
				simpleName = name.substring(0, paren);
			}
		}
		setNumber(number);
	}
	public String getDefinition() {
		return definition;
	}
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	public Set<String> getRoots() {
		return roots;
	}
	public List<String> getAlternatives() {
		return alternatives;
	}
	public Item getParent() {
		return parent;
	}
	public void setParent(Item parent) {
		this.parent = parent;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getSimpleName() {
		return simpleName;
	}
}
