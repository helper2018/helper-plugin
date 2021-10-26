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

package org.helper.plugins.mybatis_generator;

import org.helper.plugins.utils.StringUtil;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;
import org.mybatis.generator.internal.util.JavaBeansUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.helper.plugins.utils.FileUtil.createDir;
import static org.helper.plugins.utils.JavaDocUtil.*;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * <p>
 * 类名:ServiceAndControllerPlugin.
 * 2021/10/15 15:00
 * <p>
 *
 * @author zyw
 * @version 1.0.0
 */
public class ServiceAndControllerPlugin extends PluginAdapter {

    private final static String BASE_PACKAGE = "org.helper.base";

    private final static String STRING_UTIL_PACKAGE = BASE_PACKAGE + ".utils.StringUtil";
    private final static String DATE_UTIL_PACKAGE = BASE_PACKAGE + ".utils.DateUtil";

    private final static String RESULT_BO_PACKAGE = BASE_PACKAGE + ".common.bo.ResultBO";
    private final static String RESULT_RESP_DTO_PACKAGE = BASE_PACKAGE + ".common.resp.ResultRespDTO";

    private final static String BASE_QUERY_BO_PACKAGE = BASE_PACKAGE + ".common.bo.BaseQueryBO";
    private final static String BASE_QUERY_REQ_DTO_PACKAGE = BASE_PACKAGE + ".common.req.BaseQueryReqDTO";

    private final static String PAGING_QUERY_BO_PACKAGE = BASE_PACKAGE + ".common.bo.PagingQueryBO";
    private final static String PAGING_QUERY_REQ_DTO_PACKAGE = BASE_PACKAGE + ".common.req.PagingQueryReqDTO";
    private final static String PAGING_QUERY_RESP_DTO_PACKAGE = BASE_PACKAGE + ".common.resp.PagingQueryRespDTO";

    private final static String ID_REQ_DTO_PACKAGE = BASE_PACKAGE + ".common.req.IdReqDTO";

    private final static String LOGIN_USER_PACKAGE = BASE_PACKAGE + ".common.LoginUser";

    private final static String STATUS_ENUMS_PACKAGE = BASE_PACKAGE + ".enums.StatusEnum";


    /**
     * 版权信息
     */
    private String copyright;

    /**
     * 作者
     */
    private String author;

    /**
     * 生成文件路径
     */
    private String targetProject;

    /**
     * service接口的父接口
     */
    private String superServiceInterface;

    /**
     * Dao接口的父接口
     */
    private String superDaoInterface;

    /**
     * service实现类的父类
     */
    private String superServiceImpl;

    /**
     * controller类的父类
     */
    private String superController;

    /**
     * 是否生成service
     */
    private Boolean enableService;

    /**
     * 是否生成controller
     */
    private Boolean enableController;
    /**
     * dataobject后缀
     */
    String doSuffix;
    /**
     * bo后缀
     */
    String boSuffix;
    /**
     * reqDTO后缀
     */
    String reqDTOSuffix;
    /**
     * resp后缀
     */
    String respDTOSuffix;
    /**
     * dao后缀
     */
    String daoSuffix;

    /***************************上面是可配置的属性********************************/

    /**
     * service接口包名
     */
    private String servicePackage;

    /**
     * service实现类包名
     */
    private String serviceImplPackage;

    /**
     * controller类包名
     */
    private String controllerPackage;

    /**
     * do类包名
     */
    private String doPackage;

    /**
     * bo类包名
     */
    private String boPackage;

    /**
     * dto请求类包名
     */
    private String reqDTOPackage;

    /**
     * dto响应类包名
     */
    private String respDTOPackage;

    /**
     * @param 对象名
     */
    private String objectName;

    /**
     * service 模块名
     */
    private String appTargetProject;

    /**
     * 数据库列转java对象属性
     *
     * @param introspectedColumn
     * @return
     */
    private static Field getBasicJavaBeansField(IntrospectedColumn introspectedColumn) {
        FullyQualifiedJavaType propertyJavaType = introspectedColumn
                .getFullyQualifiedJavaType();
        String property = introspectedColumn.getJavaProperty();
        Field field = new Field(property, propertyJavaType);
        field.setVisibility(JavaVisibility.PRIVATE);
        return field;
    }

    @Override
    public boolean validate(List<String> warnings) {
        targetProject = properties.getProperty("targetProject");
        if (!StringUtil.hasText(targetProject)) {
            targetProject = String.format("%s%s%s",
                    System.getProperty("user.dir"), File.separator, "mybatis");
        }

        appTargetProject = String.format("%s%sapp", targetProject, File.separator);
        targetProject = String.format("%s%sservice", targetProject, File.separator);
        createDir(targetProject);
        createDir(appTargetProject);
        superServiceInterface = properties.getProperty("superServiceInterface");
        superServiceImpl = properties.getProperty("superServiceImpl");
        superDaoInterface = properties.getProperty("superDaoInterface");
        superController = properties.getProperty("superController");
        enableService = "TRUE".equalsIgnoreCase(properties.getProperty("enableService"));
        enableController = "TRUE".equalsIgnoreCase(properties.getProperty("enableController"));
        doSuffix = properties.getProperty("doSuffix", "DO");
        boSuffix = properties.getProperty("boSuffix", "BO");
        reqDTOSuffix = properties.getProperty("reqDTOSuffix", "ReqDTO");
        respDTOSuffix = properties.getProperty("respDTOSuffix", "RespDTO");
        daoSuffix = properties.getProperty("daoSuffix", "DAO");
        this.copyright = context.getProperty("copyright");
        if (!StringUtil.hasText(this.copyright)) {
            this.author = properties.getProperty("copyright");
        }
        if ("default".equals(this.copyright)) {
            this.copyright = "/*\n" +
                    " * Copyright 2021-2031 the original author or authors.\n" +
                    " *\n" +
                    " * Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    " * you may not use this file except in compliance with the License.\n" +
                    " * You may obtain a copy of the License at\n" +
                    " *\n" +
                    " *      https://www.apache.org/licenses/LICENSE-2.0\n" +
                    " *\n" +
                    " * Unless required by applicable law or agreed to in writing, software\n" +
                    " * distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    " * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    " * See the License for the specific language governing permissions and\n" +
                    " * limitations under the License.\n" +
                    " */";
        }
        this.author = context.getProperty("author");
        if (!StringUtil.hasText(this.author)) {
            this.author = properties.getProperty("author", "作者");
        }
        return true;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        // 1.生成文件路径同步
        if ("targetProject".equals(context.getJavaModelGeneratorConfiguration().getTargetProject())) {
            context.getJavaModelGeneratorConfiguration().setTargetProject(targetProject);
        }
        if ("targetProject".equals(context.getJavaClientGeneratorConfiguration().getTargetProject())) {
            context.getJavaClientGeneratorConfiguration().setTargetProject(targetProject);
        }
        if ("targetProject".equals(context.getSqlMapGeneratorConfiguration().getTargetProject())) {
            context.getSqlMapGeneratorConfiguration().setTargetProject(targetProject);
        }

        // 2.对象名，去掉表名前缀yb
        String tableNamePrefix = properties.getProperty("tableNamePrefix");
        String tableName = introspectedTable.getTableConfiguration().getTableName();
        if (StringUtil.hasText(tableNamePrefix) && tableName.startsWith(tableNamePrefix)) {
            tableName = tableName.substring(tableNamePrefix.length());
            if (tableName.startsWith("_")) {
                tableName = tableName.substring(1);
            }
        }
        String[] strs = tableName.split("_");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strs.length; i++) {
            sb.append(StringUtil.firstCharToUpperCase(strs[i]));
        }
        objectName = sb.toString();

        // 3.对象所属模块名
        String serviceModulePrefix = properties.getProperty("serviceModulePrefix");
        String serviceModuleName = introspectedTable.getTableConfiguration().getProperty("serviceModuleName");
        if (!StringUtil.hasText(serviceModuleName)) {
            serviceModuleName = tableName;
        }

        doPackage = String.format("%s.%s.dataobject", serviceModulePrefix, serviceModuleName);

        // 4.修改introspectedTableMyBatis3的属性。对象名
        IntrospectedTableMyBatis3Impl introspectedTableMyBatis3 = (IntrospectedTableMyBatis3Impl) introspectedTable;
        introspectedTableMyBatis3.setPrimaryKeyType(String.format("%s.%sKey", doPackage, objectName));
        introspectedTableMyBatis3.setBaseRecordType(String.format("%s.%s%s", doPackage, objectName, doSuffix));
        introspectedTableMyBatis3.setRecordWithBLOBsType(String.format("%s.%sWithBLOBs", doPackage, objectName));
        introspectedTableMyBatis3.setExampleType(String.format("%s.%sExample", doPackage, objectName));
        introspectedTableMyBatis3.setMyBatis3XmlMapperPackage(String.format("mapper.%s", serviceModuleName));
        introspectedTableMyBatis3.setMyBatis3XmlMapperFileName(String.format("%sMapper.xml", objectName));
        introspectedTableMyBatis3.setMyBatis3JavaMapperType(String.format("%s.%s.dao.I%s%s", serviceModulePrefix, serviceModuleName, objectName, daoSuffix));
        introspectedTableMyBatis3.setMyBatis3FallbackSqlMapNamespace(String.format("mapper.%s.%sMapper", serviceModuleName, objectName));
        introspectedTableMyBatis3.setMyBatis3SqlProviderType(String.format("%s.%s.dao.%sSqlProvider", serviceModulePrefix, serviceModuleName, objectName));
        introspectedTableMyBatis3.setMyBatisDynamicSqlSupportType(String.format("%s.%s.dao.%sDynamicSqlSupport", serviceModulePrefix, serviceModuleName, objectName));
        introspectedTableMyBatis3.setKotlinRecordType(String.format("%s.%sRecord", doPackage, objectName));

