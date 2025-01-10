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
        // 第几个sheet
        int sheetNum = 0;

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

                //生成年份标题行
                //2016, 2017, 2018, 2019, 2020, 2021, 2022, 2023
                List<String> ascSortedYear = Const.PROCESS_YEAR_RANGE.stream().sorted().collect(Collectors.toList());
                int sortedYearLength = ascSortedYear.size();
                for (int i = 0; i < sortedYearLength; i++) {
                    sheet.addCell(new Label(i + 1, 0, ascSortedYear.get(i)));
                }

                sheet.setColumnView(sortedYearLength + 3, cellView);// 根据内容自动设置列宽
                sheet.setColumnView(sortedYearLength + 4, cellView);// 根据内容自动设置列宽
                /* 方差和平均值标题栏 */
                sheet.addCell(new Label(sortedYearLength + 3, 0, Const.RECENT_YEARS_VARIANCE));
                sheet.addCell(new Label(sortedYearLength + 4, 0, Const.RECENT_YEARS_AVERAGE));


                // 2.0 写入数据
                if (list != null && !list.isEmpty()) {
                    /* 根据指标的汉字获得英文单词 */
                    String englishWord = indexes.getKeyFromValueOngetEnglishWordToChinese(sheetName);
                    List<ExcelRow> excelRows = assembleData(list, englishWord, ascSortedYear);
                    Comparator<ExcelRow> comparator = (row1, row2) -> {
                        List<Double> data1 = row1.getData();
                        List<Double> data2 = row2.getData();

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

                        int row = i + 1;

                        /* 写入股票相关指标指 */
                        List<Double> data = excelRow.getData();
                        for (int i1 = 0; i1 < data.size(); i1++) {
                            Double value = data.get(i1);
                            if (value == null || value.equals(Double.MIN_VALUE)) {
                                sheet.addCell(new Label(i1 + 1, row, ""));
                            } else {
                                sheet.addCell(new Number(i1 + 1, row, value));
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
     * @param list          股票信息
     * @param key           指标对应的英文
     * @param ascSortedYear 年份，正序排列
     * @return 拼装好的数据
     */
    private List<ExcelRow> assembleData(List<StockInfo> list, String key, List<String> ascSortedYear) {
        List<ExcelRow> excelRows = new ArrayList<>(list.size());

        for (StockInfo stockInfo : list) {
            /* 获得股票代码 */
            String stockCode = stockInfo.getStockCode();
            String stockName = stockInfo.getStockName();
            List<Double> data = new ArrayList<>();

            // 获取对应的值
            List<ContentItem> contentItems = stockInfo.getInfos().get(key);
            Map<String, String> map = contentItems.stream().collect(Collectors.toMap(ContentItem::getYear, ContentItem::getContent));
            for (String year : ascSortedYear) {
                String orDefault = map.getOrDefault(year, null);
                if (orDefault == null || "null".equals(orDefault)) {
                    data.add(Double.MIN_VALUE);
                } else {
                    data.add(Double.parseDouble(orDefault));
                }
            }

            // 计算方差
            double[] doubles = data.subList(ascSortedYear.size() - Const.VARIANCE_YEAR, ascSortedYear.size()).stream().mapToDouble(Double::doubleValue).toArray();
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

        return excelRows;
    }

}
