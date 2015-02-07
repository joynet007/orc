package org.lychie.eclipse.plugin.orc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.lychie.eclipse.plugin.orc.datasource.model.Column;
import org.lychie.eclipse.plugin.orc.datasource.model.Table;
import org.lychie.eclipse.plugin.orc.exception.ORCPCastException;
import org.lychie.eclipse.plugin.orc.res.model.Configuration;
import org.lychie.eclipse.plugin.orc.res.model.IConfiguration;
import org.lychie.jutil.ArrayUtil;
import org.lychie.jutil.FileUtil;
import org.lychie.jutil.IOUtil;
import org.lychie.jutil.StringUtil;
import org.lychie.jutil.exception.UnexpectedException;

/**
 * 配置文件工具类
 * 
 * @author Lychie Fan
 */
public class Configurations implements IConfiguration {

	public  static final String JAVA_STYLE_SOURCE_FOLDER = "src";
	public  static final String MAVEN_STYLE_SOURCE_FOLDER = "src/main/java";
	public  static final String CONFIG_FILE_NAME = ".settings" + File.separator + "org.eclipse.orc.conf";
	private static final String ALL = "*";
	private static final String EMPTY = "";
	private static final String POSTIL = "#";
	private static final String CONFIG_FILE = "/org/lychie/eclipse/plugin/orc/res/orc.conf";
	
	/**
	 * 解析配置文件
	 * 
	 * @param configFile
	 *            配置文件
	 * @return
	 */
	public static Configuration parseConfiguration(File configFile) {
		InputStream in = null;
		try {
			Configuration configuration = new Configuration();
			Properties prop = new Properties();
			in = new FileInputStream(configFile);
			prop.load(in);
			checkinConfigure(prop);
			configuration.setUsername(prop.getProperty(USERNAME));
			configuration.setPassword(prop.getProperty(PASSWORD));
			configuration.setConnection(prop.getProperty(CONNECTION));
			configuration.setPackageName(prop.getProperty(PACKAGE_NAME));
			configuration.setSourceFolder(prop.getProperty(SOURCE_FOLDER));
			configuration.setExcludeTables(prop.getProperty(EXCLUDE_TABLES, EMPTY));
			configuration.setExcludeColumns(prop.getProperty(EXCLUDE_COLUMNS, EMPTY));
			configuration.setTemplateFolder(prop.getProperty(TEMPLATE_FOLDER, EMPTY));
			return configuration;
		} catch (Throwable e) {
			throw new UnexpectedException(e);
		} finally {
			IOUtil.close(in);
		}
	}

	/**
	 * 创建配置文件的副本
	 * 
	 * @param pathname
	 *            路径名称
	 * @param config
	 *            Configuration
	 * @return
	 */
	public static File createDuplicate(String pathname, Configuration config) {
		InputStream in = Configurations.class.getResourceAsStream(CONFIG_FILE);
		String text = FileUtil.readAsString(in);
		Map<String, String> mapper = repairConfiguration(config);
		text = StringUtil.format(text, config.getUsername(),
				config.getPassword(), config.getConnection(),
				config.getPackageName(), config.getSourceFolder(),
				mapper.get(EXCLUDE_TABLES), config.getExcludeTables(),
				mapper.get(EXCLUDE_COLUMNS), config.getExcludeColumns(),
				mapper.get(TEMPLATE_FOLDER), config.getTemplateFolder());
		File configFile = new File(pathname, CONFIG_FILE_NAME);
		FileUtil.write(text, configFile);
		return configFile;
	}

	/**
	 * 获取有效的表集合
	 * 
	 * @param tables
	 *            源表集合
	 * @param configuration
	 *            配置
	 * @return
	 */
	public static List<Table> getActiveTables(List<Table> tables,
			Configuration configuration) {

		List<Table> list;
		String excludeTableNames = configuration.getExcludeTables();
		String excludeColumnNames = configuration.getExcludeColumns();
		if (StringUtil.isEmpty(excludeTableNames)) {
			list = tables;
		} else {
			list = new ArrayList<Table>();
			String[] names = excludeTableNames.split(",");
			trim(names);
			for (Table table : tables) {
				if (!ArrayUtil.contains(names, table.getName())) {
					list.add(table);
				}
			}
		}
		if (StringUtil.isNotEmpty(excludeColumnNames)) {
			String[] names = excludeColumnNames.split(",");
			trim(names);
			for (String name : names) {
				for (Table table : list) {
					table.setColumns(getActiveColumns(table, name));
				}
			}
		}
		trim(list);
		return list;
	}

	/**
	 * 获取有效的列集合
	 * 
	 * @param table
	 *            表对象
	 * @param name
	 *            排除的列名称
	 * @return
	 */
	private static List<Column> getActiveColumns(Table table, String name) {

		List<Column> list;
		String tname = StringUtil.beforeString(name, ".");
		String cname = StringUtil.afterString(name, ".");
		if (tname.equals(table.getName()) || tname.equals(ALL)) {
			list = new ArrayList<Column>();
			List<Column> columns = table.getColumns();
			for (Column column : columns) {
				if (!column.getName().equals(cname)) {
					list.add(column);
				}
			}
		} else {
			list = table.getColumns();
		}
		return list;

	}

	/**
	 * 整理表
	 * 
	 * @param tables
	 *            表集合
	 */
	private static void trim(List<Table> tables) {
		for (Table table : tables) {
			table.setName(StringUtil.toCapitalize(table.getName()));
		}
	}

	/**
	 * 整理数组内容
	 * 
	 * @param array
	 *            数组
	 */
	private static void trim(String[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i] = array[i].trim();
		}
	}
	
	/**
	 * 校验异常
	 * 
	 * @param key
	 *            键
	 * @param configFile
	 *            配置文件
	 * @return
	 */
	private static ORCPCastException checkinCastException(String key) {
		return new ORCPCastException(key + " can not be null");
	}
	
	/**
	 * 校验配置文件
	 * 
	 * @param prop
	 *            Properties
	 */
	private static void checkinConfigure(Properties prop) {
		if (StringUtil.isEmpty(prop.getProperty(USERNAME))) {
			throw checkinCastException("DB Username");
		}
		if (StringUtil.isEmpty(prop.getProperty(PASSWORD))) {
			throw checkinCastException("DB Password");
		}
		if (StringUtil.isEmpty(prop.getProperty(CONNECTION))) {
			throw checkinCastException("Connect URL");
		}
		if (StringUtil.isEmpty(prop.getProperty(PACKAGE_NAME))) {
			throw checkinCastException("Package Name");
		}
		if (StringUtil.isEmpty(prop.getProperty(SOURCE_FOLDER))) {
			throw checkinCastException("Source Folder");
		}
	}
	
	/**
	 * 补偿配置文件
	 * 
	 * @param config
	 *            配置信息对象
	 * @return
	 */
	private static Map<String, String> repairConfiguration(Configuration config) {
		Map<String, String> map = new HashMap<String, String>(3, 1);
		if (StringUtil.isEmpty(config.getExcludeTables())) {
			map.put(EXCLUDE_TABLES, POSTIL);
		} else {
			map.put(EXCLUDE_TABLES, EMPTY);
		}
		if (StringUtil.isEmpty(config.getExcludeColumns())) {
			map.put(EXCLUDE_COLUMNS, POSTIL);
		} else {
			map.put(EXCLUDE_COLUMNS, EMPTY);
		}
		if (StringUtil.isEmpty(config.getTemplateFolder())) {
			map.put(TEMPLATE_FOLDER, POSTIL);
		} else {
			map.put(TEMPLATE_FOLDER, EMPTY);
		}
		return map;
	}

}