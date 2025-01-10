package com.shen;

import java.util.*;
import java.util.stream.Collectors;

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

    public static final String RECENT_YEARS_VARIANCE = "最近三年方差";
    public static final String RECENT_YEARS_AVERAGE = "最近三年平均值";

    /**
     * 年报有很多年前的数据，参与处理的年份范围
     * 2023, 2022, 2021, 2020, 2019, 2018, 2017, 2016
     *
     * @return
     */
    private static List<String> needProcessYearRange() {
        List<String> yearRange = new ArrayList<>();
        Calendar instance = Calendar.getInstance();
        int currentYear = instance.get(Calendar.YEAR);
        int currentMonth = instance.get(Calendar.MONTH);
        int start = 1;
        // 最晚4.30号公布上一年年报
        if (currentMonth < Calendar.MAY) {
            start = 2;
        }

        for (int i = start; i < Const.COUNT_YEARS; i++) {
            yearRange.add(String.valueOf(currentYear - i));
        }

        return yearRange;
    }

    public static void main(String[] args) {
        List<String> processYearRange = Const.PROCESS_YEAR_RANGE;
        Collections.reverse(processYearRange);
        System.out.println(processYearRange);

        List<String> collect = Const.PROCESS_YEAR_RANGE.stream().sorted().collect(Collectors.toList());
        System.out.println(collect);
    }
}
