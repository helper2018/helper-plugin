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

/**
 * <p>
 * 类名:IdForm.
 * 2021/10/12 16:41
 * <p>
 *
 * @author zyw
 * @version 1.0.0
 */
@Data
public class IdReqDTO {
    @ApiModelProperty(value = "主键", example = "1", dataType = "Long", required = true)
    private Long id;
}
