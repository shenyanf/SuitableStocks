package com.shen.report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.shen.AllTrades;
import com.shen.TradeStocks;
import com.shen.entity.StockInfo;
import com.shen.helper.Indexs;
import com.shen.operatexlsx.ParseStockInfoFromXLS;

import jxl.CellView;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * produce report about tonghuashun trade
 * 
 * @author heshanshan
 * 
 */
public class CreateReport {
	public final static int years = 10;// count how many years data
	private static String filePath = "d:\\stocks"; // store report to this
													// absolute path
	private static String LAST_FIVE_YEARS_VARA = "最近五年方差";
	private static String LAST_FIVE_YEARS_AVERAGE = "最近五年平均值";

	public static void main(String[] args) {
		CreateReport createReport = new CreateReport();
		ParseStockInfoFromXLS parseStockInfoXLS = new ParseStockInfoFromXLS();
		StockInfo stockInfo = new StockInfo("002672");
		TradeStocks stocks = new TradeStocks();
		stocks.getstockBasicInfo().put("002672", "东江环保");

		List<StockInfo> list = new ArrayList<StockInfo>();
		list.add(stockInfo);

		try {
			parseStockInfoXLS.parse(stockInfo);
			createReport.writeExcel(list, stocks);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * write report to excel
	 * 
	 * @param list
	 * @param stocks
	 */
	public void writeExcel(List<StockInfo> list, TradeStocks stocks) {
		WritableWorkbook book = null;
		WritableSheet sheet = null;
		Indexs indexs = Indexs.getIndexs();
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int sheetNum = 0;
		int column = 0;
		int row = 0;
		char startColumn = 'A' + years - 5;
		char endColumn = 'A' + years - 1;
		/* 方差和平均值统计区间 */
		String countRegion = null;
		String fileName = filePath + File.separator;
		if (stocks.getTradeName() != null) {
			fileName += AllTrades.tradeShortAndName.get(stocks.getTradeName()) + ".xls";
		} else {
			fileName += "reports.xls";
		}

		try {
			// 打开文件
			book = Workbook.createWorkbook(new File(fileName));
			for (String key : indexs.getChineseToInterger().keySet()) {
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
				for (int i = 1; i < years; i++) {
					sheet.addCell(new Number(i, 0, Double.valueOf(currentYear - years + i)));
				}

				sheet.setColumnView(years + 2, cellView);// 根据内容自动设置列宽
				sheet.setColumnView(years + 3, cellView);// 根据内容自动设置列宽
				/* 方差和平均值标题栏 */
				sheet.addCell(new Label(years + 2, 0, LAST_FIVE_YEARS_VARA));
				sheet.addCell(new Label(years + 3, 0, LAST_FIVE_YEARS_AVERAGE));

				if (list != null && !list.isEmpty()) {
					for (int i = 0; i < list.size(); i++) {
						/* 获得股票代码 */
						String stockCode = list.get(i).getStockCode();
						/* 根据股票代码获得股票名称 */
						sheet.addCell(
								new Label(0, i + 1, stocks.getstockBasicInfo().get(stockCode) + "(" + stockCode + ")"));

						/* 根据指标的汉字获得英文单词 */
						String englishword = indexs.getKeyFromValueOngetEnglishWordToChinese(key);
						List<String> values = list.get(i).getInfos().get(englishword);
						column = years;
						row = i + 1;

						countRegion = "" + startColumn + (row + 1) + ":" + endColumn + (row + 1);

						/* 写入股票相关指标指 */
						for (int j = 0; j < values.size(); j++) {
							String value = values.get(j);
							if (!value.equals("null")) {
								sheet.addCell(new Number(--column, row, Double.valueOf(value)));
							}
						}

						/* 写入方差和平均值 */
						sheet.addCell(new Formula(years + 2, row, "VARA(" + countRegion + ")"));
						sheet.addCell(new Formula(years + 3, row, "AVERAGE(" + countRegion + ")"));
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
