/*
 * Copyright 2021-2031 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.helper.base.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * 类名:DateUtil.
 * 2021/10/12 13:59
 * <p>
 *
 * @author zyw
 * @version 1.0.0
 */
public class DateUtil {

    public final static String YYYY_MM_DD_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    public final static String YYYY_MM_DD_HH_mm_ss_SSS = "yyyy-MM-dd HH:mm:ss SSS";

    public final static String YYMMDD = "yyMMdd";

    public final static String YYYYMMDD = "yyyyMMdd";

    public final static String YYYYMMDDHHmmssSSS = "yyyyMMddHHmmssSSS";

    public final static String YYYYMM = "yyyyMM";

    public final static String YYMM = "yyMM";

    public final static String YYYY_MM_DD = "yyyy-MM-dd";

    public final static String CST = "EEE MMM dd HH:mm:ss 'CST' yyyy";

    public final static String YYYY_MM_DD_ZN_CH = "yyyy年MM月dd日";

    public static final long ONE_DAY_HOUR = 24;

    public static final int HOUR_TO_MINUTE = 60;

    public static final int MINUTE_TO_SECOND = 60;

    public static final long SECOND_TO_MILLI = 1000;

    /**
     * 当前时间戳.
     *
     * @return 当前时间戳
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * 当前时间戳.
     *
     * @return 当前时间戳
     */
    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * 日期格式化.
     *
     * @param date
     * @param format
     * @return 日期格式化字符串
     */
    public static String format(Date date, String format) {
        if (null == date) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 格式化日期,默认返回yyyy-MM-dd HH:mm:ss.
     *
     * @param date
     * @return 返回yyyy-MM-dd HH:mm:ss格式化字符串
     */
    public static String format(Date date) {
        return format(date, DateUtil.YYYY_MM_DD_HH_mm_ss);
    }

    /**
     * 格式化显示当前日期.
     *
     * @param format
     * @return 格式化显示当前日期
     */
    public static String format(String format) {
        return format(new Date(), format);
    }
}
