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

package org.helper.plugins.java;

import org.helper.plugins.common.JavaField;
import org.helper.plugins.utils.FileUtil;
import org.helper.plugins.utils.StringUtil;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.util.JavaBeansUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 类名:ClassPlugin.
 * 2021/10/20 17:03
 * <p>
 *
 * @author zyw
 * @version 1.0.0
 */
public class ClassPlugin {

    public static void main(String[] args) throws Exception {
        // 1. 生成类的Api Model
        String className = "PasswordResetReqDTO";
        generatorApiModel(className);

        // 2. 生成类A转类B的方法
        String classNameA = "PasswordResetBO";
        String classNameB = "AppUserBO";
        classAToClassB(classNameA, classNameB);

        // 3. 生成类A带类C的参数的构造方法
        String classNameC = "AppUserBO";
        String classNameD = "RegisterBO";
        classAByClassB(classNameC, classNameD);

        // 4. 生成类A通过类B赋值的方法(不推荐使用，使用2、3替换)
        String classNameE = "RegisterReqDTO";
        String classNameF = "RegisterBO";
        copyClassB2ClassA(classNameE, classNameF);
    }

    public static void generatorApiModel(String className) throws Exception {
        List<JavaField> javaFields = getClassField(className);
        generatorApiModelPropertyAnnotation(javaFields, className);
    }

    public static void classAToClassB(String classNameA, String classNameB) throws Exception {
        List<JavaField> javaAFields = getClassField(classNameA);
        if (null == javaAFields) {
            return;
        }
        List<JavaField> javaBFields = getClassField(classNameB);
        if (null == javaBFields) {
            return;
        }
        System.out.println(String.format("    public %s to%s() {", classNameB, classNameB));
        System.out.println(String.format("        %s %s = new %s();", classNameB, StringUtil.firstCharToLowCase(classNameB), classNameB));
        for (int i = 0; i < javaBFields.size(); i++) {
            String fieldName = javaBFields.get(i).getName();
            for (int j = 0; j < javaAFields.size(); j++) {
                if (fieldName.equals(javaAFields.get(j).getName())) {
                    System.out.println(String.format("        %s.%s(this.%s);",
                            StringUtil.firstCharToLowCase(classNameB),
                            JavaBeansUtil.getSetterMethodName(fieldName),
                            fieldName));
                    break;
                }
            }
        }
        System.out.println(String.format("        return %s;", StringUtil.firstCharToLowCase(classNameB)));
        System.out.println("    }");
    }

    public static void classAByClassB(String classNameA, String classNameB) throws Exception {
        List<JavaField> javaAFields = getClassField(classNameA);
        if (null == javaAFields) {
            return;
        }
        List<JavaField> javaBFields = getClassField(classNameB);
        if (null == javaBFields) {
            return;
        }
        System.out.println(String.format("    public %s(%s %s) {", classNameA, classNameB, StringUtil.firstCharToLowCase(classNameB)));
        for (int i = 0; i < javaBFields.size(); i++) {
            String fieldName = javaBFields.get(i).getName();
            for (int j = 0; j < javaAFields.size(); j++) {
                if (fieldName.equals(javaAFields.get(j).getName())) {
                    if (javaAFields.get(j).getType().endsWith("DTO") &&
                            javaBFields.get(i).getType().endsWith("BO")) {
                        System.out.println(String.format("        this.%s = new %s(%s.%s);",
                                fieldName,
                                javaAFields.get(j).getType(),
                                StringUtil.firstCharToLowCase(classNameB),
                                JavaBeansUtil.getGetterMethodName(fieldName,
                                        javaBFields.get(i).getType().equalsIgnoreCase("boolean")
                                                ? FullyQualifiedJavaType.getBooleanPrimitiveInstance() :
                                                FullyQualifiedJavaType.getStringInstance()) + "()"));
                    } else {
                        System.out.println(String.format("        this.%s = %s.%s;",
                                fieldName, StringUtil.firstCharToLowCase(classNameB),
                                JavaBeansUtil.getGetterMethodName(fieldName,
                                        javaBFields.get(i).getType().equalsIgnoreCase("boolean")
                                                ? FullyQualifiedJavaType.getBooleanPrimitiveInstance() :
                                                FullyQualifiedJavaType.getStringInstance()) + "()"));
                    }
                    break;
                }
            }
        }
        System.out.println("    }");
    }

