package org.lychie.eclipse.plugin.orc.engine;

import java.io.File;
import org.lychie.eclipse.plugin.orc.res.model.Configuration;
import org.lychie.eclipse.plugin.orc.util.Project;
import org.lychie.jutil.StringUtil;
import org.lychie.jutil.exception.UnexpectedException;

/**
 * 模板引擎
 * 
 * @author Lychie Fan
 */
public class VmEngine {

	private String templateDir;
	private TemplateEngine engine;
	private Configuration configuration;
	private static final String INNER_TEMPLATE_FILE_DIR = "/org/lychie/eclipse/plugin/orc/res/";

	public VmEngine(Configuration configuration) {
		this.configuration = configuration;
		this.engine = getTemplateEngine();
	}

	/**
	 * 解析模板
	 * 
	 * @param filename
	 *            模板文件名称
	 * @param model
	 *            模型
	 * @return
	 */
	public String parseTemplate(String filename, Object model) {
		try {
			return engine.parseTemplate(templateDir, filename, model);
		} catch (Throwable e) {
			throw new UnexpectedException(e);
		}
	}

	/**
	 * 获取模板引擎
	 * 
	 * @return
	 */
	private TemplateEngine getTemplateEngine() {
		if (StringUtil.isEmpty(configuration.getTemplateFolder())) {
			templateDir = INNER_TEMPLATE_FILE_DIR;
			return new InnerTemplateEngine();
		} else {
			templateDir = Project.getPath()
					+ configuration.getTemplateFolder();
			if (!templateDir.endsWith(File.separator)) {
				templateDir += File.separator;
			}
			return new OuterTemplateEngine();
		}
	}

}