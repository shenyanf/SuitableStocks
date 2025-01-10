package com.shen.helper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 中文和英文对应关系
 *
 * @author heshanshan
 */
public class Indexes {
    private HashMap<String, Integer> chinese2IntMap = new HashMap<>();
    private HashMap<String, String> english2ChineseMap = new HashMap<>();
    private static Indexes indexes = new Indexes();
    private List<String> sheetName = Arrays.asList("基本每股收益(元)", "净利润(万元)", "净利润同比增长率(%)", "营业总收入(万元)", "营业总收入同比增长率(%)", "每股净资产(元)", "净资产收益率(%)", "每股资本公积金(元)", "每股未分配利润(元)", "每股经营现金流(元)", "销售毛利率(%)", "存货周转率(%)");

    private Indexes() {
        chinese2IntMap.put("基本每股收益", 1);// "basicEarningsPerShare");
        chinese2IntMap.put("净利润", 2);// "netProfit");
        chinese2IntMap.put("净利润同比增长率", 3);// "netProfitGrowthRat");
        chinese2IntMap.put("营业总收入", 4);// "operationRevenue");
        chinese2IntMap.put("营业总收入同比增长率", 5);// "operationRevenueGrowthRat");
        chinese2IntMap.put("每股净资产", 6);// "netAssetsPerShare");
        chinese2IntMap.put("净资产收益率", 7);// "liability");
        chinese2IntMap.put("每股资本公积金", 8);// "eachCapitalReserveFund");
        chinese2IntMap.put("每股未分配利润", 9);// "nonDistributionProfitPerShare");
        chinese2IntMap.put("每股经营现金流", 10);// "operatingCashFlowPerShare");
        chinese2IntMap.put("销售毛利率", 11);// "grossProfitMargin");
        chinese2IntMap.put("存货周转率", 12);// "inventoryTurnOver");

        english2ChineseMap.put("basicEarningsPerShare", "基本每股收益(元)");
        english2ChineseMap.put("netProfit", "净利润(万元)");
        english2ChineseMap.put("netProfitGrowthRat", "净利润同比增长率(%)");
        english2ChineseMap.put("operationRevenue", "营业总收入(万元)");
        english2ChineseMap.put("operationRevenueGrowthRat", "营业总收入同比增长率(%)");
        english2ChineseMap.put("netAssetsPerShare", "每股净资产(元)");
        english2ChineseMap.put("liability", "净资产收益率(%)");
        english2ChineseMap.put("eachCapitalReserveFund", "每股资本公积金(元)");
        english2ChineseMap.put("nonDistributionProfitPerShare", "每股未分配利润(元)");
        english2ChineseMap.put("operatingCashFlowPerShare", "每股经营现金流(元)");
        english2ChineseMap.put("grossProfitMargin", "销售毛利率(%)");
        english2ChineseMap.put("inventoryTurnOver", "存货周转率(%)");
    }

    public String getKeyFromValueOngetEnglishWordToChinese(String value) {
        for (String name : indexes.getEnglish2ChineseMap().keySet()) {
            if (indexes.getEnglish2ChineseMap().get(name).equals(value)) {
                return name;
            }
        }
        return null;
    }

    public static Indexes getIndexes() {
        return indexes;
    }

    public HashMap<String, Integer> getChinese2IntMap() {
        return chinese2IntMap;
    }

    public HashMap<String, String> getEnglish2ChineseMap() {
        return english2ChineseMap;
    }

    public List<String> getSheetName() {
        return sheetName;
    }
}
