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
package org.helper.demo.app.module.user_manage.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.helper.base.common.req.BaseQueryReqDTO;
import org.helper.demo.service.module.user_manage.bo.AppUserQueryBO;

/**
 * <p>
 * 类名:AppUserQueryReqDTO
 * 2021/10/26 23:13
 * <p>
 *
 * @author zyw
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@ApiModel("AppUserQueryReqDTO")
public class AppUserQueryReqDTO extends BaseQueryReqDTO {
    @ApiModelProperty(value = "用户名称(maxLen=64)", example = "helper", dataType = "String", required = true)
    private String name;

    @ApiModelProperty(value = "手机号", example = "18511122233", dataType = "Long", required = true)
    private Long mobile;

    @ApiModelProperty(value = "邮箱(maxLen=128)", example = "helper", dataType = "String", required = false)
    private String email;

    @ApiModelProperty(value = "token(maxLen=128)", example = "helper", dataType = "String", required = false)
    private String token;

    public AppUserQueryBO toAppUserQueryBO() {
        AppUserQueryBO appUserQueryBO = new AppUserQueryBO();
        appUserQueryBO.setName(this.name);
        appUserQueryBO.setMobile(this.mobile);
        appUserQueryBO.setEmail(this.email);
        appUserQueryBO.setToken(this.token);
        return appUserQueryBO;
    }
}