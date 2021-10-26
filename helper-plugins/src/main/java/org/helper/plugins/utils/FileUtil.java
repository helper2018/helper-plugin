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

import java.io.File;

/**
 * <p>
 * 类名:FileUtil.
 * 2021/10/20 17:05
 * <p>
 *
 * @author zyw
 * @version 1.0.0
 */
public final class FileUtil {

    /**
     * 根据文件名查找文件
     *
     * @param dir
     * @param fileName
     * @return
     */
    public static File getFileByName(File dir, String fileName) {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                if (file.getName().equalsIgnoreCase(fileName)) {
                    return file;
                }
            } else if (file.isDirectory()) {
                if (file.getName().equalsIgnoreCase("target")
                        || file.getName().equalsIgnoreCase("test")) {
                    continue;
                }
                File f = getFileByName(file, fileName);
                if (null != f) {
                    return f;
                }
            }
        }
        return null;
    }

    /**
     * 查看文件夹是否存在
     *
     * @param path
     * @return
     */
    public static boolean createDir(String path) {
        if (!StringUtil.hasText(path)) {
            return false;
        }
        File f = new File(path);
        if (!f.exists() || !f.isDirectory()) {
            return f.mkdir();
        }
        return true;
    }
}
