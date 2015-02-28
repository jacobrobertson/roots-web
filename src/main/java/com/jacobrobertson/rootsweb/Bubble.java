package com.jacobrobertson.rootsweb;

import java.util.ArrayList;
import java.util.List;

public class Bubble {

	private String name;
	private String otherNames;
	private String definition;
	private Bubble treeParent;
	
	private List<Bubble> children = new ArrayList<Bubble>();

	public Bubble(Bubble treeParent, String name, String definition, String otherNames) {
		this.name = name;
		this.definition = definition;
		this.otherNames = otherNames;
		this.treeParent = treeParent;
	}

	public Bubble getTreeParent() {
		return treeParent;
	}
	
	public List<Bubble> getChildren() {
		return children;
	}
	
	public String getName() {
		return name;
	}

	public String getOtherNames() {
		return otherNames;
	}

	public String getDefinition() {
		return definition;
	}
	
	
	
}
