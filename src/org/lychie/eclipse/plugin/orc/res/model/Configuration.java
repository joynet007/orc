package org.lychie.eclipse.plugin.orc.res.model;

/**
 * 配置类
 * 
 * @author Lychie Fan
 */
public class Configuration {

	private String username;
	private String password;
	private String connection;
	private String packageName;
	private String sourceFolder;
	private String excludeTables;
	private String excludeColumns;
	private String templateFolder;

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getConnection() {
		return connection;
	}

	public String getExcludeTables() {
		return excludeTables;
	}

	public String getExcludeColumns() {
		return excludeColumns;
	}

	public String getSourceFolder() {
		return sourceFolder;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getTemplateFolder() {
		return templateFolder;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setConnection(String connection) {
		this.connection = connection;
	}

	public void setExcludeTables(String excludeTables) {
		this.excludeTables = excludeTables;
	}

	public void setExcludeColumns(String excludeColumns) {
		this.excludeColumns = excludeColumns;
	}

	public void setSourceFolder(String sourceFolder) {
		this.sourceFolder = sourceFolder;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void setTemplateFolder(String templateFolder) {
		this.templateFolder = templateFolder;
	}

}