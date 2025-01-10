package com.shen.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.shen.helper.Indexes;

/**
 * 股票的历年的权益信息，从年报中获取
 *
 * @author heshanshan
 */
public class StockInfo {
    private String stockCode;
    private String stockName;
    private final static Indexes INDEXES = Indexes.getIndexes();
    private final HashMap<String, List<ContentItem>> infos = new HashMap<>();
    /**
     * 基本每股收益
     */
    private final List<ContentItem> basicEarningsPerShare = new ArrayList<>();
    /**
     * 净利润
     */
    private final List<ContentItem> netProfit = new ArrayList<>();
    /**
     * 净利润同比增长率
     */
    private final List<ContentItem> netProfitGrowthRat = new ArrayList<>();
    /**
     * 营业总收入
     */
    private final List<ContentItem> operationRevenue = new ArrayList<>();
    /**
     * 营业总收入同比增长率
     */
    private final List<ContentItem> operationRevenueGrowthRat = new ArrayList<>();
    /**
     * 每股净资产
     */
    private final List<ContentItem> netAssetsPerShare = new ArrayList<>();
    /**
     * 资产负债比率
     */
    private final List<ContentItem> liability = new ArrayList<>();
    /**
     * 每股公积金
     */
    private final List<ContentItem> eachCapitalReserveFund = new ArrayList<>();
    /**
     * 每股未分配利润
     */
    private final List<ContentItem> nonDistributionProfitPerShare = new ArrayList<>();
    /**
     * 每股经营现金流
     */
    private final List<ContentItem> operatingCashFlowPerShare = new ArrayList<>();
    /**
     * 毛利率
     */
    private final List<ContentItem> grossProfitMargin = new ArrayList<>();
    /**
     * 库存周转率
     */
    private final List<ContentItem> inventoryTurnOver = new ArrayList<>();


    public StockInfo(String stockCode) {
        this.setStockCode(stockCode);

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
        StringBuilder sb = new StringBuilder();
        for (Entry<String, List<ContentItem>> entry : infos.entrySet()) {
            Object key = INDEXES.getEnglish2ChineseMap().get(entry.getKey());
            Object val = entry.getValue();
            sb.append(key).append(" ");
            sb.append(val).append("\n");
        }
        return sb.toString();
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public List<ContentItem> getBasicEarningsPerShare() {
        return basicEarningsPerShare;
    }

    public void addBasicEarningsPerShare(ContentItem s) {
        basicEarningsPerShare.add(s);
    }

    public List<ContentItem> getNetProfit() {
        return netProfit;
    }

    public void addNetProfit(ContentItem s) {
        netProfit.add(s);
    }

    public List<ContentItem> getNetProfitGrowthRat() {
        return netProfitGrowthRat;
    }

    public void addNetProfitGrowthRat(ContentItem s) {
        netProfitGrowthRat.add(s);
    }

    public List<ContentItem> getOperationRevenue() {
        return operationRevenue;
    }

    public void addOperationRevenue(ContentItem s) {
        operationRevenue.add(s);
    }

    public List<ContentItem> getOperationRevenueGrowthRat() {
        return operationRevenueGrowthRat;
    }

    public void addOperationRevenueGrowthRat(ContentItem s) {
        operationRevenueGrowthRat.add(s);
    }

    public List<ContentItem> getNetAssetsPerShare() {
        return netAssetsPerShare;
    }

    public void addNetAssetsPerShare(ContentItem s) {
        netAssetsPerShare.add(s);
    }

    public List<ContentItem> getLiability() {
        return liability;
    }

    public void addLiability(ContentItem s) {
        liability.add(s);
    }

    public List<ContentItem> getEachCapitalReserveFund() {
        return eachCapitalReserveFund;
    }

    public void addEachCapitalReserveFund(ContentItem s) {
        eachCapitalReserveFund.add(s);
    }

    public List<ContentItem> getNonDistributionProfitPerShare() {
        return nonDistributionProfitPerShare;
    }

    public void addNonDistributionProfitPerShare(ContentItem s) {
        nonDistributionProfitPerShare.add(s);
    }

    public List<ContentItem> getOperatingCashFlowPerShare() {
        return operatingCashFlowPerShare;
    }

    public void addOperatingCashFlowPerShare(ContentItem s) {
        operatingCashFlowPerShare.add(s);
    }

    public List<ContentItem> getGrossProfitMargin() {
        return grossProfitMargin;
    }

    public void addGrossProfitMargin(ContentItem s) {
        grossProfitMargin.add(s);
    }

    public List<ContentItem> getInventoryTurnOver() {
        return inventoryTurnOver;
    }

    public void addInventoryTurnOver(ContentItem s) {
        inventoryTurnOver.add(s);
    }

    public HashMap<String, List<ContentItem>> getInfos() {
        return infos;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }
}

