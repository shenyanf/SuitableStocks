package com.shen;

import org.w3c.dom.stylesheets.LinkStyle;

import java.util.*;

/**
 * @ClassName com.shen.Const
 * @Description 静态值
 * @Author shenyanfang
 * @Date 2025/1/8 11:05
 * @Version 1.0
 **/
public class Const {
    /**
     * 工程存储文件的目录
     */
    public final static String FILE_PATH = "d:\\stocks";
    /**
     * 计算多少年的数据，需要大于1
     */
    public final static int COUNT_YEARS = 10;

    /**
     * 计算多少年的数据的年份范围
     */
    public final static List<String> PROCESS_YEAR_RANGE = needProcessYearRange();

    /**
     * 计算几年的平均值和方差
     */
    public static final int VARIANCE_YEAR = 3;

    public static final List<String> VARIANCE_YEAR_RANGE = varianceYearRange();

    public static final String RECENT_YEARS_VARIANCE = "最近三年方差";
    public static final String RECENT_YEARS_AVERAGE = "最近三年平均值";

    /**
     * 年报有很多年前的数据，参与处理的年份范围
     *
     * @return
     */
    private static List<String> needProcessYearRange() {
        List<String> yearRange = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        for (int i = 0; i < Const.COUNT_YEARS; i++) {
            yearRange.add(String.valueOf(currentYear - i));
        }

        return yearRange;
    }


    /**
     * 参与平均值和方差计算的年份
     *
     * @return
     */
    private static List<String> varianceYearRange() {
        List<String> yearRange = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        for (int i = 0; i < Const.VARIANCE_YEAR; i++) {
            yearRange.add(String.valueOf(currentYear - i));
        }

        return yearRange;
    }
}
