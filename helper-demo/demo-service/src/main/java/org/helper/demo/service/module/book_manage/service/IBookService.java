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
package org.helper.demo.service.module.book_manage.service;

import org.helper.base.common.bo.PagingQueryBO;
import org.helper.base.common.bo.ResultBO;
import org.helper.demo.service.module.book_manage.bo.BookBO;
import org.helper.demo.service.module.book_manage.bo.BookQueryBO;

/**
 * <p>
 * 接口名:IBookService
 * 2021/10/26 23:13
 * <p>
 *
 * @author zyw
 * @version 1.0.0
 */
public interface IBookService {
    /**
     * 新增
     *
     * @param bookBO
     * @return 是否新增成功
     */
    ResultBO<BookBO> addOne(BookBO bookBO);

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
     * @param bookBO
     * @return 是否更新成功
     */
    ResultBO updateOne(BookBO bookBO);

    /**
     * 查询
     *
     * @param id
     * @return 查询对象
     */
    ResultBO<BookBO> queryById(Long id);

    /**
     * 分页查询列表
     *
     * @param pagingQueryBO
     * @param bookQueryBO
     * @return 查询列表
     */
    ResultBO<PagingQueryBO<BookBO>> list(PagingQueryBO pagingQueryBO, BookQueryBO bookQueryBO);
}