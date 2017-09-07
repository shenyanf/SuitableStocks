package com.shen.zlzj;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.shen.helper.Util;

public class MNIOFCartogram {
	private String stockCode;
	private HashMap<String, List<String>> resMap;
	private String fileName = Util.filePath + File.separator + "主力资金净流向" + ".xls";

	public MNIOFCartogram(String stockCode) {
		this.stockCode = stockCode;
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) throws BiffException, IOException {
		MNIOFCartogram cartogram = new MNIOFCartogram("000002");
		HashMap<String, List<String>> hashMap = cartogram.getZLZJData();
		cartogram.appendToExcel();
		/*
		 * Pattern pattern = Pattern.compile(".*(<span.*>)(.*)(</span>).*",
		 * Pattern.DOTALL); String str =
		 * "<td> <span class=\"green\">-494万</span> </td>"; Matcher matcher =
		 * pattern.matcher(str); if (matcher.find())
		 * System.out.println(matcher.group());
		 * System.out.println(matcher.group());
		 */
	}

	public void createExcel() {
		WritableSheet sheet = null;
		Workbook workbook = null;
		File file = null;
		WritableWorkbook writableWorkbook = null;
		String stockName = "";

		try {
			file = new File(fileName);

			/* 如果文件存在 */
			if (file.exists()) {
				workbook = Workbook.getWorkbook(file);
				writableWorkbook = Workbook.createWorkbook(file, workbook);

				/* 不存在与股票名称的单元表，创建新的单元表 */
				if (!Arrays.toString(writableWorkbook.getSheetNames()).contains(stockName)) {
					sheet = writableWorkbook.createSheet(stockName, writableWorkbook.getNumberOfSheets() + 1);
					createTitle(sheet);
				}
			} else {
				writableWorkbook = Workbook.createWorkbook(file);
				System.out.println(stockName);
				/* 生成名为key的工作表 */
				sheet = writableWorkbook.createSheet(stockName, writableWorkbook.getNumberOfSheets() + 1);

				createTitle(sheet);
			}

			writableWorkbook.write();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writableWorkbook != null) {
				try {
					writableWorkbook.close();
				} catch (WriteException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (workbook != null) {
				workbook.close();
			}
		}

	}

	public void createTitle(WritableSheet sheet) throws RowsExceededException, WriteException {
		String netAmount = "净额(万)";
		String percent = "净占比(%)";

		sheet.mergeCells(0, 0, 0, 1);
		sheet.mergeCells(1, 0, 1, 1);
		sheet.mergeCells(2, 0, 2, 1);
		sheet.mergeCells(3, 0, 4, 0);
		sheet.mergeCells(5, 0, 6, 0);
		sheet.mergeCells(7, 0, 8, 0);
		sheet.mergeCells(9, 0, 10, 0);
		sheet.mergeCells(11, 0, 12, 0);

		sheet.addCell(new Label(0, 0, "日期"));
		sheet.addCell(new Label(1, 0, "收盘价"));
		sheet.addCell(new Label(2, 0, "涨跌幅"));

		sheet.addCell(new Label(3, 0, "主力净流入"));
		sheet.addCell(new Label(3, 1, netAmount));
		sheet.addCell(new Label(4, 1, percent));

		sheet.addCell(new Label(5, 0, "超大单净流入"));
		sheet.addCell(new Label(5, 1, netAmount));
		sheet.addCell(new Label(6, 1, percent));

		sheet.addCell(new Label(7, 0, "大单净流入"));
		sheet.addCell(new Label(7, 1, netAmount));
		sheet.addCell(new Label(8, 1, percent));

		sheet.addCell(new Label(9, 0, "中单净流入"));
		sheet.addCell(new Label(9, 1, netAmount));
		sheet.addCell(new Label(10, 1, percent));

		sheet.addCell(new Label(11, 0, "小单净流入"));
		sheet.addCell(new Label(11, 1, netAmount));
		sheet.addCell(new Label(12, 1, percent));

		// sheet.addCell(new Label(14, 0, "当日净流入"));
	}

	public void appendToExcel() {
		createExcel();

		WritableWorkbook writableWorkbook = null;
		Workbook workbook = null;
		WritableSheet sheet = null;
		String key = null;
		int column = 0;
		int row = 0;
		double valueInt = 0;
		Date lastestDate = null;

		// String stockName = AllStocks.getStockName(stockCode);

		String stockName = "";

		/* 排序获取到的日期 */
		List<Object> keyList = sortHashMapKey(resMap);

		// 设置字体;
		WritableFont redFont = new WritableFont(WritableFont.TAHOMA, 10, WritableFont.NO_BOLD, false,
				UnderlineStyle.NO_UNDERLINE, Colour.RED);
		WritableFont greenFont = new WritableFont(WritableFont.TAHOMA, 10, WritableFont.NO_BOLD, false,
				UnderlineStyle.NO_UNDERLINE, Colour.GREEN);
		WritableCellFormat redCellFormat = new WritableCellFormat(redFont);
		WritableCellFormat greenCellFormat = new WritableCellFormat(greenFont);

		try {
			File file = new File(fileName);
			workbook = Workbook.getWorkbook(file);
			writableWorkbook = Workbook.createWorkbook(file, workbook);
			sheet = writableWorkbook.getSheet(stockName);

			/* 字体大小自适应 */
			CellView cellView = new CellView();
			cellView.setAutosize(true);
			sheet.setColumnView(0, cellView);
			sheet.setRowView(0, cellView);
			row = sheet.getRows();

			/* 确定单元表最后一行的日期，如果是新建的表格设置最后写入的日期为1970-1-1 */
			String lastestDateStr = sheet.getCell(0, row - 1).getContents();
			if (lastestDateStr != null && !lastestDateStr.equals("")) {
				lastestDate = DateFormat.getDateInstance().parse(lastestDateStr);
			} else {
				lastestDate = DateFormat.getDateInstance().parse("1970-1-1");
			}

			/* 根据日期写入相关数据 */
			if (keyList != null && !keyList.isEmpty()) {
				for (int i = 0; i < keyList.size(); i++, row++) {
					column = 0;
					key = keyList.get(i).toString();

					/* 判断是否继续写入数据 */
					int compareValue = lastestDate.compareTo(DateFormat.getDateInstance().parse(key.trim()));
					if (compareValue >= 0) {
						continue;
					}

					sheet.addCell(new Label(column++, row, key));

					/* 写入股票相关指标指 */
					for (int j = 0; j < resMap.get(key).size(); j++) {
						String value = resMap.get(key).get(j);
						if (!value.equals("-")) {

							/* 根据值设置字体颜色 */
							String numberStr = getNumberFromString(value);
							if (numberStr != null) {
								valueInt = Double.valueOf(numberStr);
							}
							Label label = new Label(column++, row, value);
							if (valueInt < 0d) {
								label.setCellFormat(greenCellFormat);
							} else if (valueInt > 0d) {
								label.setCellFormat(redCellFormat);
							}

							/* 根据涨幅设置收盘价颜色 */
							if ((j == 0) && (Double.valueOf(getNumberFromString(resMap.get(key).get(1))) < 0.00d)) {
								label.setCellFormat(greenCellFormat);
							}

							sheet.addCell(label);
						}
					}

					/* 写入当日净流入之和 */
					/*
					 * sumColumn = row + 1; getSum = "SUM(" + "F" + sumColumn +
					 * ",H" + sumColumn + ",J" + sumColumn + ",L" + sumColumn +
					 * ")"; sheet.addCell(new Formula(14, row, getSum));
					 * sheet.addCell(new Formula(years + 3, row, "AVERAGE(" +
					 * countRegion + ")"));
					 */
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			/* 写入数据并关闭文件，否则文件会损坏 */
			try {
				writableWorkbook.write();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if (writableWorkbook != null) {
				try {
					writableWorkbook.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (workbook != null) {
				workbook.close();
			}
		}

	}

	public HashMap<String, List<String>> getZLZJData() {
		return analysisContent(getTargetElement(stockCode));
	}

	public Node getTargetElement(String stockCode) {
		String url = "http://data.eastmoney.com/zjlx/" + stockCode + ".html";
		Document document;
		try {
			document = Jsoup.connect(url).get();
			List<Node> nodes = document.getElementById("content_zjlxtable").childNodes();
			for (Node node : nodes) {
				if (node.nodeName().equals("table")) {
					// System.out.println(node.outerHtml());
					return node;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public HashMap<String, List<String>> analysisContent(Node targetElement) {
		// System.out.println(targetElement.outerHtml());
		resMap = new HashMap<String, List<String>>();
		Node table = null;
		/*
		 * the example is <th class="tips-colname-Left"><span>每股指标</span></th>
		 */
		Pattern patternTd = Pattern.compile(".*(<td>)(.*)(</td>).*", Pattern.DOTALL);
		Pattern patternSpan = Pattern.compile(".*(<span.*>)(.*)(</span>).*", Pattern.DOTALL);
		Matcher matcher = null;
		String expectRes = null;

		/* get table body contain tag tr and tag th */
		table = targetElement.childNode(3);

		/* get info from table */
		for (Node tr : table.childNodes()) {
			String key = null;
			List<String> values = new ArrayList<String>();
			for (Node td : tr.childNodes()) {
				String s = td.outerHtml();
				matcher = patternTd.matcher(s);
				if (matcher.find()) {
					expectRes = matcher.group(2);

					/*
					 * if result has span tag, save as value ,else save as key
					 */
					if (expectRes.contains("span")) {
						matcher = patternSpan.matcher(s);
						if (matcher.find()) {
							expectRes = matcher.group(2);
							// values.add(expectRes.trim().split("万")[0].split("%")[0]);
							values.add(expectRes.trim());
						}
					} else {
						key = expectRes.trim();
					}
				}
			}
			if (key != null) {
				resMap.put(key, values);
			}
		}
		return resMap;
	}

	private List<Object> sortHashMapKey(HashMap<String, List<String>> hashMap) {
		List<Object> keyList = new ArrayList<Object>();
		Collections.addAll(keyList, hashMap.keySet().toArray());
		Collections.sort(keyList, new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				return ((String) o1).compareTo((String) o2);
			}
		});
		return keyList;
	}

	private String getNumberFromString(String srcStr) {
		Pattern pattern = Pattern.compile("(^-?\\d*\\.?\\d*).*");
		Matcher matcher = pattern.matcher(srcStr);
		String numberStr = null;
		if (matcher.find()) {
			numberStr = matcher.group(1);
			System.out.println(numberStr);
		}
		return numberStr;
	}
}
