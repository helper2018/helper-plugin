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

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.Date;

import static org.helper.plugins.utils.DateUtil.date2Str;

/**
 * <p>
 * 类名:JavaDocUtil.
 * 2021/10/26 11:48
 * <p>
 *
 * @author zyw
 * @version 1.0.0
 */
public final class JavaDocUtil {

    /**
     * 增加文件功能注释
     *
     * @param javaElement
     * @param title
     * @param author
     */
    private static void addFileJavaDoc(JavaElement javaElement, String title, String author) {
        javaElement.addJavaDocLine("/**");
        javaElement.addJavaDocLine(" * <p>");
        javaElement.addJavaDocLine(String.format(" * %s", title));
        javaElement.addJavaDocLine(String.format(" * %s", date2Str(new Date(), "yyyy/MM/dd HH:mm")));
        javaElement.addJavaDocLine(" * <p>");
        javaElement.addJavaDocLine(" *");
        javaElement.addJavaDocLine(String.format(" * @author %s", author));
        javaElement.addJavaDocLine(" * @version 1.0.0");
        javaElement.addJavaDocLine(" */");
    }

    /**
     * 增加 class 文件版权说明和注释
     *
     * @param clazz
     * @param author
     */
    public static void addClassFileJavaDoc(TopLevelClass clazz, String author, String copyright) {
        if (StringUtil.hasText(copyright)) {
            clazz.addFileCommentLine(copyright);
        }
        addFileJavaDoc(clazz, String.format("类名:%s", clazz.getType().getShortName()), author);
    }

    /**
     * 增加 interface 文件版权说明和注释
     *
     * @param someInterface
     * @param author
     */
    public static void addInterfaceFileJavaDoc(Interface someInterface, String author, String copyright) {
        if (StringUtil.hasText(copyright)) {
            someInterface.addFileCommentLine(copyright);
        }
        addFileJavaDoc(someInterface, String.format("接口名:%s", someInterface.getType().getShortName()), author);
    }

    /**
     * 增加字段注释
     *
     * @param field
     * @param introspectedColumn
     */
    public static void addFieldJavaDoc(Field field, IntrospectedColumn introspectedColumn) {
        field.addJavaDocLine("/**");
        String remarks = introspectedColumn.getRemarks();
        if (StringUtil.hasText(remarks)) {
            String[] remarkLines = remarks.split(System.getProperty("line.separator"));
            for (String remarkLine : remarkLines) {
                field.addJavaDocLine(" * " + remarkLine);
            }
        } else {
            field.addJavaDocLine(" * " + introspectedColumn.getActualColumnName());
        }
        field.addJavaDocLine(" */");
    }

    /**
     * 生成DTO Field的注解
     *
     * @param field
     * @param introspectedColumn
     */
    public static void addDTOFieldJavaDoc(Field field, IntrospectedColumn introspectedColumn) {
        String remarks = introspectedColumn.getRemarks();
        if (!StringUtil.hasText(remarks)) {
            remarks = introspectedColumn.getActualColumnName();
        }
        String propertyJavaType = introspectedColumn
                .getFullyQualifiedJavaType().getShortName();
        field.addAnnotation(String.format("@ApiModelProperty(value = \"%s\", example = \"%s\", dataType = \"%s\", required = %s)",
                "String".equals(propertyJavaType) ? String.format("%s(maxLen=%d)", remarks, introspectedColumn.getLength()) : remarks,
                StringUtil.getFieldExample(propertyJavaType, field.getName()),
                propertyJavaType, introspectedColumn.isNullable() ? "false" : "true"));
    }
}
