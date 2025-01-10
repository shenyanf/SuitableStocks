package com.shen.xlsx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.shen.entity.StockInfo;
import com.shen.helper.Util;

/**
 * download stock information from Internet in excel type
 *
 * @author heshanshan
 */
public class DownloadStockXLS {
    /**
     * down stock information xls file from tonghuashun
     *
     * @param stockCode
     * @return
     */
    public boolean downloadXLS(String stockCode) {
        String uri = "http://basic.10jqka.com.cn/" + stockCode + "/xls/mainyear.xls";
        File file = new File(Util.getFilePath() + File.separator + stockCode + ".xls");

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(uri);

        httpGet.setHeader(HttpHeaders.HOST, "basic.10jqka.com.cn");
        httpGet.setHeader(HttpHeaders.CONNECTION, "keep-alive");
        httpGet.setHeader(HttpHeaders.UPGRADE, "1");
        httpGet.setHeader(HttpHeaders.USER_AGENT,
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36");
        httpGet.setHeader(HttpHeaders.ACCEPT,
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        httpGet.setHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate");
        httpGet.setHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9,en;q=0.8");

        HttpResponse response = null;
        InputStream in = null;
        FileOutputStream fout = null;

        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            in = entity.getContent();

            fout = new FileOutputStream(file);
            int l = -1;
            byte[] tmp = new byte[1024];
            while ((l = in.read(tmp)) != -1) {
                fout.write(tmp, 0, l);
            }
            fout.flush();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                }
            }
        }
        return true;
    }

}