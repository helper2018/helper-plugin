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
package org.helper.demo.service.module.user_manage.bo;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.helper.demo.service.module.user_manage.dataobject.AppUserDO;

/**
 * <p>
 * 类名:AppUserBO
 * 2021/10/26 23:13
 * <p>
 *
 * @author zyw
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserBO {
    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 手机号
     */
    private Long mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * picture_url
     */
    private String pictureUrl;

    /**
     * 盐
     */
    private String salt;

    /**
     * token
     */
    private String token;

    /**
     * 状态:0-注销,1-正常,2-限制使用
     */
    private Byte status;

    /**
     * 注册时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date lastModifyTime;

    public AppUserDO toAppUserDO() {
        AppUserDO appUserDO = new AppUserDO();
        appUserDO.setId(this.id);
        appUserDO.setName(this.name);
        appUserDO.setMobile(this.mobile);
        appUserDO.setEmail(this.email);
        appUserDO.setPassword(this.password);
        appUserDO.setPictureUrl(this.pictureUrl);
        appUserDO.setSalt(this.salt);
        appUserDO.setToken(this.token);
        appUserDO.setStatus(this.status);
        appUserDO.setCreateTime(this.createTime);
        appUserDO.setLastModifyTime(this.lastModifyTime);
        return appUserDO;
    }

    public AppUserBO(AppUserDO appUserDO) {
        this.id = appUserDO.getId();
        this.name = appUserDO.getName();
        this.mobile = appUserDO.getMobile();
        this.email = appUserDO.getEmail();
        this.password = appUserDO.getPassword();
        this.pictureUrl = appUserDO.getPictureUrl();
        this.salt = appUserDO.getSalt();
        this.token = appUserDO.getToken();
        this.status = appUserDO.getStatus();
        this.createTime = appUserDO.getCreateTime();
        this.lastModifyTime = appUserDO.getLastModifyTime();
    }
}