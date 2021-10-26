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
package org.helper.base.common.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.helper.base.common.bo.PagingQueryBO;

import java.util.Optional;

/**
 * <p>
 * 类名:PagingQueryReqDTO.
 * 2021/10/12 13:59
 * <p>
 *
 * @author zyw
 * @version 1.0.0
 */
@Data
public class PagingQueryReqDTO<T> {
    @ApiModelProperty(value = "当前页码", example = "1", dataType = "Integer", required = true)
    private Integer pageNum;

    @ApiModelProperty(value = "每页显示条数", example = "20", dataType = "Integer", required = true)
    private Integer pageSize;

    @ApiModelProperty(value = "查询条件", example = "{\"name\":\"陈\"}", dataType = "{}", required = false)
    private T query;

    public PagingQueryBO toPagingQueryBO() {
        PagingQueryBO pagingQueryBO = new PagingQueryBO();
        pagingQueryBO.setPageNum(Optional.ofNullable(this.pageNum).orElse(1));
        pagingQueryBO.setPageSize(Optional.ofNullable(this.pageSize).orElse(12));
        return pagingQueryBO;
    }
}
