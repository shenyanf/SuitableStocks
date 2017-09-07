package com.shen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

public class GetStockInfo_old {
	private List<String> indexs;
	private List<String> stocks;
	private HashMap<String, List<String>> resMap;

	public static void main(String[] args) throws IOException {
		List<String> indexs = new ArrayList<String>();
		indexs.add("每股指标");
		indexs.add("每股净资产(元)");
		indexs.add("营业收入(元)");
		indexs.add("扣非净利润(元)");
		indexs.add("营业收入同比增长(%)");
		indexs.add("扣非净利润同比增长(%)");
		indexs.add("资产负债率(%)");
		indexs.add("每股经营现金流(元)");

		String stockCode = "sz002672";

		GetStockInfo_old getStockInfo = new GetStockInfo_old();
		getStockInfo.setIndexs(indexs);

		Node jspContent = getStockInfo.getTargetElement(stockCode);
		HashMap<String, List<String>> hashMap = getStockInfo.analysisContent(jspContent);
		getStockInfo.storeInFile(stockCode, hashMap);
	}

	public Node getTargetElement(String stockCode) {
		String url = "http://f10.eastmoney.com/f10_v2/FinanceAnalysis.aspx?code=" + stockCode;
		Document document;
		try {
			document = Jsoup.connect(url).get();
			List<Node> nodes = document.getElementById("F10MainTargetDiv").childNodes();
			for (Node node : nodes) {
				if (node.nodeName().equals("table")) {
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
		System.out.println(targetElement);
		resMap = new HashMap<String, List<String>>();

		/*
		 * the example is <th class="tips-colname-Left"><span>每股指标</span></th>
		 */
		Pattern pattern = Pattern.compile(".*(<span>)(.*)(</span>).*", Pattern.DOTALL);
		Matcher matcher = null;
		String expectRes = null;

		/* get table body contain tag tr and tag th */
		Node table = targetElement.childNode(0);

		/* get info from table */
		for (Node tr : table.childNodes()) {
			String key = null;
			List<String> values = new ArrayList<String>();
			for (Node td : tr.childNodes()) {
				String s = td.outerHtml();
				matcher = pattern.matcher(s);
				if (matcher.find()) {
					expectRes = matcher.group(2);

					/* store as hashmap's key if expectRes is chinese */
					if (checkChinese(expectRes) && indexs.contains(expectRes)) {
						key = expectRes;
					} else {
						values.add(expectRes);
					}
				}
			}
			if (key != null) {
				resMap.put(key, values);
			}
		}
		return resMap;
	}

	private boolean checkChinese(String sourceString) {
		/*
		 * String regEx = "[\\u4e00-\\u9fa5]"; Pattern p =
		 * Pattern.compile(regEx); Matcher m = p.matcher(sourceString); int
		 * count = 0; while (m.find()) { for (int i = 0; i <= m.groupCount();
		 * i++) { count = count + 1; } } return count > 0;
		 */

		return sourceString.getBytes().length == sourceString.length() ? false : true;
	}

	private boolean storeInFile(String stockNumber, HashMap<String, List<String>> result) {
		String filePath = "D:\\stocks" + File.separator + stockNumber + ".txt";
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(filePath);

			for (Entry<String, List<String>> set : result.entrySet()) {
				fileOutputStream.write(set.getKey().getBytes());
				fileOutputStream.write(set.getValue().toString().getBytes());
				fileOutputStream.write("\r\n".getBytes());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	public List<String> getIndexs() {
		return indexs;
	}

	public void setIndexs(List<String> indexs) {
		this.indexs = indexs;
	}

	public List<String> getStocks() {
		return stocks;
	}

	public void setStocks(List<String> stocks) {
		this.stocks = stocks;
	}

	public HashMap<String, List<String>> getResMap() {
		return resMap;
	}

	public void setResMap(HashMap<String, List<String>> resMap) {
		this.resMap = resMap;
	}
}