        // 5..service的包路径
        servicePackage = String.format("%s.%s.service", serviceModulePrefix, serviceModuleName);
        serviceImplPackage = String.format("%s.impl", servicePackage, serviceModuleName);
        boPackage = String.format("%s.%s.bo", serviceModulePrefix, serviceModuleName);

        // 6.controller的包路径
        String appModulePrefix = properties.getProperty("appModulePrefix");
        String appModuleName = introspectedTable.getTableConfiguration().getProperty("appModuleName");
        if (!StringUtil.hasText(appModuleName)) {
            appModuleName = serviceModuleName;
        }
        controllerPackage = String.format("%s.%s.controller", appModulePrefix, appModuleName);
        reqDTOPackage = String.format("%s.%s.dto.req", appModulePrefix, appModuleName);
        respDTOPackage = String.format("%s.%s.dto.resp", appModulePrefix, appModuleName);
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        List<GeneratedJavaFile> answer = new ArrayList<>();

        String templateMethod = introspectedTable.getTableConfiguration().getProperty("templateMethod");
        boolean listObject = templateMethod.contains("list");
        if (enableService) {
            // BO
            answer.add(generatedBOJavaFile(introspectedTable));
            List<Field> fields = null;
            if (listObject) {
                // Query BO
                GeneratedJavaFile generatedJavaFile = generatedQueryBOJavaFile(introspectedTable);
                TopLevelClass boQueryClazz = (TopLevelClass) generatedJavaFile.getCompilationUnit();
                fields = boQueryClazz.getFields();
                answer.add(generatedJavaFile);
            }
            // service
            answer.add(generateServiceInterface(introspectedTable));
            answer.add(generateServiceImpl(introspectedTable, fields));
        }
        if (enableController) {
            // ReqDTO
            answer.add(generatedReqDTOJavaFile(introspectedTable));
            // RespDTO
            answer.add(generatedRespDTOJavaFile(introspectedTable));
            if (listObject) {
                // Query ReqDTO
                answer.add(generatedQueryReqDTOJavaFile(introspectedTable));
            }
            // controller
            answer.add(generateController(introspectedTable));
        }
        return answer;
    }

    /**
     * 生成BO对应的字段
     *
     * @param clazz
     * @param introspectedTable
     * @return
     */
    private List<Field> generatedBOJavaFiled(TopLevelClass clazz, IntrospectedTable introspectedTable) {
        List<IntrospectedColumn> introspectedColumns = introspectedTable.getAllColumns();
        List<Field> fields = new ArrayList<>(introspectedColumns.size());
        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            Field field = getBasicJavaBeansField(introspectedColumn);
            fields.add(field);
            addFieldJavaDoc(field, introspectedColumn);
            clazz.addField(field);
            clazz.addImportedType(field.getType());
        }
        return fields;
    }

    /**
     * 生成Query BO对应的字段
     *
     * @param clazz
     * @param introspectedTable
     * @return
     */
    private List<Field> generatedQueryBOJavaFiled(TopLevelClass clazz, IntrospectedTable introspectedTable) {
        List<IntrospectedColumn> introspectedColumns = introspectedTable.getAllColumns();
        List<Field> fields = new ArrayList<>(introspectedColumns.size());
        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            String javaProperty = introspectedColumn.getJavaProperty();
            String columnName = introspectedColumn.getActualColumnName();
            if (columnName.contains("id") ||
                    columnName.contains("icon") ||
                    columnName.contains("url") |
                            "status".equals(javaProperty) ||
                    "createBy".equals(javaProperty) ||
                    "createTime".equals(javaProperty) ||
                    "lastModifyBy".equals(javaProperty) ||
                    "lastModifyTime".equals(javaProperty) ||
                    "password".equals(javaProperty) ||
                    "salt".equals(javaProperty)) {
                continue;
            }
            Field field = getBasicJavaBeansField(introspectedColumn);
            fields.add(field);
            addFieldJavaDoc(field, introspectedColumn);
            clazz.addField(field);
            clazz.addImportedType(field.getType());
        }
        return fields;
    }

    /**
     * 生成DTO对应的字段
     *
     * @param clazz
     * @param introspectedTable
     */
    private List<Field> generatedDTOJavaFiled(TopLevelClass clazz, IntrospectedTable introspectedTable) {
        List<IntrospectedColumn> introspectedColumns = introspectedTable.getAllColumns();
        List<Field> fields = new ArrayList<>(introspectedColumns.size());
        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            String javaProperty = introspectedColumn.getJavaProperty();
            if ("password".equals(javaProperty) ||
                    "salt".equals(javaProperty)) {
                continue;
            }
            Field field = getBasicJavaBeansField(introspectedColumn);
            fields.add(field);
            addDTOFieldJavaDoc(field, introspectedColumn);
            clazz.addField(field);
            clazz.addImportedType(field.getType());
        }
        return fields;
    }

    /**
     * 生成query DTO对应的字段
     *
     * @param clazz
     * @param introspectedTable
     */
    private List<Field> generatedQueryReqDTOJavaFiled(TopLevelClass clazz, IntrospectedTable introspectedTable) {
        List<IntrospectedColumn> introspectedColumns = introspectedTable.getAllColumns();
        List<Field> fields = new ArrayList<>(introspectedColumns.size());
        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            String javaProperty = introspectedColumn.getJavaProperty();
            String columnName = introspectedColumn.getActualColumnName();
            if (columnName.contains("id") ||
                    columnName.contains("icon") ||
                    columnName.contains("url") ||
                    "status".equals(javaProperty) ||
                    "createBy".equals(javaProperty) ||
                    "createTime".equals(javaProperty) ||
                    "lastModifyBy".equals(javaProperty) ||
                    "lastModifyTime".equals(javaProperty) ||
                    "password".equals(javaProperty) ||
                    "salt".equals(javaProperty)) {
                continue;
            }
            Field field = getBasicJavaBeansField(introspectedColumn);
            fields.add(field);
            addDTOFieldJavaDoc(field, introspectedColumn);
            clazz.addField(field);
            clazz.addImportedType(field.getType());
        }
        return fields;
    }

    /**
     * 生成bo类
     *
     * @param introspectedTable
     * @return
     */
    private GeneratedJavaFile generatedBOJavaFile(IntrospectedTable introspectedTable) {
        // 1.生成bo
        String boName = String.format("%s%s", objectName, boSuffix);
        String boJavaType = String.format("%s.%s", boPackage, boName);
        FullyQualifiedJavaType bo = new FullyQualifiedJavaType(boJavaType);
        TopLevelClass clazz = new TopLevelClass(bo);
        addClassFileJavaDoc(clazz, author, copyright);
        clazz.setVisibility(JavaVisibility.PUBLIC);
        clazz.addImportedType("lombok.Data");
//        clazz.addImportedType("lombok.Builder");
        clazz.addImportedType("lombok.NoArgsConstructor");
        clazz.addImportedType("lombok.AllArgsConstructor");
        clazz.addAnnotation("@Data");
//        clazz.addAnnotation("@Builder");
        clazz.addAnnotation("@NoArgsConstructor");
        clazz.addAnnotation("@AllArgsConstructor");
        // 2.创建bo字段
        List<Field> fields = generatedBOJavaFiled(clazz, introspectedTable);
        // 3.生成bo转do方法
        String doName = String.format("%s%s", objectName, doSuffix);
        String doJavaType = introspectedTable.getBaseRecordType();
        clazz.addImportedType(doJavaType);

        //描述 方法名
        Method bo2DOMethod = new Method(String.format("to%s", doName));
        //返回值
        FullyQualifiedJavaType doFullyQualifiedJavaType = new FullyQualifiedJavaType(doJavaType);
        bo2DOMethod.setReturnType(doFullyQualifiedJavaType);
        //方法体，逻辑代码
//        bo2DOMethod.addBodyLine(String.format("return %s.builder()", doName));
//        for (Field field : fields) {
//            bo2DOMethod.addBodyLine(String.format("    .%s(this.%s)", field.getName(), field.getName()));
//        }
//        bo2DOMethod.addBodyLine("    .build();");
        bo2DOMethod.addBodyLine(String.format("%s %s = new %s();"
                , doName, StringUtil.firstCharToLowCase(doName), doName));
        for (Field field : fields) {
            bo2DOMethod.addBodyLine(String.format("%s.%s(this.%s);",
                    StringUtil.firstCharToLowCase(doName),
                    JavaBeansUtil.getSetterMethodName(field.getName()),
                    field.getName()));
        }
        bo2DOMethod.addBodyLine(String.format("return %s;",
                StringUtil.firstCharToLowCase(doName)));
        //修饰符
        bo2DOMethod.setVisibility(JavaVisibility.PUBLIC);
        clazz.addMethod(bo2DOMethod);

        //描述 方法名
        Method do2BOMethod = new Method(boName);
        do2BOMethod.setConstructor(true);
        //参数 do
        do2BOMethod.addParameter(new Parameter(doFullyQualifiedJavaType, StringUtil.firstCharToLowCase(doName)));
        //返回值
        //方法体，逻辑代码
        for (Field field : fields) {
            do2BOMethod.addBodyLine(String.format("this.%s = %s.%s;",
                    field.getName(), StringUtil.firstCharToLowCase(doName),
                    JavaBeansUtil.getGetterMethodName(field.getName(), field.getType()) + "()"));
        }
        //修饰符
        do2BOMethod.setVisibility(JavaVisibility.PUBLIC);
        clazz.addMethod(do2BOMethod);

        return new GeneratedJavaFile(clazz, targetProject, context.getJavaFormatter());
    }

    /**
     * 生成bo query类
     *
     * @param introspectedTable
     * @return
     */
    private GeneratedJavaFile generatedQueryBOJavaFile(IntrospectedTable introspectedTable) {
        // 1.生成bo query
        String boQueryName = String.format("%sQuery%s", objectName, boSuffix);
        String boQueryJavaType = String.format("%s.%s", boPackage, boQueryName);
        FullyQualifiedJavaType boQuery = new FullyQualifiedJavaType(boQueryJavaType);
        TopLevelClass clazz = new TopLevelClass(boQuery);
        // 父类
        FullyQualifiedJavaType baseQueryBOFullyQualifiedJavaType = new FullyQualifiedJavaType(BASE_QUERY_BO_PACKAGE);
        clazz.addImportedType(baseQueryBOFullyQualifiedJavaType);
        clazz.setSuperClass(baseQueryBOFullyQualifiedJavaType);
        addClassFileJavaDoc(clazz, author, copyright);
        clazz.setVisibility(JavaVisibility.PUBLIC);
        clazz.addImportedType("lombok.Data");
//        clazz.addImportedType("lombok.Builder");
        clazz.addImportedType("lombok.NoArgsConstructor");
        clazz.addImportedType("lombok.AllArgsConstructor");
        clazz.addAnnotation("@Data");
//        clazz.addAnnotation("@Builder");
        clazz.addAnnotation("@NoArgsConstructor");
        clazz.addAnnotation("@AllArgsConstructor");
        // 2.创建bo字段
        List<Field> fields = generatedQueryBOJavaFiled(clazz, introspectedTable);
        return new GeneratedJavaFile(clazz, targetProject, context.getJavaFormatter());
    }

    /**
     * 生成req dto类
     *
     * @param introspectedTable
     * @return
     */
    private GeneratedJavaFile generatedReqDTOJavaFile(IntrospectedTable introspectedTable) {
        // 1.生成req dto
        String reqDTOName = String.format("%s%s", objectName, reqDTOSuffix);
        String reqDTOJavaType = String.format("%s.%s", reqDTOPackage, reqDTOName);
        FullyQualifiedJavaType reqDTO = new FullyQualifiedJavaType(reqDTOJavaType);
        TopLevelClass clazz = new TopLevelClass(reqDTO);
        addClassFileJavaDoc(clazz, author, copyright);
        clazz.setVisibility(JavaVisibility.PUBLIC);
        clazz.addImportedType("io.swagger.annotations.ApiModel");
        clazz.addImportedType("io.swagger.annotations.ApiModelProperty");
        clazz.addImportedType("lombok.Data");
//        clazz.addImportedType("lombok.Builder");
        clazz.addImportedType("lombok.NoArgsConstructor");
//        clazz.addImportedType("lombok.AllArgsConstructor");
        clazz.addAnnotation("@Data");
//        clazz.addAnnotation("@Builder");
        clazz.addAnnotation("@NoArgsConstructor");
//        clazz.addAnnotation("@AllArgsConstructor");
        clazz.addAnnotation(String.format("@ApiModel(\"%s\")", reqDTOName));
        // 2.创建dto字段
        List<Field> fields = generatedDTOJavaFiled(clazz, introspectedTable);
        // 3.生成dto转bo方法
        String boName = String.format("%s%s", objectName, boSuffix);
        String boJavaType = String.format("%s.%s", boPackage, boName);
        clazz.addImportedType(boJavaType);

        //描述 方法名
        Method dto2BOMethod = new Method(String.format("to%s", boName));
        //返回值
        FullyQualifiedJavaType methodReturnType = new FullyQualifiedJavaType(boJavaType);
        dto2BOMethod.setReturnType(methodReturnType);
        //方法体，逻辑代码
//        dto2BOMethod.addBodyLine(String.format("return %s.builder()", boName));
//        for (Field field : fields) {
//            dto2BOMethod.addBodyLine(String.format("    .%s(this.%s)", field.getName(), field.getName()));
//        }
//        dto2BOMethod.addBodyLine("    .build();");
        dto2BOMethod.addBodyLine(String.format("%s %s = new %s();"
                , boName, StringUtil.firstCharToLowCase(boName), boName));
        for (Field field : fields) {
            dto2BOMethod.addBodyLine(String.format("%s.%s(this.%s);",
                    StringUtil.firstCharToLowCase(boName),
                    JavaBeansUtil.getSetterMethodName(field.getName()),
                    field.getName()));
        }
        dto2BOMethod.addBodyLine(String.format("return %s;",
                StringUtil.firstCharToLowCase(boName)));

        //修饰符
        dto2BOMethod.setVisibility(JavaVisibility.PUBLIC);
        clazz.addMethod(dto2BOMethod);
        return new GeneratedJavaFile(clazz, appTargetProject, context.getJavaFormatter());
    }

    /**
     * 生成query req dto类
     *
     * @param introspectedTable
     * @return
     */
    private GeneratedJavaFile generatedQueryReqDTOJavaFile(IntrospectedTable introspectedTable) {
        // 1.生成req dto
        String queryReqDTOName = String.format("%sQuery%s", objectName, reqDTOSuffix);
        String queryReqDTOJavaType = String.format("%s.%s", reqDTOPackage, queryReqDTOName);
        FullyQualifiedJavaType queryReqDTO = new FullyQualifiedJavaType(queryReqDTOJavaType);
        TopLevelClass clazz = new TopLevelClass(queryReqDTO);
        FullyQualifiedJavaType baseQueryReqDTOFullyQualifiedJavaType = new FullyQualifiedJavaType(BASE_QUERY_REQ_DTO_PACKAGE);
        clazz.addImportedType(baseQueryReqDTOFullyQualifiedJavaType);
        clazz.setSuperClass(baseQueryReqDTOFullyQualifiedJavaType);
        addClassFileJavaDoc(clazz, author, copyright);
        clazz.setVisibility(JavaVisibility.PUBLIC);
        clazz.addImportedType("io.swagger.annotations.ApiModel");
        clazz.addImportedType("io.swagger.annotations.ApiModelProperty");
        clazz.addImportedType("lombok.Data");
//        clazz.addImportedType("lombok.Builder");
        clazz.addImportedType("lombok.NoArgsConstructor");
//        clazz.addImportedType("lombok.AllArgsConstructor");
        clazz.addAnnotation("@Data");
//        clazz.addAnnotation("@Builder");
        clazz.addAnnotation("@NoArgsConstructor");
//        clazz.addAnnotation("@AllArgsConstructor");
        clazz.addAnnotation(String.format("@ApiModel(\"%s\")", queryReqDTOName));
        // 2.创建dto字段
        List<Field> fields = generatedQueryReqDTOJavaFiled(clazz, introspectedTable);
        // 3.生成dto转bo方法
        String boQueryName = String.format("%sQuery%s", objectName, boSuffix);
        String boQueryJavaType = String.format("%s.%s", boPackage, boQueryName);
        clazz.addImportedType(boQueryJavaType);

        //描述 方法名
        Method dto2BOMethod = new Method(String.format("to%s", boQueryName));
        //返回值
        FullyQualifiedJavaType methodReturnType = new FullyQualifiedJavaType(boQueryJavaType);
        dto2BOMethod.setReturnType(methodReturnType);
        //方法体，逻辑代码
        dto2BOMethod.addBodyLine(String.format("%s %s = new %s();"
                , boQueryName, StringUtil.firstCharToLowCase(boQueryName), boQueryName));
        for (Field field : fields) {
            dto2BOMethod.addBodyLine(String.format("%s.%s(this.%s);",
                    StringUtil.firstCharToLowCase(boQueryName),
                    JavaBeansUtil.getSetterMethodName(field.getName()),
                    field.getName()));
        }
        dto2BOMethod.addBodyLine(String.format("return %s;",
                StringUtil.firstCharToLowCase(boQueryName)));

        //修饰符
        dto2BOMethod.setVisibility(JavaVisibility.PUBLIC);
        clazz.addMethod(dto2BOMethod);
        return new GeneratedJavaFile(clazz, appTargetProject, context.getJavaFormatter());
    }

    /**
     * 生成resp dto类
     *
     * @param introspectedTable
     * @return
     */
    private GeneratedJavaFile generatedRespDTOJavaFile(IntrospectedTable introspectedTable) {
        // 1.生成resp dto
        String respDTOName = String.format("%s%s", objectName, respDTOSuffix);
        String respDTOJavaType = String.format("%s.%s", respDTOPackage, respDTOName);
        FullyQualifiedJavaType respDTO = new FullyQualifiedJavaType(respDTOJavaType);
        TopLevelClass clazz = new TopLevelClass(respDTO);
        addClassFileJavaDoc(clazz, author, copyright);
        clazz.setVisibility(JavaVisibility.PUBLIC);
        clazz.addImportedType("io.swagger.annotations.ApiModel");
        clazz.addImportedType("io.swagger.annotations.ApiModelProperty");
        clazz.addImportedType("lombok.Data");
//        clazz.addImportedType("lombok.Builder");
        clazz.addImportedType("lombok.NoArgsConstructor");
        clazz.addImportedType("lombok.AllArgsConstructor");
        clazz.addAnnotation("@Data");
//        clazz.addAnnotation("@Builder");
        clazz.addAnnotation("@NoArgsConstructor");
        clazz.addAnnotation("@AllArgsConstructor");
        clazz.addAnnotation(String.format("@ApiModel(\"%s\")", respDTOName));
        // 2.创建dto字段
        List<Field> fields = generatedDTOJavaFiled(clazz, introspectedTable);
        // 3.生成dto转bo方法
        String boName = String.format("%s%s", objectName, boSuffix);
        String boJavaType = String.format("%s.%s", boPackage, boName);
        FullyQualifiedJavaType boFullyQualifiedJavaType = new FullyQualifiedJavaType(boJavaType);
        clazz.addImportedType(boFullyQualifiedJavaType);

        //描述 方法名
        Method bo2DTOMethod = new Method(respDTOName);
        bo2DTOMethod.setConstructor(true);
        //参数 bo
        bo2DTOMethod.addParameter(new Parameter(boFullyQualifiedJavaType, StringUtil.firstCharToLowCase(boName)));
        //返回值
        //方法体，逻辑代码
        for (Field field : fields) {
            // 敏感字段不支持直接返回前端
            if (field.getName().equalsIgnoreCase("password")
                    || field.getName().equalsIgnoreCase("salt")) {
                continue;
            }
            bo2DTOMethod.addBodyLine(String.format("this.%s = %s.%s;",
                    field.getName(), StringUtil.firstCharToLowCase(boName),
                    JavaBeansUtil.getGetterMethodName(field.getName(), field.getType()) + "()"));
        }

        //修饰符
        bo2DTOMethod.setVisibility(JavaVisibility.PUBLIC);
        clazz.addMethod(bo2DTOMethod);
        return new GeneratedJavaFile(clazz, appTargetProject, context.getJavaFormatter());
    }

    /**
     * 生成service接口
     *
     * @param introspectedTable
     * @return
     */
    private GeneratedJavaFile generateServiceInterface(IntrospectedTable introspectedTable) {
        String serviceInterfaceJavaType = String.format("%s.I%sService", servicePackage, objectName);

        FullyQualifiedJavaType service = new FullyQualifiedJavaType(serviceInterfaceJavaType);
        Interface serviceInterface = new Interface(service);
        addInterfaceFileJavaDoc(serviceInterface, author, copyright);
        serviceInterface.setVisibility(JavaVisibility.PUBLIC);
        // 添加父接口
        if (stringHasValue(superServiceInterface)) {
            String superServiceInterfaceName = superServiceInterface.substring(superServiceInterface.lastIndexOf(".") + 1);
            serviceInterface.addImportedType(new FullyQualifiedJavaType(superServiceInterface));
            serviceInterface.addImportedType(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
            serviceInterface.addSuperInterface(new FullyQualifiedJavaType(superServiceInterfaceName + "<" + String.format("%s%s", objectName, boSuffix) + ">"));
        }
        // 添加增删改查接口
        String boName = String.format("%s%s", objectName, boSuffix);
        String boJavaType = String.format("%s.%s", boPackage, boName);
        FullyQualifiedJavaType boFullyQualifiedJavaType = new FullyQualifiedJavaType(boJavaType);
        serviceInterface.addImportedType(boFullyQualifiedJavaType);

        FullyQualifiedJavaType resultBOFullyQualifiedJavaType = new FullyQualifiedJavaType(RESULT_BO_PACKAGE);
        serviceInterface.addImportedType(resultBOFullyQualifiedJavaType);

        String templateMethod = introspectedTable.getTableConfiguration().getProperty("templateMethod");
        FullyQualifiedJavaType methodReturnType = null;

        if (templateMethod.contains("addOne")) {
            //描述 新增
            Method addMethod = new Method("addOne");
            //参数
            addMethod.addParameter(new Parameter(boFullyQualifiedJavaType, StringUtil.firstCharToLowCase(boName)));
            //返回值
            methodReturnType = new FullyQualifiedJavaType(RESULT_BO_PACKAGE);
            methodReturnType.addTypeArgument(boFullyQualifiedJavaType);
            addMethod.setReturnType(methodReturnType);
            //注解
            addMethod.addJavaDocLine("/**");
            addMethod.addJavaDocLine(" * 新增");
            addMethod.addJavaDocLine(" *");
            addMethod.addJavaDocLine(" * @param " + StringUtil.firstCharToLowCase(boName));
            addMethod.addJavaDocLine(" * @return 是否新增成功");
            addMethod.addJavaDocLine(" */");
            //修饰符
            addMethod.setVisibility(JavaVisibility.DEFAULT);
            addMethod.setAbstract(true);
            serviceInterface.addMethod(addMethod);
        }

        if (templateMethod.contains("deleteById")) {
            //描述 删除
            Method deleteMethod = new Method("deleteById");
            //参数
            deleteMethod.addParameter(new Parameter(new FullyQualifiedJavaType("java.lang.Long"), "id"));
            //返回值
            methodReturnType = new FullyQualifiedJavaType(RESULT_BO_PACKAGE);
            deleteMethod.setReturnType(methodReturnType);
            //注解
            deleteMethod.addJavaDocLine("/**");
            deleteMethod.addJavaDocLine(" * 删除");
            deleteMethod.addJavaDocLine(" *");
            deleteMethod.addJavaDocLine(" * @param id");
            deleteMethod.addJavaDocLine(" * @return 是否删除成功");
            deleteMethod.addJavaDocLine(" */");
            //修饰符
            deleteMethod.setVisibility(JavaVisibility.DEFAULT);
            deleteMethod.setAbstract(true);
            serviceInterface.addMethod(deleteMethod);
        }

        if (templateMethod.contains("updateOne")) {
            //描述 更新
            Method updateMethod = new Method("updateOne");
            //参数
            updateMethod.addParameter(new Parameter(boFullyQualifiedJavaType, StringUtil.firstCharToLowCase(boName)));
            //返回值
            methodReturnType = new FullyQualifiedJavaType(RESULT_BO_PACKAGE);
            updateMethod.setReturnType(methodReturnType);
            //注解
            updateMethod.addJavaDocLine("/**");
            updateMethod.addJavaDocLine(" * 更新");
            updateMethod.addJavaDocLine(" *");
            updateMethod.addJavaDocLine(" * @param " + StringUtil.firstCharToLowCase(boName));
            updateMethod.addJavaDocLine(" * @return 是否更新成功");
            updateMethod.addJavaDocLine(" */");
            //修饰符
            updateMethod.setVisibility(JavaVisibility.DEFAULT);
            updateMethod.setAbstract(true);
            serviceInterface.addMethod(updateMethod);
        }

        if (templateMethod.contains("queryById")) {
            //描述 查询
            Method selectMethod = new Method("queryById");
            //参数
            selectMethod.addParameter(new Parameter(new FullyQualifiedJavaType("java.lang.Long"), "id"));
            //返回值
            methodReturnType = new FullyQualifiedJavaType(RESULT_BO_PACKAGE);
            methodReturnType.addTypeArgument(boFullyQualifiedJavaType);
            selectMethod.setReturnType(methodReturnType);
            //注解
            selectMethod.addJavaDocLine("/**");
            selectMethod.addJavaDocLine(" * 查询");
            selectMethod.addJavaDocLine(" *");
            selectMethod.addJavaDocLine(" * @param id");
            selectMethod.addJavaDocLine(" * @return 查询对象");
            selectMethod.addJavaDocLine(" */");
            //修饰符
            selectMethod.setVisibility(JavaVisibility.DEFAULT);
            selectMethod.setAbstract(true);
            serviceInterface.addMethod(selectMethod);
        }

        if (templateMethod.contains("list")) {
            //描述 查询列表
            Method listMethod = new Method("list");
            //参数
            FullyQualifiedJavaType pagingFullyQualifiedJavaType = new FullyQualifiedJavaType(PAGING_QUERY_BO_PACKAGE);
            serviceInterface.addImportedType(pagingFullyQualifiedJavaType);
            listMethod.addParameter(0, new Parameter(pagingFullyQualifiedJavaType, "pagingQueryBO"));

            String boQueryName = String.format("%sQuery%s", objectName, boSuffix);
            String boQueryJavaType = String.format("%s.%s", boPackage, boQueryName);
            FullyQualifiedJavaType boQueryFullyQualifiedJavaType = new FullyQualifiedJavaType(boQueryJavaType);
            serviceInterface.addImportedType(boQueryFullyQualifiedJavaType);
            listMethod.addParameter(1, new Parameter(boQueryFullyQualifiedJavaType, StringUtil.firstCharToLowCase(boQueryName)));

            //返回值
            pagingFullyQualifiedJavaType = new FullyQualifiedJavaType(PAGING_QUERY_BO_PACKAGE);
            pagingFullyQualifiedJavaType.addTypeArgument(boFullyQualifiedJavaType);
            methodReturnType = new FullyQualifiedJavaType(RESULT_BO_PACKAGE);
            methodReturnType.addTypeArgument(pagingFullyQualifiedJavaType);
            listMethod.setReturnType(methodReturnType);
            //注解
            listMethod.addJavaDocLine("/**");
            listMethod.addJavaDocLine(" * 分页查询列表");
            listMethod.addJavaDocLine(" *");
            listMethod.addJavaDocLine(" * @param pagingQueryBO");
            listMethod.addJavaDocLine(String.format(" * @param %s", StringUtil.firstCharToLowCase(boQueryName)));
            listMethod.addJavaDocLine(" * @return 查询列表");
            listMethod.addJavaDocLine(" */");
            //修饰符
            listMethod.setVisibility(JavaVisibility.DEFAULT);
            listMethod.setAbstract(true);
            serviceInterface.addMethod(listMethod);
        }

        return new GeneratedJavaFile(serviceInterface, targetProject, context.getJavaFormatter());
    }

    /**
     * 新增时必填字段生成校验
     *
     * @param method
     * @param columns
     * @param boField
     * @param clazz
     */
    private void generateAddRequiredFieldCheck(Method method, List<IntrospectedColumn> columns, String boField, TopLevelClass clazz) {
        StringBuilder setValue = new StringBuilder("\n");
        for (IntrospectedColumn column : columns) {
            String columnName = column.getJavaProperty();
            if ("id".equals(columnName)) {
                continue;
            }
            if ("status".equals(columnName)) {
                clazz.addImportedType(STATUS_ENUMS_PACKAGE);
                setValue.append(String.format("        %s.setStatus(StatusEnum.VALID.getStatus());\n", boField));
                continue;
            }
            if ("createBy".equals(columnName)) {
                clazz.addImportedType(LOGIN_USER_PACKAGE);
                setValue.append(String.format("        %s.setCreateBy(LoginUser.get().getId());\n", boField));
                continue;
            }
            if ("lastModifyBy".equals(columnName)) {
                clazz.addImportedType(LOGIN_USER_PACKAGE);
                setValue.append(String.format("        %s.setLastModifyBy(LoginUser.get().getId());\n", boField));
                continue;
            }
            if ("createTime".equals(columnName)) {
                clazz.addImportedType(DATE_UTIL_PACKAGE);
                setValue.append(String.format("        %s.setCreateTime(DateUtil.getCurrentDate());\n", boField));
                continue;
            }
            if ("lastModifyTime".equals(columnName)) {
                clazz.addImportedType(DATE_UTIL_PACKAGE);
                setValue.append(String.format("        %s.setLastModifyTime(DateUtil.getCurrentDate());\n", boField));
                continue;
            }
            // 必填字段
            if (!column.isNullable()) {
                if ("String".equals(column.getFullyQualifiedJavaType().getShortName())) {
                    clazz.addImportedType(STRING_UTIL_PACKAGE);
                    method.addBodyLine(String.format("if (!StringUtil.hasText(%s.%s())) {",
                            boField,
                            JavaBeansUtil.getGetterMethodName(columnName, column.getFullyQualifiedJavaType())));
                    method.addBodyLine(String.format("return ResultBO.fail(\"%s不能为空\");", column.getRemarks()));
                    method.addBodyLine("}");
                } else if ("Integer".equals(column.getFullyQualifiedJavaType().getShortName())
                        || "Byte".equals(column.getFullyQualifiedJavaType().getShortName())
                        || "Long".equals(column.getFullyQualifiedJavaType().getShortName())
                        || "Short".equals(column.getFullyQualifiedJavaType().getShortName())
                        || "Date".equals(column.getFullyQualifiedJavaType().getShortName())) {
                    method.addBodyLine(String.format("if (null == %s.%s()) {",
                            boField,
                            JavaBeansUtil.getGetterMethodName(columnName, column.getFullyQualifiedJavaType())));
                    method.addBodyLine(String.format("return ResultBO.fail(\"%s不能为空\");", column.getRemarks()));
                    method.addBodyLine("}");
                }

            }
        }
        method.addBodyLine(setValue.toString());
    }

    /**
     * 更新时必填字段生成校验
     *
     * @param method
     * @param columns
     * @param boField
     * @param clazz
     */
    private void generateUpdateRequiredFieldCheck(Method method, List<IntrospectedColumn> columns, String boField, TopLevelClass clazz) {
        StringBuilder setValue = new StringBuilder("\n");
        for (IntrospectedColumn column : columns) {
            String columnName = column.getJavaProperty();
            if ("id".equals(columnName)) {
                method.addBodyLine(String.format("if (null == %s.getId()) {", boField));
                method.addBodyLine("return ResultBO.fail(\"主键不应为空\");");
                method.addBodyLine("}");
            } else if ("lastModifyBy".equals(columnName)) {
                clazz.addImportedType(LOGIN_USER_PACKAGE);
                setValue.append(String.format("        %s.setLastModifyBy(LoginUser.get().getId());\n", boField));
            } else if ("lastModifyTime".equals(columnName)) {
                clazz.addImportedType(DATE_UTIL_PACKAGE);
                setValue.append(String.format("        %s.setLastModifyTime(DateUtil.getCurrentDate());\n", boField));
            }
        }
        method.addBodyLine(setValue.toString());
    }

    /**
     * 生成serviceImpl实现类
     *
     * @param introspectedTable
     * @return
     */
    private GeneratedJavaFile generateServiceImpl(IntrospectedTable introspectedTable, List<Field> fields) {
        String serviceInterfaceJavaType = String.format("%s.I%sService", servicePackage, objectName);
        String serviceImplJavaType = String.format("%s.%sServiceImpl", serviceImplPackage, objectName);
        FullyQualifiedJavaType service = new FullyQualifiedJavaType(serviceInterfaceJavaType);
        FullyQualifiedJavaType serviceImpl = new FullyQualifiedJavaType(serviceImplJavaType);
        TopLevelClass clazz = new TopLevelClass(serviceImpl);
        addClassFileJavaDoc(clazz, author, copyright);
        //描述类的作用域修饰符
        clazz.setVisibility(JavaVisibility.PUBLIC);
        //描述类 引入的类
        clazz.addImportedType(service);
        //描述类 的实现接口类
        clazz.addSuperInterface(service);

        clazz.addImportedType("org.springframework.transaction.annotation.Transactional");
        // bo do导入
        String doJavaType = introspectedTable.getBaseRecordType();
        String doName = doJavaType.substring(doJavaType.lastIndexOf(".") + 1);
        String boName = String.format("%sBO", objectName, boSuffix);
        String boJavaType = String.format("%s.%s", boPackage, boName);

        FullyQualifiedJavaType doFullyQualifiedJavaType = new FullyQualifiedJavaType(doJavaType);
        clazz.addImportedType(doFullyQualifiedJavaType);
        FullyQualifiedJavaType boFullyQualifiedJavaType = new FullyQualifiedJavaType(boJavaType);
        clazz.addImportedType(boFullyQualifiedJavaType);

        if (stringHasValue(superServiceImpl)) {
            String superServiceImplName = superServiceImpl.substring(superServiceImpl.lastIndexOf(".") + 1);
            clazz.addImportedType(superServiceImpl);
//            clazz.addImportedType(boJavaType);
            clazz.setSuperClass(superServiceImplName + "<" + boName + ">");
        }
        clazz.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Service"));
        clazz.addAnnotation("@Service");

        // 日志文件导入
        clazz.addImportedType(new FullyQualifiedJavaType("org.slf4j.Logger"));
        clazz.addImportedType(new FullyQualifiedJavaType("org.slf4j.LoggerFactory"));
        Field loggerField = new Field("logger", new FullyQualifiedJavaType("org.slf4j.Logger"));
        // 描述成员属性修饰符
        loggerField.setStatic(true);
        loggerField.setFinal(true);
        loggerField.setVisibility(JavaVisibility.PRIVATE);
        loggerField.setInitializationString(String.format("LoggerFactory.getLogger(%s.class)", serviceImpl.getShortName()));
        clazz.addField(loggerField);

        // DAO注入
        String daoFieldType = introspectedTable.getMyBatis3JavaMapperType();
        String daoFieldName = StringUtil.firstCharToLowCase(daoFieldType.substring(daoFieldType.lastIndexOf(".") + 2));
        //描述类的成员属性
        Field daoField = new Field(daoFieldName, new FullyQualifiedJavaType(daoFieldType));
        clazz.addImportedType(new FullyQualifiedJavaType(daoFieldType));
        clazz.addImportedType(new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired"));
        //描述成员属性 的注解
        daoField.addAnnotation("@Autowired");
        //描述成员属性修饰符
        daoField.setVisibility(JavaVisibility.PRIVATE);
        clazz.addField(daoField);

        FullyQualifiedJavaType resultBOFullyQualifiedJavaType = new FullyQualifiedJavaType(RESULT_BO_PACKAGE);
        clazz.addImportedType(resultBOFullyQualifiedJavaType);

        String templateMethod = introspectedTable.getTableConfiguration().getProperty("templateMethod");
        FullyQualifiedJavaType methodReturnType = null;

        if (templateMethod.contains("addOne")) {
            //描述 新增
            Method addMethod = new Method("addOne");
            //方法注解
            addMethod.addAnnotation("@Transactional(rollbackFor = Exception.class)");
            addMethod.addAnnotation("@Override");
            //参数
            addMethod.addParameter(new Parameter(boFullyQualifiedJavaType, StringUtil.firstCharToLowCase(boName)));
            //返回值
            methodReturnType = new FullyQualifiedJavaType(RESULT_BO_PACKAGE);
            methodReturnType.addTypeArgument(boFullyQualifiedJavaType);
            addMethod.setReturnType(methodReturnType);
            //方法体，逻辑代码
            addMethod.addBodyLine("// 1.校验对象");
            generateAddRequiredFieldCheck(addMethod, introspectedTable.getBaseColumns(),
                    StringUtil.firstCharToLowCase(boName), clazz);
            addMethod.addBodyLine("// 2.bo 转 do");
            addMethod.addBodyLine(String.format("%s %s = %s.to%s();", doName, StringUtil.firstCharToLowCase(doName), StringUtil.firstCharToLowCase(boName), doName));
            addMethod.addBodyLine("// 3.持久化对象");
            addMethod.addBodyLine(String.format("%s.insert(%s);", daoFieldName, StringUtil.firstCharToLowCase(doName)));
            addMethod.addBodyLine(String.format("Long id = %s.getId();", StringUtil.firstCharToLowCase(doName)));
            addMethod.addBodyLine("if (null != id) {");
            addMethod.addBodyLine(String.format("return ResultBO.success(new %s(%s));", boName, StringUtil.firstCharToLowCase(doName)));
            addMethod.addBodyLine("}");
            addMethod.addBodyLine(String.format("return ResultBO.fail(\"新增%s失败\");", introspectedTable.getRemarks()));
            //注解
            addMethod.addJavaDocLine("/**");
            addMethod.addJavaDocLine(" * 新增");
            addMethod.addJavaDocLine(" *");
            addMethod.addJavaDocLine(" * @param " + StringUtil.firstCharToLowCase(boName));
            addMethod.addJavaDocLine(" * @return 是否新增成功");
            addMethod.addJavaDocLine(" */");
            //修饰符
            addMethod.setVisibility(JavaVisibility.PUBLIC);
            clazz.addMethod(addMethod);
        }

        if (templateMethod.contains("deleteById")) {
            //描述 删除
            Method deleteMethod = new Method("deleteById");
            //方法注解
            deleteMethod.addAnnotation("@Transactional(rollbackFor = Exception.class)");
            deleteMethod.addAnnotation("@Override");
            //参数
            deleteMethod.addParameter(new Parameter(new FullyQualifiedJavaType("java.lang.Long"), "id"));
            //返回值
            methodReturnType = new FullyQualifiedJavaType(RESULT_BO_PACKAGE);
            deleteMethod.setReturnType(methodReturnType);
            //方法体，逻辑代码
            deleteMethod.addBodyLine(String.format("if (1 == %s.deleteByPrimaryKey(id)) {", daoFieldName));
            deleteMethod.addBodyLine("return ResultBO.success();");
            deleteMethod.addBodyLine("}");
            deleteMethod.addBodyLine("return ResultBO.fail(String.format(\"请确认记录(id=%d)是否存在\", id));");
            //注解
            deleteMethod.addJavaDocLine("/**");
            deleteMethod.addJavaDocLine(" * 删除");
            deleteMethod.addJavaDocLine(" *");
            deleteMethod.addJavaDocLine(" * @param id");
            deleteMethod.addJavaDocLine(" * @return 是否删除成功");
            deleteMethod.addJavaDocLine(" */");
            //修饰符
            deleteMethod.setVisibility(JavaVisibility.PUBLIC);
            clazz.addMethod(deleteMethod);
        }

        if (templateMethod.contains("updateOne")) {
            //描述 更新
            Method updateMethod = new Method("updateOne");
            //方法注解
            updateMethod.addAnnotation("@Transactional(rollbackFor = Exception.class)");
            updateMethod.addAnnotation("@Override");
            //参数
            updateMethod.addParameter(new Parameter(boFullyQualifiedJavaType, StringUtil.firstCharToLowCase(boName)));
            //返回值
            methodReturnType = new FullyQualifiedJavaType(RESULT_BO_PACKAGE);
            updateMethod.setReturnType(methodReturnType);
            //方法体，逻辑代码
            updateMethod.addBodyLine("// 1.校验对象");
            generateUpdateRequiredFieldCheck(updateMethod, introspectedTable.getNonBLOBColumns(),
                    StringUtil.firstCharToLowCase(boName), clazz);
            updateMethod.addBodyLine("// 2.bo 转 do");
            updateMethod.addBodyLine(String.format("%s %s = %s.to%s();", doName, StringUtil.firstCharToLowCase(doName), StringUtil.firstCharToLowCase(boName), doName));
            updateMethod.addBodyLine("// 3.持久化对象");
            updateMethod.addBodyLine(String.format("int count = %s.updateByPrimaryKeySelective(%s);", daoFieldName, StringUtil.firstCharToLowCase(doName)));
            updateMethod.addBodyLine("if (1 == count) {");
            updateMethod.addBodyLine("return ResultBO.success();");
            updateMethod.addBodyLine("}");
            updateMethod.addBodyLine("return ResultBO.fail(String.format(\"请确认记录(id=%d)是否存在\", " +
                    String.format("%s.getId()));", StringUtil.firstCharToLowCase(boName)));
            //注解
            updateMethod.addJavaDocLine("/**");
            updateMethod.addJavaDocLine(" * 更新");
            updateMethod.addJavaDocLine(" *");
            updateMethod.addJavaDocLine(" * @param " + StringUtil.firstCharToLowCase(boName));
            updateMethod.addJavaDocLine(" * @return 是否更新成功");
            updateMethod.addJavaDocLine(" */");
            //修饰符
            updateMethod.setVisibility(JavaVisibility.PUBLIC);
            clazz.addMethod(updateMethod);
        }

        if (templateMethod.contains("queryById")) {
            //描述 查询
            Method selectMethod = new Method("queryById");
            //方法注解
            selectMethod.addAnnotation("@Override");
            //参数
            selectMethod.addParameter(new Parameter(new FullyQualifiedJavaType("java.lang.Long"), "id"));
            //返回值
            methodReturnType = new FullyQualifiedJavaType(RESULT_BO_PACKAGE);
            methodReturnType.addTypeArgument(boFullyQualifiedJavaType);
            selectMethod.setReturnType(methodReturnType);
            //方法体，逻辑代码
            selectMethod.addBodyLine(String.format("%s %s = %s.selectByPrimaryKey(id);", doName, StringUtil.firstCharToLowCase(doName), daoFieldName));
            selectMethod.addBodyLine(String.format("if (null != %s) {", StringUtil.firstCharToLowCase(doName)));
            selectMethod.addBodyLine(String.format("return ResultBO.success(new %s(%s));", boName, StringUtil.firstCharToLowCase(doName)));
            selectMethod.addBodyLine("}");
            selectMethod.addBodyLine("return ResultBO.fail(String.format(\"请确认记录(id=%d)是否存在\", id));");
            //注解
            selectMethod.addJavaDocLine("/**");
            selectMethod.addJavaDocLine(" * 查询");
            selectMethod.addJavaDocLine(" *");
            selectMethod.addJavaDocLine(" * @param id");
            selectMethod.addJavaDocLine(" * @return 查询对象");
            selectMethod.addJavaDocLine(" */");
            //修饰符
            selectMethod.setVisibility(JavaVisibility.PUBLIC);
            clazz.addMethod(selectMethod);
        }

        if (templateMethod.contains("list")) {
            //描述 查询列表
            Method listMethod = new Method("list");
            //方法注解
            listMethod.addAnnotation("@Override");
            //参数
            FullyQualifiedJavaType pagingFullyQualifiedJavaType = new FullyQualifiedJavaType(PAGING_QUERY_BO_PACKAGE);
            clazz.addImportedType(pagingFullyQualifiedJavaType);
            listMethod.addParameter(0, new Parameter(pagingFullyQualifiedJavaType, "pagingQueryBO"));

            String boQueryName = String.format("%sQuery%s", objectName, boSuffix);
            String boQueryJavaType = String.format("%s.%s", boPackage, boQueryName);
            FullyQualifiedJavaType boQueryFullyQualifiedJavaType = new FullyQualifiedJavaType(boQueryJavaType);
            clazz.addImportedType(boQueryFullyQualifiedJavaType);
            listMethod.addParameter(1, new Parameter(boQueryFullyQualifiedJavaType, StringUtil.firstCharToLowCase(boQueryName)));

            //返回值
            pagingFullyQualifiedJavaType = new FullyQualifiedJavaType(PAGING_QUERY_BO_PACKAGE);
            pagingFullyQualifiedJavaType.addTypeArgument(boFullyQualifiedJavaType);
            methodReturnType = new FullyQualifiedJavaType(RESULT_BO_PACKAGE);
            methodReturnType.addTypeArgument(pagingFullyQualifiedJavaType);
            listMethod.setReturnType(methodReturnType);

            //方法体，逻辑代码
            String doExampleName = String.format("%sExample", objectName);
            String doExampleJavaType = String.format("%s.%s", doPackage, doExampleName);
            FullyQualifiedJavaType doExampleFullyQualifiedJavaType = new FullyQualifiedJavaType(doExampleJavaType);
            clazz.addImportedType(doExampleFullyQualifiedJavaType);
            clazz.addImportedType("java.util.List");
            clazz.addImportedType("java.util.stream.Collectors");
            listMethod.addBodyLine(String.format("%s %s = new %s();", doExampleName, StringUtil.firstCharToLowCase(doExampleName), doExampleName));
            listMethod.addBodyLine(String.format("%s.Criteria criteria = %s.createCriteria();", doExampleName, StringUtil.firstCharToLowCase(doExampleName)));

            listMethod.addBodyLine(String.format("if (null != %s.getStatus()) {", StringUtil.firstCharToLowCase(boQueryName)));
            listMethod.addBodyLine(String.format("criteria.andStatusEqualTo(%s.getStatus());", StringUtil.firstCharToLowCase(boQueryName)));
            listMethod.addBodyLine("}");

            if (null != fields) {
                for (Field field : fields) {
                    String javaType = field.getType().getShortName();
                    String fieldName = field.getName();
                    if ("String".equals(javaType)) {
                        clazz.addImportedType("STRING_UTIL_PACKAGE");
                        listMethod.addBodyLine(String.format("if (StringUtil.hasText(%s.%s())) {",
                                StringUtil.firstCharToLowCase(boQueryName),
                                JavaBeansUtil.getGetterMethodName(fieldName, field.getType())));
                        listMethod.addBodyLine(String.format("criteria.and%sLike(%s.%s());",
                                StringUtil.firstCharToUpperCase(fieldName),
                                StringUtil.firstCharToLowCase(boQueryName),
                                JavaBeansUtil.getGetterMethodName(fieldName, field.getType())
                        ));
                        listMethod.addBodyLine("}");
                    } else if ("Date".equals(javaType)) {
                        // 介于
                        listMethod.addBodyLine(String.format("if (null != %s.%s()) {",
                                StringUtil.firstCharToLowCase(boQueryName),
                                JavaBeansUtil.getGetterMethodName(fieldName, field.getType())));
                        listMethod.addBodyLine(String.format("criteria.and%sEqualTo(%s.%s());",
                                StringUtil.firstCharToUpperCase(fieldName),
                                StringUtil.firstCharToLowCase(boQueryName),
                                JavaBeansUtil.getGetterMethodName(fieldName, field.getType())
                        ));
                        listMethod.addBodyLine("}");
                    } else if ("Long".equals(javaType) || "Integer".equals(javaType)
                            || "Short".equals(javaType) || "Byte".equals(javaType)) {
                        listMethod.addBodyLine(String.format("if (null != %s.%s()) {",
                                StringUtil.firstCharToLowCase(boQueryName),
                                JavaBeansUtil.getGetterMethodName(fieldName, field.getType())));
                        listMethod.addBodyLine(String.format("criteria.and%sEqualTo(%s.%s());",
                                StringUtil.firstCharToUpperCase(fieldName),
                                StringUtil.firstCharToLowCase(boQueryName),
                                JavaBeansUtil.getGetterMethodName(fieldName, field.getType())
                        ));
                        listMethod.addBodyLine("}");
                    }
                }
            }

            listMethod.addBodyLine("");
            listMethod.addBodyLine(String.format("long dataCount = %s.countByExample(%s);", daoFieldName, StringUtil.firstCharToLowCase(doExampleName)));
            listMethod.addBodyLine("pagingQueryBO.initTotal(dataCount);");
            listMethod.addBodyLine("if (0 == dataCount) {");
            listMethod.addBodyLine("return ResultBO.fail(\"没有查询到数据\");");
            listMethod.addBodyLine("}");
            listMethod.addBodyLine("if (pagingQueryBO.getPages() < pagingQueryBO.getPageNum()) {");
            listMethod.addBodyLine("return ResultBO.fail(\"查询页码大于总页数\");");
            listMethod.addBodyLine("}");
            listMethod.addBodyLine("");
            listMethod.addBodyLine("// 查询数据列表");
            listMethod.addBodyLine(String.format("%s.setFrom(pagingQueryBO.getFrom());", StringUtil.firstCharToLowCase(doExampleName)));
            listMethod.addBodyLine(String.format("%s.setSize(pagingQueryBO.getPageSize());", StringUtil.firstCharToLowCase(doExampleName)));
            listMethod.addBodyLine(String.format("if (StringUtil.hasText(%s.getOrderBy())) {", StringUtil.firstCharToLowCase(boQueryName)));
            listMethod.addBodyLine(String.format("%s.setOrderByClause(%s.getOrderBy());",
                    StringUtil.firstCharToLowCase(doExampleName),
                    StringUtil.firstCharToLowCase(boQueryName)
            ));
            listMethod.addBodyLine("}");
            listMethod.addBodyLine("");
            listMethod.addBodyLine(String.format("List<%s> %sList = %s.selectByExample(%s);",
                    doName, StringUtil.firstCharToLowCase(doName), daoFieldName, StringUtil.firstCharToLowCase(doExampleName)));
            listMethod.addBodyLine(String.format("pagingQueryBO.setData(%sList.stream().map(%s::new).collect(Collectors.toList()));",
                    StringUtil.firstCharToLowCase(doName), boName));
            listMethod.addBodyLine("");
            listMethod.addBodyLine("return ResultBO.success(pagingQueryBO);");

            //注解
            listMethod.addJavaDocLine("/**");
            listMethod.addJavaDocLine(" * 分页查询列表");
            listMethod.addJavaDocLine(" *");
            listMethod.addJavaDocLine(" * @param pagingQueryBO");
            listMethod.addJavaDocLine(String.format(" * @param %s", StringUtil.firstCharToLowCase(boQueryName)));
            listMethod.addJavaDocLine(" * @return 查询列表");
            listMethod.addJavaDocLine(" */");
            //修饰符
            listMethod.setVisibility(JavaVisibility.PUBLIC);
            clazz.addMethod(listMethod);
        }

        return new GeneratedJavaFile(clazz, targetProject, context.getJavaFormatter());
    }

    /**
     * 生成controller类
     *
     * @param introspectedTable
     * @return
     */
    private GeneratedJavaFile generateController(IntrospectedTable introspectedTable) {
        String serviceInterfaceJavaType = String.format("%s.I%sService", servicePackage, objectName);
        String controllerJavaType = String.format("%s.%sController", controllerPackage, objectName);

        String reqDTOName = String.format("%s%s", objectName, reqDTOSuffix);
        String respDTOName = String.format("%s%s", objectName, respDTOSuffix);
        String reqDTOJavaType = String.format("%s.%s", reqDTOPackage, reqDTOName);
        FullyQualifiedJavaType reqDTOFullyQualifiedJavaType = new FullyQualifiedJavaType(reqDTOJavaType);
        String respDTOJavaType = String.format("%s.%s", respDTOPackage, respDTOName);
        FullyQualifiedJavaType respDTOFullyQualifiedJavaType = new FullyQualifiedJavaType(respDTOJavaType);

        FullyQualifiedJavaType resultBOFullyQualifiedJavaType = new FullyQualifiedJavaType(RESULT_BO_PACKAGE);

        String boName = String.format("%s%s", objectName, boSuffix);
        String boJavaType = String.format("%s.%s", boPackage, boName);
        FullyQualifiedJavaType boFullyQualifiedJavaType = new FullyQualifiedJavaType(boJavaType);

        FullyQualifiedJavaType controller = new FullyQualifiedJavaType(controllerJavaType);
        TopLevelClass clazz = new TopLevelClass(controller);
        addClassFileJavaDoc(clazz, author, copyright);
        //描述类的作用域修饰符
        clazz.setVisibility(JavaVisibility.PUBLIC);

        //添加@Controller注解，并引入相应的类
        clazz.addImportedType("org.springframework.web.bind.annotation.RestController");
        clazz.addAnnotation("@RestController");
        //添加@RequestMapping注解，并引入相应的类
        clazz.addImportedType("org.springframework.web.bind.annotation.RequestMapping");
        clazz.addAnnotation("@RequestMapping(\"/" + StringUtil.firstCharToLowCase(objectName) + "\")");
        //添加@Api注解，并引入相应的类
        clazz.addImportedType("io.swagger.annotations.Api");
        String remarks = introspectedTable.getRemarks();
        if (!StringUtil.hasText(remarks)) {
            remarks = StringUtil.firstCharToLowCase(objectName);
        }
        clazz.addAnnotation("@Api(\"" + remarks + "\")");

        clazz.addImportedType(reqDTOFullyQualifiedJavaType);
        clazz.addImportedType(respDTOFullyQualifiedJavaType);
        clazz.addImportedType(resultBOFullyQualifiedJavaType);
        clazz.addImportedType(boFullyQualifiedJavaType);
        FullyQualifiedJavaType resultDTOFullyQualifiedJavaType = new FullyQualifiedJavaType(RESULT_RESP_DTO_PACKAGE);
        clazz.addImportedType(resultDTOFullyQualifiedJavaType);
        FullyQualifiedJavaType idReqDTOFullyQualifiedJavaType = new FullyQualifiedJavaType(ID_REQ_DTO_PACKAGE);
        clazz.addImportedType(idReqDTOFullyQualifiedJavaType);

        //引入controller的父类和dto，并添加泛型
        if (stringHasValue(superController)) {
            clazz.addImportedType(superController);
            FullyQualifiedJavaType superInterface = new FullyQualifiedJavaType(superController + "<" + reqDTOName + ">");
            clazz.addSuperInterface(superInterface);
        }

        //引入Service
        FullyQualifiedJavaType serviceInterface = new FullyQualifiedJavaType(serviceInterfaceJavaType);
        clazz.addImportedType(serviceInterface);

        //添加Service成员变量
        String serviceFieldName = StringUtil.firstCharToLowCase(String.format("%sService", StringUtil.firstCharToLowCase(objectName)));
        Field serviceField = new Field(serviceFieldName, serviceInterface);
        clazz.addImportedType("org.springframework.beans.factory.annotation.Autowired");
        //描述成员属性 的注解
        serviceField.addAnnotation("@Autowired");
        //描述成员属性修饰符
        serviceField.setVisibility(JavaVisibility.PRIVATE);
        clazz.addField(serviceField);

        // 日志文件导入
        clazz.addImportedType(new FullyQualifiedJavaType("org.slf4j.Logger"));
        clazz.addImportedType(new FullyQualifiedJavaType("org.slf4j.LoggerFactory"));
        Field loggerField = new Field("logger", new FullyQualifiedJavaType("org.slf4j.Logger"));
        // 描述成员属性修饰符
        loggerField.setStatic(true);
        loggerField.setFinal(true);
        loggerField.setVisibility(JavaVisibility.PRIVATE);
        loggerField.setInitializationString(String.format("LoggerFactory.getLogger(%s.class)", controller.getShortName()));
        clazz.addField(loggerField);

        clazz.addImportedType("org.springframework.web.bind.annotation.PostMapping");
        clazz.addImportedType("io.swagger.annotations.ApiOperation");
        clazz.addImportedType("org.springframework.web.bind.annotation.RequestBody");

        String templateMethod = introspectedTable.getTableConfiguration().getProperty("templateMethod");
        FullyQualifiedJavaType methodReturnType = null;
        Parameter parameter = null;

        if (templateMethod.contains("addOne")) {
            //描述 新增
            Method addMethod = new Method("addOne");
            //方法注解
            addMethod.addAnnotation("@PostMapping(\"/add/one\")");
            addMethod.addAnnotation("@ApiOperation(value = \"新增\")");
            //参数
            parameter = new Parameter(reqDTOFullyQualifiedJavaType, "req");
            parameter.addAnnotation("@RequestBody");
            addMethod.addParameter(parameter);
            //返回值
            methodReturnType = new FullyQualifiedJavaType(RESULT_RESP_DTO_PACKAGE);
            methodReturnType.addTypeArgument(respDTOFullyQualifiedJavaType);
            addMethod.setReturnType(methodReturnType);
            //方法体，逻辑代码
            addMethod.addBodyLine(String.format("%s %s = req.to%s();",
                    boName, StringUtil.firstCharToLowCase(boName), boName));
            addMethod.addBodyLine(String.format("ResultBO<%s> resultBO = %s.addOne(%s);", boName, serviceFieldName, StringUtil.firstCharToLowCase(boName)));
            addMethod.addBodyLine("if (resultBO.getResult()) {");
            addMethod.addBodyLine(String.format("return ResultRespDTO.success(new %s(resultBO.getData()));",
                    respDTOName));
            addMethod.addBodyLine("}");
            addMethod.addBodyLine("return ResultRespDTO.error(resultBO);");
            //修饰符
            addMethod.setVisibility(JavaVisibility.PUBLIC);
            clazz.addMethod(addMethod);
        }

        if (templateMethod.contains("deleteById")) {
            //描述 删除
            Method deleteMethod = new Method("deleteById");
            //方法注解
            deleteMethod.addAnnotation("@PostMapping(\"/delete/id\")");
            deleteMethod.addAnnotation("@ApiOperation(value = \"删除\")");
            //参数
            parameter = new Parameter(idReqDTOFullyQualifiedJavaType, "req");
            parameter.addAnnotation("@RequestBody");
            deleteMethod.addParameter(parameter);
            //返回值
            methodReturnType = new FullyQualifiedJavaType(RESULT_RESP_DTO_PACKAGE);
            deleteMethod.setReturnType(methodReturnType);
            //方法体，逻辑代码
            deleteMethod.addBodyLine(String.format("return ResultRespDTO.result(%s.deleteById(req.getId()));",
                    serviceFieldName));
            //修饰符
            deleteMethod.setVisibility(JavaVisibility.PUBLIC);
            clazz.addMethod(deleteMethod);
        }

        if (templateMethod.contains("updateOne")) {
            //描述 更新
            Method updateMethod = new Method("updateOne");
            //方法注解
            updateMethod.addAnnotation("@PostMapping(\"/update/one\")");
            updateMethod.addAnnotation("@ApiOperation(value = \"更新\")");
            //参数
            parameter = new Parameter(reqDTOFullyQualifiedJavaType, "req");
            parameter.addAnnotation("@RequestBody");
            updateMethod.addParameter(parameter);
            //返回值
            methodReturnType = new FullyQualifiedJavaType(RESULT_RESP_DTO_PACKAGE);
            updateMethod.setReturnType(methodReturnType);
            //方法体，逻辑代码
            updateMethod.addBodyLine(String.format("%s %s = req.to%s();",
                    boName, StringUtil.firstCharToLowCase(boName), boName));
            updateMethod.addBodyLine(String.format("return ResultRespDTO.result(%s.updateOne(%s));", serviceFieldName, StringUtil.firstCharToLowCase(boName)));
            //修饰符
            updateMethod.setVisibility(JavaVisibility.PUBLIC);
            clazz.addMethod(updateMethod);
        }

        if (templateMethod.contains("queryById")) {
            //描述 查询
            Method selectMethod = new Method("queryById");
            //方法注解
            selectMethod.addAnnotation("@PostMapping(\"/query/id\")");
            selectMethod.addAnnotation("@ApiOperation(value = \"查询\")");
            //参数
            parameter = new Parameter(idReqDTOFullyQualifiedJavaType, "req");
            parameter.addAnnotation("@RequestBody");
            selectMethod.addParameter(parameter);
            //返回值
            methodReturnType = new FullyQualifiedJavaType(RESULT_RESP_DTO_PACKAGE);
            methodReturnType.addTypeArgument(respDTOFullyQualifiedJavaType);
            selectMethod.setReturnType(methodReturnType);
            //方法体，逻辑代码
            selectMethod.addBodyLine(String.format("ResultBO<%s> resultBO = %s.queryById(req.getId());",
                    boName, serviceFieldName));
            selectMethod.addBodyLine("if (resultBO.getResult()) {");
            selectMethod.addBodyLine(String.format("return ResultRespDTO.success(new %s(resultBO.getData()));", respDTOName));
            selectMethod.addBodyLine("}");
            selectMethod.addBodyLine("return ResultRespDTO.error(resultBO);");
            //修饰符
            selectMethod.setVisibility(JavaVisibility.PUBLIC);
            clazz.addMethod(selectMethod);
        }

        if (templateMethod.contains("list")) {
            //描述 查询列表
            Method listMethod = new Method("list");
            //方法注解
            listMethod.addAnnotation("@PostMapping(\"/list\")");
            listMethod.addAnnotation("@ApiOperation(value = \"分页查询\")");
            //参数
            FullyQualifiedJavaType pagingQueryReqDTOFullyQualifiedJavaType = new FullyQualifiedJavaType(PAGING_QUERY_REQ_DTO_PACKAGE);
            clazz.addImportedType(pagingQueryReqDTOFullyQualifiedJavaType);

            String queryReqDTOName = String.format("%sQuery%s", objectName, reqDTOSuffix);
            String queryReqDTOJavaType = String.format("%s.%s", reqDTOPackage, queryReqDTOName);
            FullyQualifiedJavaType queryReqDTOFullyQualifiedJavaType = new FullyQualifiedJavaType(queryReqDTOJavaType);
            clazz.addImportedType(queryReqDTOFullyQualifiedJavaType);

            pagingQueryReqDTOFullyQualifiedJavaType.addTypeArgument(queryReqDTOFullyQualifiedJavaType);

            parameter = new Parameter(pagingQueryReqDTOFullyQualifiedJavaType, "req");
            parameter.addAnnotation("@RequestBody");
            listMethod.addParameter(parameter);
            //返回值

            FullyQualifiedJavaType pagingQueryRespDTOFullyQualifiedJavaType = new FullyQualifiedJavaType(PAGING_QUERY_RESP_DTO_PACKAGE);
            clazz.addImportedType(pagingQueryRespDTOFullyQualifiedJavaType);
            pagingQueryRespDTOFullyQualifiedJavaType.addTypeArgument(respDTOFullyQualifiedJavaType);

            methodReturnType = new FullyQualifiedJavaType(RESULT_RESP_DTO_PACKAGE);
            methodReturnType.addTypeArgument(pagingQueryRespDTOFullyQualifiedJavaType);
            listMethod.setReturnType(methodReturnType);

            //方法体，逻辑代码
            clazz.addImportedType("java.util.stream.Collectors");

            FullyQualifiedJavaType pagingQueryBOFullyQualifiedJavaType = new FullyQualifiedJavaType(PAGING_QUERY_BO_PACKAGE);
            clazz.addImportedType(pagingQueryBOFullyQualifiedJavaType);

            listMethod.addBodyLine(String.format("ResultBO<PagingQueryBO<%s>> resultBO = %s.list(req.toPagingQueryBO(), req.getQuery().to%s());",
                    boName, serviceFieldName, String.format("%sQuery%s", objectName, boSuffix)));
            listMethod.addBodyLine("if (resultBO.getResult()) {");
            listMethod.addBodyLine(String.format("return ResultRespDTO.success(new PagingQueryRespDTO(resultBO.getData(),resultBO.getData().getData().stream().map(%s::new).collect(Collectors.toList())));", respDTOName));
            listMethod.addBodyLine("}");
            listMethod.addBodyLine("return ResultRespDTO.error(resultBO);");
            //修饰符
            listMethod.setVisibility(JavaVisibility.PUBLIC);
            clazz.addMethod(listMethod);
        }

        return new GeneratedJavaFile(clazz, appTargetProject, context.getJavaFormatter());
    }

    /**
     * selectByExample 增加分页查询
     *
     * @param element
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        XmlElement isNotNullElement = new XmlElement("if");
        isNotNullElement.addAttribute(new Attribute("test", "from != null and from >= 0 and size != null and size >= 1"));
        isNotNullElement.addElement(new TextElement("limit ${from}, ${size}"));
        element.addElement(isNotNullElement);
        return super.sqlMapSelectByExampleWithoutBLOBsElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.addClassField(topLevelClass, introspectedTable, "from");
        this.addClassField(topLevelClass, introspectedTable, "size");
        return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
    }

    /**
     * 增加分页查询字段 from size
     * @param topLevelClass
     * @param introspectedTable
     * @param name
     */
    private void addClassField(TopLevelClass topLevelClass, IntrospectedTable introspectedTable, String name) {
        CommentGenerator commentGenerator = context.getCommentGenerator();

        Field field = new Field(name, FullyQualifiedJavaType.getIntInstance());
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setInitializationString("-1");
        commentGenerator.addFieldComment(field, introspectedTable);

        topLevelClass.addField(field);

        // set
        Method method = new Method(JavaBeansUtil.getSetterMethodName(name));
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), name));
        method.addBodyLine(String.format("this.%s = %s;", name, name));
        commentGenerator.addGeneralMethodComment(method, introspectedTable);

        topLevelClass.addMethod(method);

        // get
        method = new Method(JavaBeansUtil.getGetterMethodName(name, field.getType()));
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addBodyLine(String.format("return this.%s;", name));
        commentGenerator.addGeneralMethodComment(method, introspectedTable);

        topLevelClass.addMethod(method);
    }
}
