package com.shen.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.shen.helper.Indexs;

/**
 * 股票的历年的权益信息，从年报中获取
 * 
 * @author heshanshan
 * 
 */
public class StockInfo {
    private String stockCode;
    private String stockName;
    private final static Indexs indexs = Indexs.getIndexs();
    private List<String> years = new ArrayList<String>();
    private List<String> basicEarningsPerShare = new ArrayList<String>();// 基本每股收益
    private List<String> netProfit = new ArrayList<String>();// 净利润
    private List<String> netProfitGrowthRat = new ArrayList<String>();// 净利润同比增长率
    private List<String> operationRevenue = new ArrayList<String>();// 营业总收入
    private List<String> operationRevenueGrowthRat = new ArrayList<String>();// 营业总收入同比增长率
    private List<String> netAssetsPerShare = new ArrayList<String>();// 每股净资产
    private List<String> liability = new ArrayList<String>();// 资产负债比率
    private List<String> eachCapitalReserveFund = new ArrayList<String>();// 每股公积金
    private List<String> nonDistributionProfitPerShare = new ArrayList<String>();// 每股未分配利润
    private List<String> operatingCashFlowPerShare = new ArrayList<String>();// 每股经营现金流
    private List<String> grossProfitMargin = new ArrayList<String>();// 毛利率
    private List<String> inventoryTurnOver = new ArrayList<String>();// 库存周转率
    private HashMap<String, List<String>> infos = new HashMap<String, List<String>>();

    public StockInfo(String stockCode) {
        this.setStockCode(stockCode);

        infos.put("years", years);
        infos.put("basicEarningsPerShare", basicEarningsPerShare);
        infos.put("netProfit", netProfit);
        infos.put("netProfitGrowthRat", netProfitGrowthRat);
        infos.put("operationRevenue", operationRevenue);
        infos.put("operationRevenueGrowthRat", operationRevenueGrowthRat);
        infos.put("netAssetsPerShare", netAssetsPerShare);
        infos.put("liability", liability);
        infos.put("eachCapitalReserveFund", eachCapitalReserveFund);
        infos.put("nonDistributionProfitPerShare", nonDistributionProfitPerShare);
        infos.put("operatingCashFlowPerShare", operatingCashFlowPerShare);
        infos.put("grossProfitMargin", grossProfitMargin);
        infos.put("inventoryTurnOver", inventoryTurnOver);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        Iterator<Entry<String, List<String>>> iter = infos.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, List<String>> entry = iter.next();
            Object key = indexs.getEnglishWordToChinese().get(entry.getKey());
            Object val = entry.getValue();
            sb.append(key + " ");
            sb.append(val + "\n");
        }
        return sb.toString();
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public List<String> getBasicEarningsPerShare() {
        return basicEarningsPerShare;
    }

    public void setBasicEarningsPerShare(List<String> basicEarningsPerShare) {
        this.basicEarningsPerShare = basicEarningsPerShare;
    }

    public void addBasicEarningsPerShare(String s) {
        basicEarningsPerShare.add(s);
    }

    public List<String> getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(List<String> netProfit) {
        this.netProfit = netProfit;
    }

    public void addNetProfit(String s) {
        netProfit.add(s);
    }

    public List<String> getNetProfitGrowthRat() {
        return netProfitGrowthRat;
    }

    public void setNetProfitGrowthRat(List<String> netProfitGrowthRat) {
        this.netProfitGrowthRat = netProfitGrowthRat;
    }

    public void addNetProfitGrowthRat(String s) {
        netProfitGrowthRat.add(s);
    }

    public List<String> getOperationRevenue() {
        return operationRevenue;
    }

    public void setOperationRevenue(List<String> operationRevenue) {
        this.operationRevenue = operationRevenue;
    }

    public void addOperationRevenue(String s) {
        operationRevenue.add(s);
    }

    public List<String> getOperationRevenueGrowthRat() {
        return operationRevenueGrowthRat;
    }

    public void setOperationRevenueGrowthRat(List<String> operationRevenueGrowthRat) {
        this.operationRevenueGrowthRat = operationRevenueGrowthRat;
    }

    public void addOperationRevenueGrowthRat(String s) {
        operationRevenueGrowthRat.add(s);
    }

    public List<String> getNetAssetsPerShare() {
        return netAssetsPerShare;
    }

    public void setNetAssetsPerShare(List<String> netAssetsPerShare) {
        this.netAssetsPerShare = netAssetsPerShare;
    }

    public void addNetAssetsPerShare(String s) {
        netAssetsPerShare.add(s);
    }

    public List<String> getLiability() {
        return liability;
    }

    public void setLiability(List<String> liability) {
        this.liability = liability;
    }

    public void addLiability(String s) {
        liability.add(s);
    }

    public List<String> getEachCapitalReserveFund() {
        return eachCapitalReserveFund;
    }

    public void setEachCapitalReserveFund(List<String> eachCapitalReserveFund) {
        this.eachCapitalReserveFund = eachCapitalReserveFund;
    }

    public void addEachCapitalReserveFund(String s) {
        eachCapitalReserveFund.add(s);
    }

    public List<String> getNonDistributionProfitPerShare() {
        return nonDistributionProfitPerShare;
    }

    public void setNonDistributionProfitPerShare(List<String> nonDistributionProfitPerShare) {
        this.nonDistributionProfitPerShare = nonDistributionProfitPerShare;
    }

    public void addNonDistributionProfitPerShare(String s) {
        nonDistributionProfitPerShare.add(s);
    }

    public List<String> getOperatingCashFlowPerShare() {
        return operatingCashFlowPerShare;
    }

    public void setOperatingCashFlowPerShare(List<String> operatingCashFlowPerShare) {
        this.operatingCashFlowPerShare = operatingCashFlowPerShare;
    }

    public void addOperatingCashFlowPerShare(String s) {
        operatingCashFlowPerShare.add(s);
    }

    public List<String> getGrossProfitMargin() {
        return grossProfitMargin;
    }

    public void setGrossProfitMargin(List<String> grossProfitMargin) {
        this.grossProfitMargin = grossProfitMargin;
    }

    public void addGrossProfitMargin(String s) {
        grossProfitMargin.add(s);
    }

    public List<String> getInventoryTurnOver() {
        return inventoryTurnOver;
    }

    public void setInventoryTurnOver(List<String> inventoryTurnOver) {
        this.inventoryTurnOver = inventoryTurnOver;
    }

    public void addInventoryTurnOver(String s) {
        inventoryTurnOver.add(s);
    }

    public List<String> getYears() {
        return years;
    }

    public void setYears(List<String> years) {
        this.years = years;
    }

    public void addYears(String s) {
        years.add(s);
    }

    public HashMap<String, List<String>> getInfos() {
        return infos;
    }

    public void setInfos(HashMap<String, List<String>> infos) {
        this.infos = infos;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

}