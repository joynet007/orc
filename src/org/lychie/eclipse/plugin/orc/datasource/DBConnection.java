package org.lychie.eclipse.plugin.orc.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import org.lychie.jutil.exception.UnexpectedException;
import org.lychie.eclipse.plugin.orc.exception.ORCPCastException;

/**
 * 数据库连接
 * 
 * @author Lychie Fan
 */
public class DBConnection {

	private static final String MYSQL_TYPE = "jdbc:mysql";
	private static final String SQLSERVER_TYPE = "jdbc:sqlserver";
	private static final String MYSQL_DRIVER_CLASS = "com.mysql.jdbc.Driver";
	private static final String SQLSERVER_DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	/**
	 * 获取数据库连接对象
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param url
	 *            连接串
	 * @return
	 */
	public static Connection getConnection(String username, String password, String url) {
		try {
			loadDriverClass(url);
			return DriverManager.getConnection(url, username, password);
		} catch (Throwable e) {
			throw new UnexpectedException(e);
		}
	}
	
	/**
	 * 载入驱动类
	 * 
	 * @param url
	 *            数据库连接串
	 * @throws Throwable
	 */
	private static void loadDriverClass(String url) throws Throwable {
		if (url.startsWith(MYSQL_TYPE)) {
			Class.forName(MYSQL_DRIVER_CLASS);
		} else if (url.startsWith(SQLSERVER_TYPE)) {
			Class.forName(SQLSERVER_DRIVER_CLASS);
		} else {
			throw new ORCPCastException("unsupported url : " + url);
		}
	}
	
}