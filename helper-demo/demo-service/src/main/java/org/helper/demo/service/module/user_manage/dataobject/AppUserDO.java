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
package org.helper.demo.service.module.user_manage.dataobject;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 类名:AppUserDO
 * 2021/10/26 23:13
 * <p>
 *
 * @author zyw
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDO {
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
}