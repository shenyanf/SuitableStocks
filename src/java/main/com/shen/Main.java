package com.shen;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.shen.entity.StockInfo;
import com.shen.operatexlsx.DownLoadStockXLS;
import com.shen.operatexlsx.ParseStockInfoFromXLS;
import com.shen.report.CreateReport;

import jxl.read.biff.BiffException;

public class Main {

	public static void main(String[] args) {
		/*
		 * 半导体及元件,白色家电,保险及其他,包装印刷,采掘服务,传媒,电力,电气设备,电子制造,房地产开发,非汽车交运,服装家纺,纺织制造,
		 * 国防军工,公交,港口航运,公路铁路运输,钢铁,光学光电子,环保工程,化工合成材料,化工新材料,化学制品,化学制药,基础化学,机场航运,
		 * 酒店及餐饮,景点及旅游,计算机设备,计算机应用,家用轻工,交运设备服务,建筑材料,建筑装饰,零售,煤炭开采,贸易,农产品加工,农业服务,
		 * 汽车零部件,汽车整车,其他电子,燃气水务,食品加工制造,视听器材,生物制品,石油矿业开采,通信服务,通信设备,通用设备,物流,新材料,
		 * 医疗器械服务,饮料制造,园区开发,仪器仪表,有色冶炼加工,银行,医药商业,养殖业,综合,证券,中药,专用设备,造纸,种植业与林业
		 */
		TradeStocks stocks = new TradeStocks("银行");
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
