package com.shen.xlsx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

import com.shen.Const;
import com.shen.entity.ContentItem;
import com.shen.entity.StockInfo;
import com.shen.helper.Indexes;
import com.shen.helper.Util;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * get stock information from excel
 *
 * @author heshanshan
 */
public class ParseStockInfoFromXLS {
    private final static Indexes INDEXES = Indexes.getIndexes();

    /**
     * 解析股票对应年报的xls文件
     *
     * @param stockInfo
     * @throws FileNotFoundException
     * @throws IOException
     * @throws BiffException
     */
    public void parse(StockInfo stockInfo) throws FileNotFoundException, IOException, BiffException {
        jxl.Workbook readwb;
        String path = Util.getFilePath() + File.separator + stockInfo.getStockCode() + ".xls";
        InputStream instream = new FileInputStream(path);
        readwb = Workbook.getWorkbook(instream);

        // Sheet的下标是从0开始
        // 获取第一张Sheet表
        Sheet readsheet = readwb.getSheet(0);

        // 获取Sheet表中所包含的总列数
        int columns = readsheet.getColumns();
        int rsColumns = Math.min(columns, 10);

        // 获取Sheet表中所包含的总行数
        int rsRows = readsheet.getRows();
        HashSet<String> yearRangeSet = new HashSet<>(Const.PROCESS_YEAR_RANGE);

        // 获取指定单元格的对象引用
        for (int row = 1; row < rsRows; row++) {
            // 当前行是什么指标
            Cell indexCell = readsheet.getCell(0, row);
            String indexCellContent = indexCell.getContents();
            int indexFlag = INDEXES.getChinese2IntMap().getOrDefault(indexCellContent, -1);
            if (indexFlag == -1) {
                continue;
            }

            for (int column = 1; column < rsColumns; column++) {
                // 哪一年的数据
                Cell yearCell = readsheet.getCell(column, 0);
                String year = yearCell.getContents();
                // 不在需要处理的范围内，直接丢弃
                if (!yearRangeSet.contains(year)) {
                    continue;
                }

                //内容单元格
                Cell cell = readsheet.getCell(column, row);
                String cellContent = cell.getContents();
                storeInfo(stockInfo, cellContent, indexFlag, year);
            }
        }
        System.out.println(stockInfo);
    }

    /**
     * get info from relative xls file and store to StockInfo
     */
    private void storeInfo(StockInfo stockInfo, String cellContent, int indexFlag, String year) {
        if (cellContent.isEmpty()) {
            cellContent = "null";
        }

        switch (indexFlag) {
            case 1:
                stockInfo.addBasicEarningsPerShare(new ContentItem(year, cellContent));
                break;
            case 2:
                stockInfo.addNetProfit(new ContentItem(year, cellContent));
                break;
            case 3:
                stockInfo.addNetProfitGrowthRat(new ContentItem(year, cellContent));
                break;
            case 4:
                stockInfo.addOperationRevenue(new ContentItem(year, cellContent));
                break;
            case 5:
                stockInfo.addOperationRevenueGrowthRat(new ContentItem(year, cellContent));
                break;
            case 6:
                stockInfo.addNetAssetsPerShare(new ContentItem(year, cellContent));
                break;
            case 7:
                stockInfo.addLiability(new ContentItem(year, cellContent));
                break;
            case 8:
                stockInfo.addEachCapitalReserveFund(new ContentItem(year, cellContent));
                break;
            case 9:
                stockInfo.addNonDistributionProfitPerShare(new ContentItem(year, cellContent));
                break;
            case 10:
                stockInfo.addOperatingCashFlowPerShare(new ContentItem(year, cellContent));
                break;
            case 11:
                stockInfo.addGrossProfitMargin(new ContentItem(year, cellContent));
                break;
            case 12:
                stockInfo.addInventoryTurnOver(new ContentItem(year, cellContent));
                break;
            default:
                break;
        }

    }

}
