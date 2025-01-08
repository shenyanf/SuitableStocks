package com.shen.report;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import com.shen.trade.AllTrades;
import com.shen.Const;
import com.shen.trade.TradeStocks;
import com.shen.entity.StockInfo;
import com.shen.helper.Indexes;

import com.shen.helper.Util;
import jxl.CellView;
import jxl.Workbook;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * produce report about tonghuashun trade
 *
 * @author heshanshan
 */
public class CreateReport {

    /**
     * write report to excel
     *
     * @param list
     * @param stocks
     */
    public void writeExcel(List<StockInfo> list, TradeStocks stocks) {
        WritableWorkbook book = null;
        WritableSheet sheet = null;
        Indexes indexes = Indexes.getIndexes();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int sheetNum = 0;
        int column = 0;
        int row = 0;
        char startColumn = 'A' + Const.COUNT_YEARS - Const.YEAR_RANGE_START;
        char endColumn = 'A' + Const.COUNT_YEARS - 1;
        /* 方差和平均值统计区间 */
        String countRegion = null;
        String fileName = Util.getFilePath() + File.separator;
        if (stocks.getTradeName() != null) {
            fileName += AllTrades.tradeShortAndName.get(stocks.getTradeName()) + ".xls";
        } else {
            fileName += "reports.xls";
        }

        try {
            // 打开文件
            book = Workbook.createWorkbook(new File(fileName));
            for (String key : indexes.getChinese2IntMap().keySet()) {
                System.out.println(key);
                /* 不需要年份的sheet */
                if (key.equals("科目\\时间(年)")) {
                    continue;
                }

                /* 生成名为key的工作表，参数0表示这是第一页 */
                sheet = book.createSheet(key, sheetNum++);
                CellView cellView = new CellView();
                cellView.setAutosize(true); // 设置自动大小

                sheet.addCell(new Label(0, 0, "年份"));

                /* 生成年份标题行 */
                for (int i = 1; i < Const.COUNT_YEARS; i++) {
                    sheet.addCell(new Number(i, 0, Double.valueOf(currentYear - Const.COUNT_YEARS + i)));
                }

                sheet.setColumnView(Const.COUNT_YEARS + 2, cellView);// 根据内容自动设置列宽
                sheet.setColumnView(Const.COUNT_YEARS + 3, cellView);// 根据内容自动设置列宽
                /* 方差和平均值标题栏 */
                sheet.addCell(new Label(Const.COUNT_YEARS + 2, 0, Const.RECENT_YEARS_VARIANCE));
                sheet.addCell(new Label(Const.COUNT_YEARS + 3, 0, Const.RECENT_YEARS_AVERAGE));

                if (list != null && !list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        /* 获得股票代码 */
                        String stockCode = list.get(i).getStockCode();
                        /* 根据股票代码获得股票名称 */
                        sheet.addCell(
                                new Label(0, i + 1, stocks.getstockBasicInfo().get(stockCode) + "(" + stockCode + ")"));

                        /* 根据指标的汉字获得英文单词 */
                        String englishword = indexes.getKeyFromValueOngetEnglishWordToChinese(key);
                        List<String> values = list.get(i).getInfos().get(englishword);
                        column = Const.COUNT_YEARS;
                        row = i + 1;

                        countRegion = "" + startColumn + (row + 1) + ":" + endColumn + (row + 1);

                        /* 写入股票相关指标指 */
                        for (int j = 0; j < values.size(); j++) {
                            String value = values.get(j);
                            if (!value.equals("null")) {
                                sheet.addCell(new Number(--column, row, Double.valueOf(value)));
                            } else {
                                sheet.addCell(new Label(--column, row, "null"));
                            }
                        }

                        /* 写入方差和平均值 */
                        sheet.addCell(new Formula(Const.COUNT_YEARS + 2, row, "VARA(" + countRegion + ")"));
                        sheet.addCell(new Formula(Const.COUNT_YEARS + 3, row, "AVERAGE(" + countRegion + ")"));
                    }
                }
            }

            // 写入数据并关闭文件
            book.write();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (book != null) {
                try {
                    book.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
