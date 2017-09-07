package com.shen;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.shen.helper.Util;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * all stocks's code and name, contains szse and sse
 * 
 * @author heshanshan
 * 
 */
public class AllStocks {
	private static Map<String, String> allStockCodeAndName = loadStockBasicInfoFromTHS();

	public AllStocks() {
	}

	/**
	 * 从xlsx表中加载stockcode和name
	 * 
	 * @return hashmap
	 */
	@Deprecated
	private static HashMap<String, String> init() {
		jxl.Workbook readwb = null;
		HashMap<String, String> hashMap = new HashMap<String, String>();
		InputStream instream;
		try {
			instream = AllStocks.class.getResourceAsStream("/" + "上市公司列表.xls");

			readwb = Workbook.getWorkbook(instream);

			// Sheet的下标是从0开始
			// 获取第一张Sheet表
			Sheet readsheet = readwb.getSheet(0);

			// 获取Sheet表中所包含的总列数,目前只需要代码和名称
			// int columns = 2;
			// readsheet.getColumns();

			// 获取Sheet表中所包含的总行数
			int rsRows = readsheet.getRows();

			// 获取指定单元格的对象引用
			for (int i = 0; i < rsRows; i++) {
				String key = readsheet.getCell(0, i).getContents();
				String value = readsheet.getCell(1, i).getContents();

				// System.out.println("code is " + key + " name is " + value);

				hashMap.put(key, value);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return hashMap;
	}

	/**
	 * 调用com.shen.helper.Util的方法，从同花顺上加载所有公司的编码和名称
	 * 
	 * @return Map<String,String>
	 */
	private static Map<String, String> loadStockBasicInfoFromTHS() {
		System.out.println("从同花顺上加载所有公司的编码和名称");
		return Util.loadAllStockCodeAndName();
	}

	public static String getStockName(String stockCode) {
		return allStockCodeAndName.get(stockCode);
	}

	public static void main(String[] args) {
		System.out.println(AllStocks.allStockCodeAndName);
	}
}
