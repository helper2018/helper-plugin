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
package org.helper.demo.service.module.book_manage.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.helper.demo.service.module.book_manage.dataobject.BookDO;
import org.helper.demo.service.module.book_manage.dataobject.BookExample;

/**
 * <p>
 * 接口名:IBookDAO
 * 2021/10/26 23:13
 * <p>
 *
 * @author zyw
 * @version 1.0.0
 */
public interface IBookDAO {
    long countByExample(BookExample example);

    int deleteByExample(BookExample example);

    int deleteByPrimaryKey(Long id);

    int insert(BookDO record);

    int insertSelective(BookDO record);

    List<BookDO> selectByExample(BookExample example);

    BookDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") BookDO record, @Param("example") BookExample example);

    int updateByExample(@Param("record") BookDO record, @Param("example") BookExample example);

    int updateByPrimaryKeySelective(BookDO record);

    int updateByPrimaryKey(BookDO record);
}