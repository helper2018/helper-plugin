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

package org.helper.base.common.bo;

import lombok.Data;

/**
 * <p>
 * 类名:ResultBO.
 * 2021/10/20 16:22
 * <p>
 *
 * @author zyw
 * @version 1.0.0
 */
@Data
public class ResultBO<T> {
    /**
     * 是否成功
     */
    private Boolean result;

    /**
     * 失败原因描述
     */
    private String failReason;

    /**
     * 返回数据
     */
    private T data;

    public static <M> ResultBO<M> success(M data) {
        ResultBO resultBO = new ResultBO();
        resultBO.setResult(true);
        resultBO.setData(data);
        return resultBO;
    }

    public static ResultBO success() {
        ResultBO resultBO = new ResultBO();
        resultBO.setResult(true);
        return resultBO;
    }

    public static ResultBO fail(String failReason) {
        ResultBO resultBO = new ResultBO();
        resultBO.setResult(false);
        resultBO.setFailReason(failReason);
        return resultBO;
    }
}
