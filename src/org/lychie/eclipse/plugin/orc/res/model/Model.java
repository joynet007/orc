package org.lychie.eclipse.plugin.orc.res.model;

/**
 * Model
 * 
 * @author Lychie Fan
 */
public class Model extends ModelBase {

	private String table;

	public Model() {}

	public Model(String packagename, String table) {
		this.table = table;
		this.packagename = packagename;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

}