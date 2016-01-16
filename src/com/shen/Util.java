package com.shen;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Util {
	public static String filePath = "d:\\stocks";

	public Util() {
	}

	public static String getStockName1(String stockCode) {
		String uri = "http://stock.quote.stockstar.com/" + stockCode + ".shtml";
		Pattern pattern = Pattern.compile("(.*)(\\(" + stockCode + ")", Pattern.DOTALL);
		Matcher matcher = null;
		String name = null;

		try {
			/* get json type date */
			Document doc = Jsoup.connect(uri).get();
			/* get title like: <title>大秦铁路(601006) _ 股票行情 _ 东方财富网</title> */
			Elements s = doc.getElementsByTag("title");
			// System.out.println(s.text());

			matcher = pattern.matcher(s.text());
			if (matcher.find()) {
				name = matcher.group(1);
				if (checkChinese(name)) {
					System.out.println(name);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return name;
	}

	public static String getStockName(String stockCode) {
		String stockName = AllStocks.allStockCodeAndName.get(stockCode);

		if (stockName == null) {
			return null;
		} else if (checkChinese(stockName)) {
			return stockName;
		}
		return null;
	}

	/**
	 * check sourceString is Chinese or not
	 * 
	 * @param sourceString
	 * @return
	 */
	public static boolean checkChinese(String sourceString) {
		/*
		 * String regEx = "[\\u4e00-\\u9fa5]"; Pattern p = Pattern.compile(regEx); Matcher m = p.matcher(sourceString);
		 * int count = 0; while (m.find()) { for (int i = 0; i <= m.groupCount(); i++) { count = count + 1; } } return
		 * count > 0;
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
