package com.jacobrobertson.rootsweb;

import java.util.HashSet;
import java.util.Set;

public class Item {

	private String name;
	private String definition;
	private Set<String> roots = new HashSet<String>(); // only applies to words
	public Item(String name, String definition) {
		this.name = name;
		this.definition = definition;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	
}
