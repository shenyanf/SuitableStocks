package com.shen.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public final class Util {
    public final static String FILE_PATH = "d:\\stocks";
    private static String cookie;

    public Util() {
    }

    /**
     * check sourceString is Chinese or not
     * 
     * @param sourceString
     * @return
     */
    public static boolean checkChinese(String sourceString) {
        /*
         * String regEx = "[\\u4e00-\\u9fa5]"; Pattern p = Pattern.compile(regEx); Matcher m = p.matcher(sourceString);
         * int count = 0; while (m.find()) { for (int i = 0; i <= m.groupCount(); i++) { count = count + 1; } } return
         * count > 0;
         */

        return sourceString.getBytes().length == sourceString.length() ? false : true;
    }

    public static String getFilePath() {
        return FILE_PATH;
    }

    public static Map<String, String> loadAllStockCodeAndName() {
        String body = null;
        List<String> stockCodes = new ArrayList<String>();
        List<String> stockNames = new ArrayList<String>();
        Map<String, String> stockBasicInfo = new HashMap<String, String>();

        /* get stockcode in circle */
        for (int i = 1;; i++) {
            String url = "http://q.10jqka.com.cn/index/index/board/all/field/zdf/order/desc/page/" + i + "/ajax/1/";

            try {
                /* bs4 解析html */
                body = getDatas(url);

                Document doc = Jsoup.parse(body, "UTF-8");
                // baseUri 参数用于解决文件中URLs是相对路径的问题。如果不需要可以传入一个空的字符串。
                doc.setBaseUri("http://q.10jqka.com.cn");

                Elements links = doc.select("td a[target=_blank]");

                // 如果tbody没有内容，就推出
                if (links.size() == 0) {
                    System.out.println("breakkkkkkkkkkk");
                    break;
                }

                for (Element link : links) {
                    // String linkHref = link.attr("href");
                    String linkText = link.text();
                    // 如果是编码就添加到list中
                    if (linkText.matches("\\d*")) {
                        stockCodes.add(linkText);
                    } else {
                        stockNames.add(linkText);
                    }
                }
                // 单位是mills
                Thread.sleep(500);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < stockCodes.size(); i++) {
            stockBasicInfo.put(stockCodes.get(i), stockNames.get(i));
        }
        System.out.println(stockBasicInfo);
        System.out.println(stockBasicInfo.size());

        return stockBasicInfo;
    }

    public static String getDatas(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = null;
        CloseableHttpResponse response = null;
        String respContent = "";
        try {
            httpGet = new HttpGet(url);
            httpGet.setHeader("Host", "q.10jqka.com.cn");
            httpGet.setHeader("Connection", "keep-alive");
            httpGet.setHeader("Pragma", "no-cache");
            httpGet.setHeader("Cache-Control", "no-cache");
            httpGet.setHeader("Upgrade-Insecure-Requests", "1");
            httpGet.setHeader("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36");
            httpGet.setHeader("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            httpGet.setHeader("Accept-Encoding", "gzip, deflate");
            httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
            httpGet.setHeader("Cookie", cookie);

            response = httpClient.execute(httpGet);

            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                respContent = EntityUtils.toString(response.getEntity(), "utf-8");
                System.out.println(respContent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return respContent;
    }

    public static String getCookie() {
        return cookie;
    }

    public static void setCookie(String cookie) {
        Util.cookie = cookie;
    }

    public static void main(String[] args) {
        String url = "http://q.10jqka.com.cn/thshy/detail/field/199112/order/desc/page/2/ajax/1/code/881125";
        String cookie = "v=AmO5bVZ7PH_FCfBLYcnZ0XvH8qwOWPaeMek7yJXAv445Z41aHSiH6kG8z6ym";
        Util.setCookie(cookie);
        Util.loadAllStockCodeAndName();
    }

}
