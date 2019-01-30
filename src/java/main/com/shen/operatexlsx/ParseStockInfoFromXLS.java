package com.shen.operatexlsx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import com.shen.entity.StockInfo;
import com.shen.helper.Indexs;
import com.shen.helper.Util;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * get stock information from excel
 * 
 * @author heshanshan
 * 
 */
public class ParseStockInfoFromXLS {
    private String stockCode;
    static int indexFlag = 0;
    private final static Indexs indexs = Indexs.getIndexs();
    private Set<String> keySet = indexs.getChineseToInterger().keySet();

    public static void main(String[] args) {
        StockInfo stockInfo1 = new StockInfo("000088");
        ParseStockInfoFromXLS parseStockInfoXLS = new ParseStockInfoFromXLS();

        try {
            parseStockInfoXLS.parse(stockInfo1);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }

    }

    /**
     * 解析股票对应年报的xls文件
     * 
     * @param stockInfo
     * @throws FileNotFoundException
     * @throws IOException
     * @throws BiffException
     */
    public void parse(StockInfo stockInfo) throws FileNotFoundException, IOException, BiffException {
        jxl.Workbook readwb = null;
        String path = Util.FILE_PATH + File.separator + stockInfo.getStockCode() + ".xls";
        InputStream instream = new FileInputStream(path);
        readwb = Workbook.getWorkbook(instream);

        // Sheet的下标是从0开始
        // 获取第一张Sheet表
        Sheet readsheet = readwb.getSheet(0);

        // 获取Sheet表中所包含的总列数
        int columns = readsheet.getColumns();
        int rsColumns = (columns > 10) ? 10 : columns;

        // 获取Sheet表中所包含的总行数
        int rsRows = readsheet.getRows();

        // 获取指定单元格的对象引用
        for (int i = 0; i < rsRows; i++) {
            for (int j = 0; j < rsColumns; j++) {
                Cell cell = readsheet.getCell(j, i);

                // System.out.print(cell.getContents() + " ");
                String cellContent = cell.getContents();

                storeInfo(stockInfo, cellContent, j);
            }
        }
        System.out.println(stockInfo);
    }

    /**
     * get info from relative xls file and store to StockInfo
     * 
     * @param stockInfo
     * @param cellContent
     * @param rowNum
     */
    private void storeInfo(StockInfo stockInfo, String cellContent, int rowNum) {
        if (cellContent.isEmpty()) {
            cellContent = "null";
        }
        if (rowNum == 0) {
            for (String s : keySet) {
                if (s.split("\\(")[0].equals(cellContent)) {
                    indexFlag = indexs.getChineseToInterger().get(s);
                    break;
                } else {
                    indexFlag = 0;
                }
            }
        } else {
            switch (indexFlag) {
            case 1:
                stockInfo.addYears(cellContent);
                break;
            case 2:
                stockInfo.addBasicEarningsPerShare(cellContent);
                break;
            case 3:
                stockInfo.addNetProfit(cellContent);
                break;
            case 4:
                stockInfo.addNetProfitGrowthRat(cellContent);
                break;
            case 5:
                stockInfo.addOperationRevenue(cellContent);
                break;
            case 6:
                stockInfo.addOperationRevenueGrowthRat(cellContent);
                break;
            case 7:
                stockInfo.addNetAssetsPerShare(cellContent);
                break;
            case 8:
                stockInfo.addLiability(cellContent);
                break;
            case 9:
                stockInfo.addEachCapitalReserveFund(cellContent);
                break;
            case 10:
                stockInfo.addNonDistributionProfitPerShare(cellContent);
                break;
            case 11:
                stockInfo.addOperatingCashFlowPerShare(cellContent);
                break;
            case 12:
                stockInfo.addGrossProfitMargin(cellContent);
                break;
            case 13:
                stockInfo.addInventoryTurnOver(cellContent);
                break;
            default:
                break;
            }
        }
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

}
