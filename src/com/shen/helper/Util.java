package com.shen.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public final class Util {
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

	public static Map<String, String> loadAllStockCodeAndName() {
		String body = null;
		List<String> stockCodes = new ArrayList<String>();
		List<String> stockNames = new ArrayList<String>();
		Map<String, String> stockBasicInfo = new HashMap<String, String>();

		/* get stockcode in circle */
		for (int i = 1;; i++) {
			String url = "http://q.10jqka.com.cn/index/index/board/all/field/zdf/order/desc/page/" + i + "/ajax/1/";

			try {
				/* bs4 解析html */
				body = Jsoup.connect(url).ignoreContentType(true).execute().body();

				Document doc = Jsoup.parse(body, "UTF-8");
				// baseUri 参数用于解决文件中URLs是相对路径的问题。如果不需要可以传入一个空的字符串。
				doc.setBaseUri("http://q.10jqka.com.cn");

				Elements links = doc.select("td a[target=_blank]");

				// 如果tbody没有内容，就推出
				if (links.size() == 0) {
					System.out.println("breakkkkkkkkkkk");
					break;
				}

				for (Element link : links) {
					// String linkHref = link.attr("href");
					String linkText = link.text();
					// 如果是编码就添加到list中
					if (linkText.matches("\\d*")) {
						stockCodes.add(linkText);
					} else {
						stockNames.add(linkText);
					}
				}
				// 单位是mills
				Thread.sleep(500);

			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < stockCodes.size(); i++) {
			stockBasicInfo.put(stockCodes.get(i), stockNames.get(i));
		}
		System.out.println(stockBasicInfo);
		System.out.println(stockBasicInfo.size());

		return stockBasicInfo;
	}

	public static void setFilePath(String filePath) {
		Util.filePath = filePath;
	}

	public static void main(String[] args) {
		Util.loadAllStockCodeAndName();
	}
}
