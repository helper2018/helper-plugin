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
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

import static org.helper.plugins.utils.JavaDocUtil.*;

/**
 * <p>
 * 类名:RemarksCommentPlugin.
 * 2021/10/18 10:18
 * <p>
 *
 * @author zyw
 * @version 1.0.0
 */
public class RemarksCommentPlugin extends PluginAdapter {

    /**
     * 版权信息
     */
    private String copyright;

    /**
     * 作者
     */
    private String author;

    @Override
    public boolean validate(List<String> warnings) {
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
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addClassFileJavaDoc(topLevelClass, author, copyright);
        return true;
    }

    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addClassFileJavaDoc(topLevelClass, author, copyright);
        return true;
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable
            introspectedTable) {
        addClassFileJavaDoc(topLevelClass, author, copyright);
        return true;
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addClassFileJavaDoc(topLevelClass, author, copyright);
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        addInterfaceFileJavaDoc(interfaze, author, copyright);
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn
            introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        addFieldJavaDoc(field, introspectedColumn);
        return true;
    }

}
