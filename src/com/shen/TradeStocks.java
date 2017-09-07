package com.shen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.shen.helper.Util;

/**
 * 获取指定行业下的所有公司
 * 
 * @author heshanshan
 * 
 */
public class TradeStocks {
	/* basic info is code and name */
	private HashMap<String, String> stockBasicInfo = new HashMap<String, String>();

	private String tradeName;

	public static void main(String[] args) {
		TradeStocks stocks = new TradeStocks("零售");
		try {
			stocks.getStockCodeAndName();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public TradeStocks() {
		stockBasicInfo.put("300152", "燃控科技");
		/*
		 * stockBasicInfo.put("300334", "津膜科技"); stockBasicInfo.put("300125",
		 * "易世达"); stockBasicInfo.put("300332", "天壕节能 ");
		 * stockBasicInfo.put("300335", "迪森股份"); stockBasicInfo.put("603126",
		 * "中材节能"); stockBasicInfo.put("300137", "先河环保");
		 * stockBasicInfo.put("603588", "高能环境"); stockBasicInfo.put("002341",
		 * "新纶科技"); stockBasicInfo.put("002573", "清新环境");
		 * stockBasicInfo.put("000939", "凯迪电力"); stockBasicInfo.put("300187",
		 * "永清环保"); stockBasicInfo.put("600292", "中电远达");
		 * stockBasicInfo.put("300090", "盛运环保"); stockBasicInfo.put("000035",
		 * "中国天楹"); stockBasicInfo.put("300172", "中电环保");
		 * stockBasicInfo.put("300422", "博世科"); stockBasicInfo.put("000826",
		 * "桑德环境"); stockBasicInfo.put("300055", "万邦达");
		 * stockBasicInfo.put("002672", "东江环保"); stockBasicInfo.put("300156",
		 * "神雾环保"); stockBasicInfo.put("603568", "伟明环保");
		 * stockBasicInfo.put("300425", "环能科技"); stockBasicInfo.put("600217",
		 * "*ST秦岭"); stockBasicInfo.put("300495", "美尚生态");
		 * stockBasicInfo.put("300262", "巴安水务"); stockBasicInfo.put("300203",
		 * "聚光科技"); stockBasicInfo.put("300190", "维尔利");
		 * stockBasicInfo.put("300070", "碧水源"); stockBasicInfo.put("300056",
		 * "三维丝");
		 */
	}

	public TradeStocks(String tradeName) {
		// if (getStockCodeAndName(tradeName) != null) {
		// System.out.println("Get stock code and name about tade " + tradeName
		// + "successful");
		// }
		if (Util.checkChinese(tradeName)) {
			this.tradeName = AllTrades.tradeShortAndName.get(tradeName);
		} else {
			this.tradeName = tradeName;
		}
	}

	public List<String> getStockCodeAndName() throws JSONException {
		String body = null;
		List<String> stockCodes = new ArrayList<String>();
		List<String> stockNames = new ArrayList<String>();

		/* if tradeName is null return null */
		if (tradeName == null) {
			return null;
		}

		/* get stockcode in circle */
		for (int i = 1;; i++) {
			String url = "http://q.10jqka.com.cn/thshy/detail/field/199112/order/desc/page/" + i + "/ajax/1/code/"
					+ tradeName;

			try {
				/* bs4 解析html */
				body = Jsoup.connect(url).ignoreContentType(true).execute().body();

				// 如果包含这个信息，就说明翻页到头了
				if (body.contains("暂无成份股数据")) {
					break;
				}

				Document doc = Jsoup.parse(body, "UTF-8");
				// baseUri 参数用于解决文件中URLs是相对路径的问题。如果不需要可以传入一个空的字符串。
				doc.setBaseUri("http://q.10jqka.com.cn");

				Elements links = doc.select("td a[target=_blank]");
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

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < stockCodes.size(); i++) {
			String code = stockCodes.get(i);
			stockBasicInfo.put(code, stockNames.get(i));
		}
		System.out.println(stockBasicInfo);

		return stockCodes;
	}

	public HashMap<String, String> getstockBasicInfo() {
		return stockBasicInfo;
	}

	public void setstockBasicInfo(HashMap<String, String> stockBasicInfo) {
		this.stockBasicInfo = stockBasicInfo;
	}

	public String getTradeName() {
		return tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

}
