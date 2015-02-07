package org.lychie.eclipse.plugin.orc.res.model;

import java.util.List;
import org.lychie.eclipse.plugin.orc.datasource.model.Table;

/**
 * ModelMaster
 * 
 * @author Lychie Fan
 */
public class ModelMaster extends ModelBase {

	private List<Table> tables;

	public ModelMaster() {}

	public ModelMaster(String packagename, List<Table> tables) {
		this.tables = tables;
		this.packagename = packagename;
	}

	public List<Table> getTables() {
		return tables;
	}

	public void setTables(List<Table> tables) {
		this.tables = tables;
	}
	
}