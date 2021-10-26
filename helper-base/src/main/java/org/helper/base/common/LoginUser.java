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

package org.helper.base.common;

import lombok.Data;

/**
 * <p>
 * 类名:LoginUser.
 * 2021/10/13 17:07
 * 
 * <p>
 *
 * @author zyw
 * @version 1.0.0
 */
@Data
public class LoginUser {
    /**
     * 登录用户Id
     */
    private Long id;

    private static ThreadLocal<LoginUser> threadLocal = new ThreadLocal<>();

    public static void set(LoginUser user) {
        threadLocal.set(user);
    }

    public static LoginUser get() {
        return threadLocal.get();
    }
}
