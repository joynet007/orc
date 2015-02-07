package org.lychie.eclipse.plugin.orc.util;

import java.io.File;
import java.util.LinkedList;
import org.lychie.jutil.StringUtil;

/**
 * 文件队列
 * 
 * @author Lychie Fan
 */
public class FileQueue {

	private static LinkedList<Entry> entries;

	/**
	 * 初始化队列
	 */
	public static void init() {
		entries = new LinkedList<Entry>();
	}

	/**
	 * 获取队列条目
	 * 
	 * @return
	 */
	public static LinkedList<Entry> getEntries() {
		return entries;
	}

	/**
	 * 添加条目到队列
	 * 
	 * @param entry
	 *            条目
	 */
	public static void add(Entry entry) {
		entries.add(entry);
	}

	/**
	 * 队列大小
	 * 
	 * @return
	 */
	public static int size() {
		return entries.size();
	}

	/**
	 * 条目
	 * 
	 * @author Lychie Fan
	 */
	public static class Entry {

		private File file;
		private String content;
		private String message;
		private static final String MESSAGE = "Creating ? file . . .";

		public Entry(String content, File file) {
			this.file = file;
			this.content = content;
			this.message = StringUtil.format(MESSAGE, file.getName());
		}

		public File getFile() {
			return file;
		}

		public String getContent() {
			return content;
		}

		public String getMessage() {
			return message;
		}

	}

}