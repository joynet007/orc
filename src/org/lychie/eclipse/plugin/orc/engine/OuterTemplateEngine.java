package org.lychie.eclipse.plugin.orc.engine;

import java.io.StringWriter;
import java.util.Properties;
import org.lychie.jutil.IOUtil;
import org.apache.velocity.app.VelocityEngine;
import org.lychie.eclipse.plugin.orc.exception.ORCPCastException;

/**
 * 外部模板引擎
 * 
 * @author Lychie Fan
 */
public class OuterTemplateEngine extends TemplateEngine {
	
	private static final String RESOURCE_LOADER_PATH = "file.resource.loader.path";

	@Override
	public String parseTemplate(String pathname, String filename, Object arg) {
		Thread thread = Thread.currentThread();
		ClassLoader loader = thread.getContextClassLoader();
		thread.setContextClassLoader(this.getClass().getClassLoader());
		StringWriter writer = new StringWriter();
		try {
			VelocityEngine engine = new VelocityEngine();
			Properties prop = new Properties();
			prop.put(RESOURCE_LOADER_PATH, pathname);
			engine.init(prop);
			engine.getTemplate(filename).merge(buildContext(arg), writer);
			return writer.toString();
		} catch (Throwable e) {
			throw new ORCPCastException("can not parse the outer template : "
					+ pathname + filename);
		} finally {
			thread.setContextClassLoader(loader);
			IOUtil.close(writer);
		}
	}

}