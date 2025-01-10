package com.shen.test;

import com.shen.trade.AllTrades;
import com.shen.trade.TradeStocks;
import com.shen.entity.StockInfo;
import com.shen.helper.Util;
import com.shen.xlsx.DownloadStockXLS;
import com.shen.xlsx.ParseStockInfoFromXLS;
import com.shen.report.CreateReport;
import jxl.read.biff.BiffException;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JustTest {

    @Test
    public void createReport() {
        CreateReport createReport = new CreateReport();
        ParseStockInfoFromXLS parseStockInfoXLS = new ParseStockInfoFromXLS();

        TradeStocks stocks = new TradeStocks();
        stocks.getstockBasicInfo().put("600066", "宇通客车");

        try {
            StockInfo stockInfo = parseStockInfoXLS.parse("600066");
            List<StockInfo> list = new ArrayList<StockInfo>();
            list.add(stockInfo);
            createReport.writeExcel(list, stocks);
        } catch (BiffException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void allTrade() {
        AllTrades allTrades = new AllTrades();
        HashMap<String, String> tradeShortAndName = allTrades.getTradeShortAndName();
        System.out.println(tradeShortAndName);
    }

    @Test
    public void downloadXLS() {
        DownloadStockXLS downLoadStockInfo = new DownloadStockXLS();

        downLoadStockInfo.downloadXLS("300137");
    }

    @Test
    public void tradeStocks() {
        try {
            TradeStocks stocks = new TradeStocks("银行");
            List<String> stockCodeAndName = stocks.getStockCodeAndName();
            System.out.println(stockCodeAndName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getData() {
        String url = "http://q.10jqka.com.cn/thshy/detail/field/199112/order/desc/page/1/ajax/1/code/881155";
        String basicUrl = "http://www.10jqka.com.cn/";
        Util.getData(url);
    }

    @Test
    public void getStockInfoFromXLS() {
        ParseStockInfoFromXLS parseStockInfoXLS = new ParseStockInfoFromXLS();

        try {
            StockInfo stockInfo = parseStockInfoXLS.parse("600066");
            System.out.println(stockInfo);
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }
    }
}
