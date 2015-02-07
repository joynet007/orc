package org.lychie.eclipse.plugin.orc.datasource.model;

import java.util.List;

/**
 * 表
 * 
 * @author Lychie Fan
 */
public class Table {

	private String name;
	private List<Column> columns;

	public Table(String name, List<Column> columns) {
		this.name = name;
		this.columns = columns;
	}

	public String getName() {
		return name;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public void setName(String name) {
		this.name = name;
	}

}