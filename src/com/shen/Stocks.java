package com.shen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

/**
 * 选择多只股票，可以通过设置行业来选择，也可以手动设置股票
 * 
 * @author heshanshan
 * 
 */
public class Stocks extends AllStocks {
	/* basic info is code and name */
	private HashMap<String, String> stockBasicInfo = new HashMap<String, String>();
	private String tradeName;

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Stocks stocks = new Stocks("hbgc");
		// System.out.println(stocks.stockBasicInfo);
		System.out.println(Util.getStockName("002672"));
	}

	public Stocks() {
		stockBasicInfo.put("300152", "燃控科技");
		/*
		 * stockBasicInfo.put("300334", "津膜科技"); stockBasicInfo.put("300125", "易世达"); stockBasicInfo.put("300332",
		 * "天壕节能 "); stockBasicInfo.put("300335", "迪森股份"); stockBasicInfo.put("603126", "中材节能");
		 * stockBasicInfo.put("300137", "先河环保"); stockBasicInfo.put("603588", "高能环境"); stockBasicInfo.put("002341",
		 * "新纶科技"); stockBasicInfo.put("002573", "清新环境"); stockBasicInfo.put("000939", "凯迪电力");
		 * stockBasicInfo.put("300187", "永清环保"); stockBasicInfo.put("600292", "中电远达"); stockBasicInfo.put("300090",
		 * "盛运环保"); stockBasicInfo.put("000035", "中国天楹"); stockBasicInfo.put("300172", "中电环保");
		 * stockBasicInfo.put("300422", "博世科"); stockBasicInfo.put("000826", "桑德环境"); stockBasicInfo.put("300055",
		 * "万邦达"); stockBasicInfo.put("002672", "东江环保"); stockBasicInfo.put("300156", "神雾环保");
		 * stockBasicInfo.put("603568", "伟明环保"); stockBasicInfo.put("300425", "环能科技"); stockBasicInfo.put("600217",
		 * "*ST秦岭"); stockBasicInfo.put("300495", "美尚生态"); stockBasicInfo.put("300262", "巴安水务");
		 * stockBasicInfo.put("300203", "聚光科技"); stockBasicInfo.put("300190", "维尔利"); stockBasicInfo.put("300070",
		 * "碧水源"); stockBasicInfo.put("300056", "三维丝");
		 */
	}

	Stocks(String tradeName) {
		// if (getStockCodeAndName(tradeName) != null) {
		// System.out.println("Get stock code and name about tade " + tradeName + "successful");
		// }
		if (Util.checkChinese(tradeName)) {
			this.tradeName = AllTrades.tradeShortAndName.get(tradeName);
		} else {
			this.tradeName = tradeName;
		}
	}

	/**
	 * get stock code and name about trade
	 * 
	 * @return
	 * @throws JSONException
	 */
	public Boolean getStockCodeAndName() throws JSONException {
		List<String> stockCode = getStockCode();
		String name = null;

		if (tradeName == null || stockCode == null) {
			return Boolean.FALSE;
		}

		for (String code : stockCode) {
			name = Util.getStockName(code);
			if (name != null) {
				stockBasicInfo.put(code, name);
			}
		}
		return Boolean.TRUE;
	}

	private List<String> getStockCode() throws JSONException {
		String body = null;
		List<String> stockCodes = new ArrayList<String>();
		JSONObject jsonObject = null;
		JSONArray menu = null;

		/* if tradeName is null return null */
		if (tradeName == null) {
			return null;
		}

		/* get stockcode in circle */
		for (int i = 1;; i++) {
			String url = "http://q.10jqka.com.cn/interface/stock/detail/zdf/desc/" + i + "/1/" + tradeName;

			try {
				/* get json type date */
				body = Jsoup.connect(url).ignoreContentType(true).execute().body();
				jsonObject = new JSONObject(body);

				/* transfer to jsonarray */
				menu = jsonObject.getJSONArray("data");

				/* if jsonarray is null than break */
				if (menu.length() <= 0) {
					break;
				}

				/* get stock code */
				for (int j = 0; j < menu.length(); j++) {
					String s = (String) menu.getJSONObject(j).get("stockcode");
					stockCodes.add(s);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