    public static void copyClassB2ClassA(String classNameA, String classNameB) throws Exception {
        List<JavaField> javaAFields = getClassField(classNameA);
        if (null == javaAFields) {
            return;
        }
        List<JavaField> javaBFields = getClassField(classNameB);
        if (null == javaBFields) {
            return;
        }
        for (int i = 0; i < javaBFields.size(); i++) {
            String fieldName = javaBFields.get(i).getName();
            for (int j = 0; j < javaAFields.size(); j++) {
                if (fieldName.equals(javaAFields.get(j).getName())) {
                    System.out.println(String.format("        %s.%s(%s.%s);",
                            StringUtil.firstCharToLowCase(classNameA),
                            JavaBeansUtil.getSetterMethodName(fieldName),
                            StringUtil.firstCharToLowCase(classNameB),
                            JavaBeansUtil.getGetterMethodName(fieldName, FullyQualifiedJavaType
                                    .getStringInstance()) + "()"));
                    break;
                }
            }
        }
    }

    private static List<JavaField> getClassField(String className) throws Exception {
        String userDirPath = System.getProperty("user.dir");
        String projectModule = "arrange-service";
        if (!className.endsWith("BO")) {
            projectModule = "arrange-app";
        }
        String dirPath = userDirPath + "/" + projectModule;
        File fileByName = FileUtil.getFileByName(new File(dirPath), String.format("%s.java", className));
        if (null == fileByName) {
            projectModule = "arrange-cmp";
            dirPath = userDirPath + "/" + projectModule;
            fileByName = FileUtil.getFileByName(new File(dirPath), String.format("%s.java", className));
        }
        if (null == fileByName) {
            dirPath = userDirPath;
            fileByName = FileUtil.getFileByName(new File(dirPath), String.format("%s.java", className));
        }
        if (null == fileByName) {
            System.out.println(String.format("请确认文件 【%s.java】 是否存在", className));
            return null;
        }
        System.out.println(fileByName);
        List<JavaField> javaFields = getJavaFiled(fileByName);
        return javaFields;
    }

    private static List<JavaField> getJavaFiled(File javaFile) throws Exception {
        //定义一个fileReader对象，用来初始化BufferedReader
        FileReader reader = new FileReader(javaFile);
        //new一个BufferedReader对象，将文件内容读取到缓存
        BufferedReader bufferedReader = new BufferedReader(reader);
        //逐行读取文件内容，不读取换行符和末尾的空格
        List<JavaField> javaFields = new ArrayList<>(16);
        bufferedReader.lines().forEach(line -> {
            line = line.trim();
            if (line.startsWith("private ") && line.endsWith(";")
                    && !line.contains(" static ") && !line.contains(" final ")) {
                line = line.substring(0, line.length() - 1).trim().replaceAll(" +", " ");
                String[] strs = line.split(" ");
                if (strs.length == 3) {
                    javaFields.add(new JavaField(strs[2], strs[1], line + ";"));
                }
            }
        });
        bufferedReader.close();
        return javaFields;
    }

    private static void generatorApiModelPropertyAnnotation(List<JavaField> javaFields,
                                                            String javaName) {
        System.out.println("import io.swagger.annotations.ApiModel;");
        System.out.println("import io.swagger.annotations.ApiModelProperty;");
        System.out.println(String.format("@ApiModel(\"%s\")", javaName));
        for (JavaField javaField : javaFields) {
            System.out.println(String.format("    @ApiModelProperty(value = \"%s\", example = \"%s\", dataType = \"%s\", required = %s)"
                    , javaField.getName(),
                    StringUtil.getFieldExample(javaField.getType(), javaField.getName()),
                    javaField.getType(), "true")
            );
            System.out.println("    " + javaField.getField());
        }
    }
}
