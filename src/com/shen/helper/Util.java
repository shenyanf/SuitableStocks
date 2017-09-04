package com.shen.helper;

public class Util {
	public static String filePath = "d:\\stocks";

	public Util() {
	}

	/**
	 * check sourceString is Chinese or not
	 * 
	 * @param sourceString
	 * @return
	 */
	public static boolean checkChinese(String sourceString) {
		/*
		 * String regEx = "[\\u4e00-\\u9fa5]"; Pattern p =
		 * Pattern.compile(regEx); Matcher m = p.matcher(sourceString); int
		 * count = 0; while (m.find()) { for (int i = 0; i <= m.groupCount();
		 * i++) { count = count + 1; } } return count > 0;
		 */

		return sourceString.getBytes().length == sourceString.length() ? false : true;
	}

	public static String getFilePath() {
		return filePath;
	}

	public static void setFilePath(String filePath) {
		Util.filePath = filePath;
	}
}
