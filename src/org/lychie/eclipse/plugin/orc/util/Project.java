package org.lychie.eclipse.plugin.orc.util;

import java.io.File;
import org.lychie.jutil.FileUtil;

/**
 * 项目工具类
 * 
 * @author Lychie Fan
 */
public class Project {

	private static String pathname;

	/**
	 * 获取项目的路径
	 * 
	 * @return
	 */
	public static String getPath() {
		String path = FileUtil.getUniformPath(pathname);
		if (!path.endsWith(File.separator)) {
			return path += File.separator;
		}
		return path;
	}

	/**
	 * 设置项目的路径
	 * 
	 * @param pathname
	 *            路径名称
	 */
	public static void setPath(String pathname) {
		Project.pathname = pathname;
	}

}