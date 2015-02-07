package org.lychie.eclipse.plugin.orc.util;

import org.lychie.jutil.StringUtil;

/**
 * 工具类
 * 
 * @author Lychie Fan
 */
public class Utilities {

	private static final String IS = "is";
	private static final String GET = "get";
	private static final String SET = "set";
	private static final String BOOLEAN = "boolean";

	/**
	 * set方法
	 * 
	 * @param name
	 *            属性名称
	 * @return
	 */
	public static String set(String name) {
		return SET + StringUtil.toCapitalize(name);
	}

	/**
	 * get方法
	 * 
	 * @param type
	 *            属性类型
	 * @param name
	 *            属性名称
	 * @return
	 */
	public static String get(String type, String name) {
		String prefix = GET;
		if (type.equalsIgnoreCase(BOOLEAN)) {
			prefix = IS;
		}
		return prefix + StringUtil.toCapitalize(name);
	}
	
	/**
	 * 判断对象是否为null
	 * 
	 * @param obj
	 *            对象
	 * @return
	 */
	public static boolean isNull(Object obj) {
		return obj == null;
	}

	/**
	 * 判断对象是否不为null
	 * 
	 * @param obj
	 *            对象
	 * @return
	 */
	public static boolean isNotNull(Object obj) {
		return obj != null;
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 *            字符串
	 * @return 若字符串为null或长度为0, 则返回true, 否则返回false
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * 判断字符串是否不为空
	 * 
	 * @param str
	 *            字符串
	 * @return 若字符串不为null而且长度不为0, 则返回true, 否则返回false
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

}