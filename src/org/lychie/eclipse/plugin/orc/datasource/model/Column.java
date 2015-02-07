package org.lychie.eclipse.plugin.orc.datasource.model;

/**
 * 列
 * 
 * @author Lychie Fan
 */
public class Column {

	private String name;
	private String type;

	public Column(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

}