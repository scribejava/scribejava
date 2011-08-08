package org.scribe.utils;

import java.util.AbstractMap;

public class Param extends AbstractMap.SimpleEntry<String, String> implements
		Comparable<Param> {
	
	private static final long serialVersionUID = -8759599581313805692L;

	public Param(String key, String value) {
		super(key, value);
	}

	@Override
	public int compareTo(Param that) {
		return this.getKey().compareTo(that.getKey());
	}
}