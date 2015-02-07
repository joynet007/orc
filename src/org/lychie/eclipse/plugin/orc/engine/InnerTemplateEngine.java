package org.lychie.eclipse.plugin.orc.engine;

import java.io.StringWriter;
import java.util.Properties;
import org.lychie.jutil.IOUtil;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.lychie.eclipse.plugin.orc.exception.ORCPCastException;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

/**
 * 内部模板引擎
 * 
 * @author Lychie Fan
 */
public class InnerTemplateEngine extends TemplateEngine {

	private static final String CLASSPATH = "classpath";
	private static final String CLASSPATH_RESOURCE_LOADER = "classpath.resource.loader.class";

	@Override
	public String parseTemplate(String pathname, String filename, Object arg) {
		StringWriter writer = new StringWriter();
		try {
			pathname += filename;
			VelocityEngine engine = getVelocityEngine();
			engine.getTemplate(pathname).merge(buildContext(arg), writer);
			return writer.toString();
		} catch (Throwable e) {
			throw new ORCPCastException("can not parse the inner template : "
					+ pathname + filename);
		} finally {
			IOUtil.close(writer);
		}
	}

	/**
	 * 获取引擎对象
	 * 
	 * @return
	 * @throws Throwable
	 */
	private VelocityEngine getVelocityEngine() throws Throwable {
		Properties prop = new Properties();
		prop.put(RuntimeConstants.RESOURCE_LOADER, CLASSPATH);
		prop.put(CLASSPATH_RESOURCE_LOADER,
				ClasspathResourceLoader.class.getName());
		VelocityEngine engine = new VelocityEngine();
		engine.init(prop);
		return engine;
	}

}