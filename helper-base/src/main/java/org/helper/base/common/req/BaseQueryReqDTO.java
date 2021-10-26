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
import org.helper.base.common.bo.BaseQueryBO;
import org.helper.base.enums.StatusEnum;

import java.util.Optional;

/**
 * <p>
 * 类名:BaseQueryBO.
 * 2021/10/23 12:53
 * <p>
 *
 * @author zyw
 * @version 1.0.0
 */
@Data
public class BaseQueryReqDTO {
    @ApiModelProperty(value = "状态", example = "1", dataType = "Byte", required = false)
    private Byte status;

    @ApiModelProperty(value = "排序字段", example = "id desc", dataType = "String", required = false)
    private String orderBy;

    public BaseQueryBO toBaseQueryBO() {
        BaseQueryBO baseQueryBO = new BaseQueryBO();
        // 无效数据不允许插叙
        if (null != status && status >= StatusEnum.VALID.getStatus()) {
            baseQueryBO.setStatus(status);
        } else {
            baseQueryBO.setStatus(StatusEnum.VALID.getStatus());
        }
        // 默认id 倒序排列
        baseQueryBO.setOrderBy(Optional.ofNullable(this.orderBy).orElse("id desc"));
        return baseQueryBO;
    }

    /**
     * 没有其他查询字段时使用此方法
     * @param baseQueryBO
     */
    public void toBaseQueryBO(BaseQueryBO baseQueryBO) {
        // 无效数据不允许插叙
        if (null != status && status >= StatusEnum.VALID.getStatus()) {
            baseQueryBO.setStatus(status);
        } else {
            baseQueryBO.setStatus(StatusEnum.VALID.getStatus());
        }
        // 默认id 倒序排列
        baseQueryBO.setOrderBy(Optional.ofNullable(this.orderBy).orElse("id desc"));
    }
}
