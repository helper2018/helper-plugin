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

package org.helper.plugins.utils;

import java.util.Date;

import static org.helper.plugins.utils.DateUtil.date2Str;

/**
 * <p>
 * 类名:StringUtil.
 * 2021/10/20 17:15
 * <p>
 *
 * @author zyw
 * @version 1.0.0
 */
public final class StringUtil {
    /**
     * 首字母小写
     *
     * @param str
     * @return
     */
    public static String firstCharToLowCase(String str) {
        char c = str.charAt(0);
        if (c >= 'A' && c <= 'Z') {
            return (char) (c + 32) + str.substring(1);
        }
        return str;
    }

    /**
     * 首字母大写
     *
     * @param str
     * @return
     */
    public static String firstCharToUpperCase(String str) {
        char c = str.charAt(0);
        if (c >= 'a' && c <= 'z') {
            return (char) (c - 32) + str.substring(1);
        }
        return str;
    }

    /**
     * copy form spring boot 2.5.5: org.springframework.util.StringUtils
     *
     * @param str
     * @return
     */
    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * copy form spring boot 2.5.5: org.springframework.util.StringUtils
     *
     * @param str
     * @return
     */
    public static boolean hasText(String str) {
        return (null != str) && !str.isEmpty() && containsText(str);
    }

    /**
     * 根据字段类型和名称自动填充swagger示例值
     *
     * @param javaTypeShortName
     * @param name
     * @return
     */
    public static String getFieldExample(String javaTypeShortName, String name) {
        String fieldExample;
        switch (javaTypeShortName) {
            case "Byte":
                fieldExample = "1";
                break;
            case "Short":
                fieldExample = "1000";
                break;
            case "Integer":
                fieldExample = "100000";
                break;
            case "Long":
                if ("mobile".equals(name)) {
                    fieldExample = "18511122233";
                } else {
                    fieldExample = "1";
                }

                break;
            case "Double":
            case "Float":
                fieldExample = "1.23";
                break;
            case "Date":
                fieldExample = date2Str(new Date(), "yyyy-MM-dd HH:mm:ss");
                break;
            case "String":
                fieldExample = "helper";
                break;
            default:
                fieldExample = javaTypeShortName;
        }
        return fieldExample;
    }


    public static void main(String[] args) {
        System.out.println(firstCharToLowCase("L"));
        System.out.println(firstCharToUpperCase("l"));
    }
}
