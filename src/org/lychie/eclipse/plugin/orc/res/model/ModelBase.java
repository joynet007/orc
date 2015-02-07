package org.lychie.eclipse.plugin.orc.res.model;

/**
 * ModelBase
 * 
 * @author Lychie Fan
 */
public class ModelBase {

	protected String packagename;

	public ModelBase() {}

	public ModelBase(String packagename) {
		this.packagename = packagename;
	}

	public String getPackagename() {
		return packagename;
	}

	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}

}