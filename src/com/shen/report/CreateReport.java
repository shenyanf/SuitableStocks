package com.shen.report;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import com.shen.entity.ContentItem;
import com.shen.trade.AllTrades;
import com.shen.Const;
import com.shen.trade.TradeStocks;
import com.shen.entity.StockInfo;
import com.shen.helper.Indexes;

import com.shen.helper.Util;
import jxl.CellView;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

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
        WritableSheet sheet;
        Indexes indexes = Indexes.getIndexes();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        // 第几个sheet
        int sheetNum = 0;
        int column;
        int row;

        String fileName = Util.getFilePath() + File.separator;
        if (stocks.getTradeName() != null) {
            fileName += AllTrades.tradeShortAndName.get(stocks.getTradeName()) + ".xls";
        } else {
            fileName += "reports.xls";
        }

        try {
            // 打开文件
            book = Workbook.createWorkbook(new File(fileName));
            for (String sheetName : indexes.getSheetName()) {
                System.out.println(sheetName);

                /* 生成名为key的工作表，参数0表示这是第一页 */
                sheet = book.createSheet(sheetName, sheetNum++);
                CellView cellView = new CellView();
                cellView.setAutosize(true); // 设置自动大小

                // 1.0 设置标题
                sheet.addCell(new Label(0, 0, "年份"));

                /* 生成年份标题行 */
                for (int i = 1; i < Const.COUNT_YEARS; i++) {
                    sheet.addCell(new Number(i, 0, currentYear - Const.COUNT_YEARS + i));
                }

                sheet.setColumnView(Const.COUNT_YEARS + 2, cellView);// 根据内容自动设置列宽
                sheet.setColumnView(Const.COUNT_YEARS + 3, cellView);// 根据内容自动设置列宽
                /* 方差和平均值标题栏 */
                sheet.addCell(new Label(Const.COUNT_YEARS + 2, 0, Const.RECENT_YEARS_VARIANCE));
                sheet.addCell(new Label(Const.COUNT_YEARS + 3, 0, Const.RECENT_YEARS_AVERAGE));


                // 2.0 写入数据
                if (list != null && !list.isEmpty()) {
                    /* 根据指标的汉字获得英文单词 */
                    String englishWord = indexes.getKeyFromValueOngetEnglishWordToChinese(sheetName);
                    List<ExcelRow> excelRows = assembleData(list, englishWord);
                    Comparator<ExcelRow> comparator = (row1, row2) -> {
                        List<Double> data1 = row1.getData();
                        List<Double> data2 = row2.getData();

                        System.out.println(row1.getStockBasicInfo() + "  " + row2.getStockBasicInfo());

                        int size = data1.size();
                        // 比较最后一列年度指标
                        int lastYearColumn = size - 5;
                        int compareResult = getNotNull(data2, lastYearColumn).compareTo(getNotNull(data1, lastYearColumn));
                        if (compareResult == 0) {
                            // 如果最后一列年度指标相等，比较平均值
                            compareResult = getNotNull(data2, size - 1).compareTo(getNotNull(data1, size - 1));
                            // 如果平均值相等，比较方差列
                            if (compareResult == 0) {
                                compareResult = getNotNull(data1, size - 2).compareTo(getNotNull(data2, size - 2));
                            }
                        }
                        return compareResult;
                    };

                    // 排序
                    excelRows.sort(comparator);

                    for (int i = 0; i < excelRows.size(); i++) {
                        /* 获得股票代码 */
                        ExcelRow excelRow = excelRows.get(i);

                        sheet.addCell(new Label(0, i + 1, excelRow.getStockBasicInfo()));

                        column = Const.COUNT_YEARS + 4;
                        row = i + 1;

                        /* 写入股票相关指标指 */
                        List<Double> data = excelRow.getData();
                        for (Double value : data) {
                            if (value != null) {
                                sheet.addCell(new Number(--column, row, value));
                            } else {
                                sheet.addCell(new Label(--column, row, "null"));
                            }
                        }
                    }
                }
            }

            // 3.0 写入文件
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

    private Double getNotNull(List<Double> list, int index) {
        if (list == null || index < 0 || index >= list.size()) {
            return Double.MIN_VALUE;
        }
        Double aDouble = list.get(index);
        if (aDouble == null) {
            return Double.MIN_VALUE;
        }
        return aDouble;
    }

    static class ExcelRow {
        private final String stockBasicInfo;
        private final List<Double> data;

        public ExcelRow(String stockBasicInfo, List<Double> data) {
            this.stockBasicInfo = stockBasicInfo;
            this.data = data;
        }

        public String getStockBasicInfo() {
            return stockBasicInfo;
        }

        public List<Double> getData() {
            return data;
        }

        @Override
        public String toString() {
            return "ExcelRow{" +
                    "stockBasicInfo='" + stockBasicInfo + '\'' +
                    ", data=" + data +
                    '}';
        }
    }

    /**
     * 组装excel数据
     *
     * @param list
     * @param key
     * @return
     */
    private List<ExcelRow> assembleData(List<StockInfo> list, String key) {
        List<ExcelRow> excelRows = new ArrayList<>(list.size());

        for (StockInfo stockInfo : list) {
            /* 获得股票代码 */
            String stockCode = stockInfo.getStockCode();
            String stockName = stockInfo.getStockName();
            List<Double> data = new ArrayList<>();

            // 获取对应的值
            List<ContentItem> contentItems = stockInfo.getInfos().get(key);
            Map<String, String> map = contentItems.stream().collect(Collectors.toMap(ContentItem::getYear, ContentItem::getContent));
            for (String year : Const.PROCESS_YEAR_RANGE) {
                String orDefault = map.getOrDefault(year, null);
                if (orDefault == null) {
                    data.add(Double.MIN_VALUE);
                } else {
                    data.add(Double.parseDouble(orDefault));
                }
            }

            // 计算方差
            double[] doubles = data.subList(0, Const.VARIANCE_YEAR).stream().mapToDouble(Double::doubleValue).toArray();
            DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics(doubles);
            double variance = descriptiveStatistics.getVariance();

            // 计算平均值
            double average = Arrays.stream(doubles).average().orElse(0);

            data.add(null);
            data.add(null);
            data.add(variance);
            data.add(average);
            ExcelRow excelRow = new ExcelRow(stockName + "(" + stockCode + ")", data);

            excelRows.add(excelRow);
        }
        System.out.println();
        excelRows.forEach(System.out::println);

        return excelRows;
    }

}
