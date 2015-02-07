package org.lychie.eclipse.plugin.orc.engine;

import java.util.Set;
import org.lychie.beanutil.ClassWrapper;
import org.apache.velocity.VelocityContext;
import org.lychie.eclipse.plugin.orc.util.Utilities;

/**
 * 模板引擎
 * 
 * @author Lychie Fan
 */
public abstract class TemplateEngine {

	private static final Utilities UTILITIES = new Utilities();

	/**
	 * 构建上下文对象
	 * 
	 * @param model
	 *            模型
	 * @return
	 */
	protected VelocityContext buildContext(Object model) {
		VelocityContext context = new VelocityContext();
		ClassWrapper wrapper = ClassWrapper.wrap(model.getClass());
		Set<String> properties = wrapper.getProperties().keySet();
		for (String property : properties) {
			context.put(property, wrapper.getPropertyValue(model, property));
		}
		context.put("util", UTILITIES);
		return context;
	}
	
	/**
	 * 解析模板
	 * 
	 * @param pathname
	 *            路径名称
	 * @param filename
	 *            文件名称
	 * @param arg
	 *            模型
	 * @return
	 */
	public abstract String parseTemplate(String pathname, String filename, Object arg);

}