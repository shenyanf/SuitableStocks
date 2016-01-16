package com.shen;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import jxl.read.biff.BiffException;

public class Main {

	public static void main(String[] args) {
		/*
		 * "白色家电 半导体及元件 包装印刷 保险及其他 采掘服务 传媒 电力 电气设备 电子制造 房地产开发 纺织制造 非汽车交运 服装家纺 钢铁 港口航运 公交 公路铁路运输 光学光电子 国防军工 化工合成材料 化工新材料
		 * 化学制品 化学制药 环保工程 机场航运 基础化学 计算机设备 计算机应用 家用轻工 建筑材料 建筑装饰 交运设备服务 景点及旅游 酒店及餐饮 零售 贸易 煤炭开采 农产品加工 农业服务 其他电子 汽车零部件 汽车整车
		 * 燃气水务 生物制品 石油矿业开采 食品加工制造 视听器材 通信服务 通信设备 通用设备 物流 新材料 养殖业 医疗器械服务 医药商业 仪器仪表 银行 饮料制造 有色冶炼加工 园区开发 造纸 证券 中药 种植业与林业
		 * 专用设备 综合" ;
		 */
		Stocks stocks = new Stocks("物流");
		try {
			stocks.getStockCodeAndName();
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		DownLoadStockXLS downLoadStockInfo = new DownLoadStockXLS();
		CreateReport createReport = new CreateReport();
		ParseStockInfoFromXLS parseStockInfoXLS = new ParseStockInfoFromXLS();
		List<StockInfo> stockinfos = new ArrayList<StockInfo>();

		for (String stockCode : stocks.getstockBasicInfo().keySet()) {
			StockInfo stockInfo = new StockInfo(stockCode);
			stockinfos.add(stockInfo);

			if (!downLoadStockInfo.downdLoadXLS(stockInfo)) {
				System.out.println("down load xls about " + stockInfo.getStockCode() + " failed");
			}

			try {
				parseStockInfoXLS.parse(stockInfo);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (BiffException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		createReport.writeExcel(stockinfos, stocks);
	}
}
