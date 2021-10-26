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
package org.helper.base.common.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.helper.base.common.bo.PagingQueryBO;

import java.util.List;

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
@ApiModel("PagingQueryRespDTO")
public class PagingQueryRespDTO<T> {
    @ApiModelProperty(value = "总页数", example = "10", dataType = "Integer")
    private Integer pages;

    @ApiModelProperty(value = "总条数", example = "10", dataType = "Long")
    private Long total;

    @ApiModelProperty(value = "每页数据条数", example = "10", dataType = "Integer")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页码", example = "10", dataType = "Integer")
    private Integer pageNum;

    @ApiModelProperty(value = "返回数据")
    private List<T> data;

    public PagingQueryRespDTO(PagingQueryBO pagingQueryBO, List<T> data) {
        this.pages = pagingQueryBO.getPages();
        this.total = pagingQueryBO.getTotal();
        this.pageSize = pagingQueryBO.getPageSize();
        this.pageNum = pagingQueryBO.getPageNum();
        this.data = data;
    }
}
