package org.lychie.eclipse.plugin.orc.res.model;

/**
 * 配置参数键
 * 
 * @author Lychie Fan
 */
public interface IConfiguration {

	String USERNAME = "orc.db.username";
	
	String PASSWORD = "orc.db.password";
	
	String CONNECTION = "orc.db.conn.url";
	
	String PACKAGE_NAME = "orc.package.name";
	
	String SOURCE_FOLDER = "orc.source.folder";
	
	String EXCLUDE_TABLES = "orc.ds.exclude.tables";
	
	String EXCLUDE_COLUMNS = "orc.ds.exclude.columns";
	
	String TEMPLATE_FOLDER = "orc.model.template.folder";
	
}