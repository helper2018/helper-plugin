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

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 类名:PagingQueryRespDTO.
 * 2021/10/12 13:59
 * <p>
 *
 * @author zyw
 * @version 1.0.0
 */
@Data
public class PagingQueryBO<T> {
    /**
     * 总页数
     */
    private Integer pages;

    /**
     * 总条数
     */
    private Long total;

    /**
     * 每页数据条数
     */
    private Integer pageSize;

    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 返回数据
     */
    private List<T> data;

    /**
     * 查询开始索引
     *
     * @return
     */
    public int getFrom() {
        this.pageNum = Optional.ofNullable(this.pageNum).orElse(1);
        this.pageSize = Optional.ofNullable(this.pageSize).orElse(12);
        return (this.pageNum - 1) * this.pageSize;
    }

    public void initTotal(long total) {
        this.total = total;
        this.pages = (int) ((this.total + this.pageSize - 1) / this.pageSize);
    }
}
