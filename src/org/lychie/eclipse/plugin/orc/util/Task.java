package org.lychie.eclipse.plugin.orc.util;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

import org.lychie.eclipse.plugin.orc.datasource.DBConnection;
import org.lychie.eclipse.plugin.orc.datasource.DBMetaData;
import org.lychie.eclipse.plugin.orc.datasource.model.Table;
import org.lychie.eclipse.plugin.orc.engine.VmEngine;
import org.lychie.eclipse.plugin.orc.exception.ORCPCastException;
import org.lychie.eclipse.plugin.orc.res.model.Configuration;
import org.lychie.eclipse.plugin.orc.res.model.Model;
import org.lychie.eclipse.plugin.orc.res.model.ModelBase;
import org.lychie.eclipse.plugin.orc.res.model.ModelMaster;
import org.lychie.eclipse.plugin.orc.ui.Navigation;
import org.lychie.eclipse.plugin.orc.util.FileQueue.Entry;
import org.lychie.jutil.FileUtil;
import org.lychie.jutil.StringUtil;
import org.lychie.jutil.exception.UnexpectedException;

/**
 * 任务工具类
 * 
 * @author Lychie Fan
 */
public class Task {

	private static final String MODEL_FILE_NAME = "Model.vm";
	private static final String MODELBASE_FILE_NAME = "ModelBase.vm";
	private static final String MODELMASTER_FILE_NAME = "ModelMaster.vm";
	private static final String MODELBASE_JAVA_FILE = "ModelBase.java";
	private static final String MODELMASTER_JAVA_FILE = "ModelMaster.java";
	private static final String MODEL_TEMPLATE_FILE = "/org/lychie/eclipse/plugin/orc/res/Model.vm";
	private static final String MODELBASE_TEMPLATE_FILE = "/org/lychie/eclipse/plugin/orc/res/ModelBase.vm";
	private static final String MODELMASTER_TEMPLATE_FILE = "/org/lychie/eclipse/plugin/orc/res/ModelMaster.vm";

	/**
	 * 启动任务
	 */
	public static void startup() {
		File configFile = new File(Project.getPath(),
				Configurations.CONFIG_FILE_NAME);
		Configuration configuration = Configurations
				.parseConfiguration(configFile);
		String userModelTemDir = configuration.getTemplateFolder();
		if (StringUtil.isNotEmpty(userModelTemDir)) {
			createModelTemplateDuplicate(userModelTemDir);
		}
		List<Table> tables = getTables(configuration);
		List<Table> activeTables = Configurations.getActiveTables(tables,
				configuration);
		sourceFileEnQueue(activeTables, configuration);
	}

	/**
	 * 源代码文件入队
	 * 
	 * @param tables
	 *            表
	 * @param configuration
	 *            配置
	 */
	private static void sourceFileEnQueue(List<Table> tables,
			Configuration configuration) {
		FileQueue.init();
		String packagename = configuration.getPackageName();
		File dir = buildModelSourceFolder(configuration);
		VmEngine engine = new VmEngine(configuration);
		modelBaseEnQueue(engine, dir, packagename);
		modelMasterEnQueue(engine, dir, tables, packagename);
		modelEnQueue(engine, dir, tables, packagename);
		executeQueue();
	}
	
	/**
	 * 执行队列任务
	 */
	private static void executeQueue() {
		try {
			Navigation navigation = new Navigation();
			navigation.setProgressBound(FileQueue.size());
			LinkedList<Entry> entries = FileQueue.getEntries();
			int index = 1;
			Thread.sleep(200);
			for(Entry entry : entries){
				FileUtil.write(entry.getContent(), entry.getFile());
				navigation.setProgress(index++, entry.getMessage());
				Thread.sleep(10);
			}
			navigation.setProgress(index++, "");
		} catch (Throwable e) {
			throw new UnexpectedException(e);
		}
	}

	/**
	 * 具体的实体类模型入队
	 * 
	 * @param engine
	 *            引擎
	 * @param dir
	 *            目录
	 * @param tables
	 *            表集合
	 * @param packagename
	 *            包名
	 */
	private static void modelEnQueue(VmEngine engine, File dir,
			List<Table> tables, String packagename) {
		String filename;
		for (Table table : tables) {
			filename = table.getName() + ".java";
			File file = new File(dir, filename);
			if (!file.exists()) {
				Model model = new Model(packagename, table.getName());
				String source = engine.parseTemplate(MODEL_FILE_NAME, model);
				FileQueue.add(new Entry(source, file));
			}
		}
	}

