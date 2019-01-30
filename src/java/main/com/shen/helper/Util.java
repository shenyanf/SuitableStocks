package com.shen.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;

public final class Util {
    // 工程存储文件的目录
    public final static String FILE_PATH = "d:\\stocks";

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

    /**
     * 所有目前状态为上市的股票的代码和名称
     * 
     * @return
     */
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

    /**
     * 获取页面或这jsoup格式的信息
     */
    public static String getDatas(String url) {
        CookieStore cookieStore = new BasicCookieStore();
        HttpGet httpGet = null;
        CloseableHttpResponse response = null;
        String respContent = "";
        CloseableHttpClient httpClient = null;
        List<Header> headers = new ArrayList<Header>();
        StringBuilder cookieSb = new StringBuilder();

        try {
            // Util.getCookies().forEach(c -> {
            // String name = c.getName();
            // String value = c.getValue();
            // String domain = c.getDomain();
            // Date expiryDate = c.getExpires();
            // String path = c.getPath();
            //
            // BasicClientCookie basicClientCookie = new BasicClientCookie(name, value);
            // basicClientCookie.setDomain(domain);
            // basicClientCookie.setExpiryDate(expiryDate);
            // basicClientCookie.setPath(path);
            // cookieStore.addCookie(basicClientCookie);
            // });

            httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

            headers.add(new BasicHeader(HttpHeaders.HOST, "q.10jqka.com.cn"));
            headers.add(new BasicHeader(HttpHeaders.CONNECTION, "keep-alive"));
            headers.add(new BasicHeader(HttpHeaders.UPGRADE, "1"));
            headers.add(new BasicHeader(HttpHeaders.USER_AGENT,
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36"));
            headers.add(new BasicHeader(HttpHeaders.ACCEPT,
                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"));
            headers.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"));
            headers.add(new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate"));
            headers.add(new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9,en;q=0.8"));
            headers.add(new BasicHeader(HttpHeaders.CACHE_CONTROL, "no-cache"));
            headers.add(new BasicHeader(HttpHeaders.PRAGMA, "no-cache"));

            // 使用htmlUint获取cookie信息
            Util.getCookies().forEach(c -> {
                String name = c.getName();
                String value = c.getValue();
                String domain = c.getDomain();
                Date expiryDate = c.getExpires();
                String path = c.getPath();

                cookieSb.append(name);
                cookieSb.append("=");
                cookieSb.append(value);
                cookieSb.append(";");
            });
            headers.add(new BasicHeader("Cookie", cookieSb.toString()));
            // headers.add(new BasicHeader("Cookie", "v=Ah4lzQM9zhjvAxp8tw9es48wb79j3-P1dKKWNMinj0pd1LBpMG8yaUQz5lOb"));

            httpGet = new HttpGet(url);
            for (Header h : headers) {
                httpGet.setHeader(h);
            }

            response = httpClient.execute(httpGet);

            System.out.println("request url:" + url + " response status:" + response.getStatusLine());
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

    /**
     * 使用htmlunit获取cookie信息
     * 
     * @return
     */
    public static Set<Cookie> getCookies() {
        WebClient webClient = new WebClient();
        Set<Cookie> cookies = null;
        try {
            HtmlPage page = webClient.getPage("http://q.10jqka.com.cn");
            System.out.println(page.getHead());
            cookies = webClient.getCookieManager().getCookies();
            cookies.forEach(c -> System.out.println(c));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return cookies;
    }

    public static void main(String[] args) {
        String url = "http://q.10jqka.com.cn/thshy/detail/field/199112/order/desc/page/1/ajax/1/code/881155";
        String basicUrl = "http://www.10jqka.com.cn/";
        Util.getDatas(url);
    }

}
