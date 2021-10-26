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
package org.helper.demo.service.module.user_manage.service;

import org.helper.base.common.bo.PagingQueryBO;
import org.helper.base.common.bo.ResultBO;
import org.helper.demo.service.module.user_manage.bo.AppUserBO;
import org.helper.demo.service.module.user_manage.bo.AppUserQueryBO;

/**
 * <p>
 * 接口名:IAppUserService
 * 2021/10/26 23:13
 * <p>
 *
 * @author zyw
 * @version 1.0.0
 */
public interface IAppUserService {
    /**
     * 新增
     *
     * @param appUserBO
     * @return 是否新增成功
     */
    ResultBO<AppUserBO> addOne(AppUserBO appUserBO);

    /**
     * 删除
     *
     * @param id
     * @return 是否删除成功
     */
    ResultBO deleteById(Long id);

    /**
     * 更新
     *
     * @param appUserBO
     * @return 是否更新成功
     */
    ResultBO updateOne(AppUserBO appUserBO);

    /**
     * 查询
     *
     * @param id
     * @return 查询对象
     */
    ResultBO<AppUserBO> queryById(Long id);

    /**
     * 分页查询列表
     *
     * @param pagingQueryBO
     * @param appUserQueryBO
     * @return 查询列表
     */
    ResultBO<PagingQueryBO<AppUserBO>> list(PagingQueryBO pagingQueryBO, AppUserQueryBO appUserQueryBO);
}