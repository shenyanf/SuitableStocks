package com.shen.entity;

/**
 * @ClassName aaa
 * @Description 元数据，年份和对应的值
 * @Author shenyanfang
 * @Date 2025/1/10 12:25
 * @Version 1.0
 **/
public class ContentItem {
    private final String year;
    private final String content;

    public ContentItem(String year, String content) {
        this.year = year;
        this.content = content;
    }

    public String getYear() {
        return year;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "ContentItem{" +
                "year='" + year + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}