package com.github.tifezh.kchartlib.chart.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 时间工具类
 * Created by tifezh on 2016/4/27.
 */
public class DateUtil {
    public static SimpleDateFormat LongTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
    public static SimpleDateFormat ShortTimeFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    public static SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
}
