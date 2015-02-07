package org.lychie.eclipse.plugin.orc.datasource;

import java.util.List;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.util.ArrayList;
import java.sql.DatabaseMetaData;
import java.sql.ResultSetMetaData;
import org.lychie.jutil.StringUtil;
import org.lychie.jutil.exception.UnexpectedException;
import org.lychie.eclipse.plugin.orc.datasource.model.Table;
import org.lychie.eclipse.plugin.orc.datasource.model.Column;

/**
 * 数据库元数据
 * 
 * @author Lychie Fan
 */
public class DBMetaData {

	private Connection conn;
	private DatabaseMetaData metaData;
	private static final String TABLE = "TABLE";
	private static final String TABLE_NAME = "TABLE_NAME";

	public DBMetaData(Connection conn) {
		try {
			this.conn = conn;
			this.metaData = conn.getMetaData();
		} catch (Throwable e) {
			throw new UnexpectedException(e);
		}
	}

	/**
	 * 获取数据库所有的表
	 * 
	 * @return
	 */
	public List<Table> getTables() {
		ResultSet rs = null;
		try {
			List<Table> tables = new ArrayList<Table>();
			rs = metaData.getTables(null, "%", "%", new String[] { TABLE });
			while (rs.next()) {
				String tablename = rs.getString(TABLE_NAME);
				tables.add(new Table(tablename, getColumns(tablename)));
			}
			return tables;
		} catch (Throwable e) {
			throw new UnexpectedException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Throwable e) {}
		}
	}

	/**
	 * 获取表的所有列
	 * 
	 * @param tablename
	 *            表名
	 * @return
	 * @throws Throwable
	 */
	private List<Column> getColumns(String tablename) throws Throwable {
		ResultSet rs = null;
		try {
			List<Column> columns = new ArrayList<Column>();
			Statement statement = conn.createStatement();
			rs = statement.executeQuery("SELECT * FROM " + tablename);
			ResultSetMetaData rsmd = rs.getMetaData();
			int size = rsmd.getColumnCount();
			for (int i = 1; i <= size; ++i) {
				columns.add(new Column(rsmd.getColumnName(i),
						getColumnClassName(rsmd.getColumnClassName(i))));
			}
			return columns;
		} catch (Throwable e) {
			throw e;
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}
	
	/**
	 * 获取列的数据类型
	 * 
	 * @param name
	 *            数据类型名称
	 * @return
	 */
	private static String getColumnClassName(String name) {
		if (name.equals("java.sql.Date") || name.equals("java.sql.Time")
				|| name.equals("java.sql.Timestamp")) {
			return "java.util.Date";
		} else {
			return StringUtil.afterLastString(name, ".");
		}
	}

}