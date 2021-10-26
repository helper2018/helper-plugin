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
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.helper.demo.service.module.user_manage.bo.AppUserBO;

/**
 * <p>
 * 类名:AppUserReqDTO
 * 2021/10/26 23:13
 * <p>
 *
 * @author zyw
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@ApiModel("AppUserReqDTO")
public class AppUserReqDTO {
    @ApiModelProperty(value = "用户ID", example = "1", dataType = "Long", required = true)
    private Long id;

    @ApiModelProperty(value = "用户名称(maxLen=64)", example = "helper", dataType = "String", required = true)
    private String name;

    @ApiModelProperty(value = "手机号", example = "18511122233", dataType = "Long", required = true)
    private Long mobile;

    @ApiModelProperty(value = "邮箱(maxLen=128)", example = "helper", dataType = "String", required = false)
    private String email;

    @ApiModelProperty(value = "picture_url(maxLen=1024)", example = "helper", dataType = "String", required = false)
    private String pictureUrl;

    @ApiModelProperty(value = "token(maxLen=128)", example = "helper", dataType = "String", required = false)
    private String token;

    @ApiModelProperty(value = "状态:0-注销,1-正常,2-限制使用", example = "1", dataType = "Byte", required = true)
    private Byte status;

    @ApiModelProperty(value = "注册时间", example = "2021-10-26 23:13:09", dataType = "Date", required = true)
    private Date createTime;

    @ApiModelProperty(value = "修改时间", example = "2021-10-26 23:13:09", dataType = "Date", required = true)
    private Date lastModifyTime;

    public AppUserBO toAppUserBO() {
        AppUserBO appUserBO = new AppUserBO();
        appUserBO.setId(this.id);
        appUserBO.setName(this.name);
        appUserBO.setMobile(this.mobile);
        appUserBO.setEmail(this.email);
        appUserBO.setPictureUrl(this.pictureUrl);
        appUserBO.setToken(this.token);
        appUserBO.setStatus(this.status);
        appUserBO.setCreateTime(this.createTime);
        appUserBO.setLastModifyTime(this.lastModifyTime);
        return appUserBO;
    }
}