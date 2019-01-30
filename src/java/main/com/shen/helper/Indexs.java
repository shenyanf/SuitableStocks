package com.shen.helper;

import java.util.HashMap;

/**
 * 中文和英文对应关系
 * 
 * @author heshanshan
 * 
 */
public class Indexs {
    private HashMap<String, Integer> chineseToInterger = new HashMap<String, Integer>();
    private HashMap<String, String> englishWordToChinese = new HashMap<String, String>();
    private static Indexs indexs = new Indexs();

    public static void main(String[] args) {
        System.out.println(indexs.getEnglishWordToChinese().values());
    }

    private Indexs() {
        chineseToInterger.put("科目\\时间(年)", 1);// "years");
        chineseToInterger.put("基本每股收益(元)", 2);// "basicEarningsPerShare");
        chineseToInterger.put("净利润(万元)", 3);// "netProfit");
        chineseToInterger.put("净利润同比增长率(%)", 4);// "netProfitGrowthRat");
        chineseToInterger.put("营业总收入(万元)", 5);// "operationRevenue");
        chineseToInterger.put("营业总收入同比增长率(%)", 6);// "operationRevenueGrowthRat");
        chineseToInterger.put("每股净资产(元)", 7);// "netAssetsPerShare");
        chineseToInterger.put("净资产收益率(%)", 8);// "liability");
        chineseToInterger.put("每股资本公积金(元)积金", 9);// "eachCapitalReserveFund");
        chineseToInterger.put("每股未分配利润(元)", 10);// "nonDistributionProfitPerShare");
        chineseToInterger.put("每股经营现金流(元)", 11);// "operatingCashFlowPerShare");
        chineseToInterger.put("销售毛利率(%)", 12);// "grossProfitMargin");
        chineseToInterger.put("存货周转率(%)", 13);// "inventoryTurnOver");

        englishWordToChinese.put("years", "科目\\时间(年)");
        englishWordToChinese.put("basicEarningsPerShare", "基本每股收益(元)");
        englishWordToChinese.put("netProfit", "净利润(万元)");
        englishWordToChinese.put("netProfitGrowthRat", "净利润同比增长率(%)");
        englishWordToChinese.put("operationRevenue", "营业总收入(万元)");
        englishWordToChinese.put("operationRevenueGrowthRat", "营业总收入同比增长率(%)");
        englishWordToChinese.put("netAssetsPerShare", "每股净资产(元)");
        englishWordToChinese.put("liability", "净资产收益率(%)");
        englishWordToChinese.put("eachCapitalReserveFund", "每股资本公积金(元)积金");
        englishWordToChinese.put("nonDistributionProfitPerShare", "每股未分配利润(元)");
        englishWordToChinese.put("operatingCashFlowPerShare", "每股经营现金流(元)");
        englishWordToChinese.put("grossProfitMargin", "销售毛利率(%)");
        englishWordToChinese.put("inventoryTurnOver", "存货周转率(%)");
    }

    public String getKeyFromValueOngetEnglishWordToChinese(String value) {
        for (String name : indexs.getEnglishWordToChinese().keySet()) {
            if (indexs.getEnglishWordToChinese().get(name).equals(value)) {
                return name;
            }
        }
        return null;
    }

    public static Indexs getIndexs() {
        return indexs;
    }

    public HashMap<String, Integer> getChineseToInterger() {
        return chineseToInterger;
    }

    public void setChineseToInterger(HashMap<String, Integer> chineseToInterger) {
        this.chineseToInterger = chineseToInterger;
    }

    public HashMap<String, String> getEnglishWordToChinese() {
        return englishWordToChinese;
    }

    public void setEnglishWordToChinese(HashMap<String, String> englishWordToChinese) {
        this.englishWordToChinese = englishWordToChinese;
    }
}
