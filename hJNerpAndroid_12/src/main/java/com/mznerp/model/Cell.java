package com.mznerp.model;

public class Cell {
	public String title;
	public Object value;
	public String type;
	public String var_format;

	@Override
	public String toString() {
		return "Cell{" +
				"title='" + title + '\'' +
				", value=" + value +
				", type='" + type + '\'' +
				", var_format='" + var_format + '\'' +
				'}';
	}
}
