package com.java.edu.morphology;

public class Data {
	private int type;
	private String value = null;
	public Data(int _type, String _value) {
		type = _type;
		value = _value;
	}
	public Data(int _type) {
		type = _type;
	}
	public int getType() {
		return type;
	}
	public String getValue() {
		return value;
	}
}