	/**
	 * ModelMaster入队
	 * 
	 * @param engine
	 *            引擎
	 * @param dir
	 *            目录
	 * @param tables
	 *            表集合
	 * @param packagename
	 *            包名
	 */
	private static void modelMasterEnQueue(VmEngine engine, File dir,
			List<Table> tables, String packagename) {
		ModelMaster model = new ModelMaster(packagename, tables);
		String source = engine.parseTemplate(MODELMASTER_FILE_NAME, model);
		FileQueue.add(new Entry(source, new File(dir, MODELMASTER_JAVA_FILE)));
	}

	/**
	 * ModelBase入队
	 * 
	 * @param engine
	 *            引擎
	 * @param dir
	 *            目录
	 * @param packagename
	 *            包名
	 */
	private static void modelBaseEnQueue(VmEngine engine, File dir,
			String packagename) {
		File file = new File(dir, MODELBASE_JAVA_FILE);
		if (!file.exists()) {
			ModelBase model = new ModelBase(packagename);
			String source = engine.parseTemplate(MODELBASE_FILE_NAME, model);
			FileQueue.add(new Entry(source, file));
		}
	}

	/**
	 * 获取数据库表集合
	 * 
	 * @param configuration
	 *            配置
	 * @return
	 */
	private static List<Table> getTables(Configuration configuration) {
		String username = configuration.getUsername();
		String password = configuration.getPassword();
		String connection = configuration.getConnection();
		Connection conn = DBConnection.getConnection(username, password,
				connection);
		DBMetaData metaData = new DBMetaData(conn);
		return metaData.getTables();
	}

	/**
	 * 获取目录的绝对路径
	 * 
	 * @param dir
	 *            目录
	 * @return
	 */
	private static String getAbsoluteDir(String dir) {
		String pathname = Project.getPath() + dir;
		if (!pathname.endsWith(File.separator)) {
			pathname += File.separator;
		}
		return pathname;
	}

	/**
	 * 创建模型模板文件的副本
	 * 
	 * @param dir
	 *            目录
	 */
	private static void createModelTemplateDuplicate(String dir) {
		String pathname = getAbsoluteDir(dir);
		FileUtil.mkdir(new File(pathname), true);
		File file = new File(pathname, MODEL_FILE_NAME);
		InputStream in;
		if (!file.exists()) {
			in = Task.class.getResourceAsStream(MODEL_TEMPLATE_FILE);
			FileUtil.write(in, file);
		}
		file = new File(pathname, MODELBASE_FILE_NAME);
		if (!file.exists()) {
			in = Task.class.getResourceAsStream(MODELBASE_TEMPLATE_FILE);
			FileUtil.write(in, file);
		}
		file = new File(pathname, MODELMASTER_FILE_NAME);
		if (!file.exists()) {
			in = Task.class.getResourceAsStream(MODELMASTER_TEMPLATE_FILE);
			FileUtil.write(in, file);
		}
	}

	/**
	 * 构建模型类的源文件目录
	 * 
	 * @param configuration
	 *            配置
	 * @return
	 */
	private static File buildModelSourceFolder(Configuration configuration) {
		String packagename = configuration.getPackageName();
		packagename = packagename.replace(".", File.separator);
		String sourceFolderPath = getSourceFolderPath(configuration);
		String pathname = sourceFolderPath + packagename;
		File dir = new File(pathname);
		FileUtil.mkdir(dir, true);
		return dir;
	}

	/**
	 * 获取源文件目录路径
	 * 
	 * @param configuration
	 *            配置
	 * @return
	 */
	private static String getSourceFolderPath(Configuration configuration) {
		String sourceFolder = configuration.getSourceFolder();
		String pathname;
		if (StringUtil.isEmpty(sourceFolder)) {
			pathname = Configurations.MAVEN_STYLE_SOURCE_FOLDER;
			if (!new File(Project.getPath(), pathname).exists()) {
				pathname = Configurations.JAVA_STYLE_SOURCE_FOLDER;
				if (!new File(Project.getPath(), pathname).exists()) {
					throw new ORCPCastException(
							"can not found the default source folder 'src' or 'src/java/main'");
				}
			}
		} else {
			pathname = configuration.getSourceFolder();
		}
		return getAbsoluteDir(pathname);
	}

}