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
package org.helper.demo.service.module.book_manage.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.helper.base.common.bo.PagingQueryBO;
import org.helper.base.common.bo.ResultBO;
import org.helper.base.enums.StatusEnum;
import org.helper.base.utils.DateUtil;
import org.helper.base.utils.StringUtil;
import org.helper.demo.service.module.book_manage.bo.BookBO;
import org.helper.demo.service.module.book_manage.bo.BookQueryBO;
import org.helper.demo.service.module.book_manage.dao.IBookDAO;
import org.helper.demo.service.module.book_manage.dataobject.BookDO;
import org.helper.demo.service.module.book_manage.dataobject.BookExample;
import org.helper.demo.service.module.book_manage.service.IBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 类名:BookServiceImpl
 * 2021/10/26 23:13
 * <p>
 *
 * @author zyw
 * @version 1.0.0
 */
@Service
public class BookServiceImpl implements IBookService {
    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    @Autowired
    private IBookDAO bookDAO;

    /**
     * 新增
     *
     * @param bookBO
     * @return 是否新增成功
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultBO<BookBO> addOne(BookBO bookBO) {
        // 1.校验对象
        if (!StringUtil.hasText(bookBO.getTitle())) {
            return ResultBO.fail("标题不能为空");
        }
        if (!StringUtil.hasText(bookBO.getAuthor())) {
            return ResultBO.fail("作者不能为空");
        }
        if (null == bookBO.getEdition()) {
            return ResultBO.fail("版次不能为空");
        }
        
        bookBO.setStatus(StatusEnum.VALID.getStatus());
        bookBO.setCreateTime(DateUtil.getCurrentDate());
        bookBO.setLastModifyTime(DateUtil.getCurrentDate());

        // 2.bo 转 do
        BookDO bookDO = bookBO.toBookDO();
        // 3.持久化对象
        bookDAO.insert(bookDO);
        Long id = bookDO.getId();
        if (null != id) {
            return ResultBO.success(new BookBO(bookDO));
        }
        return ResultBO.fail("新增图书失败");
    }

    /**
     * 删除
     *
     * @param id
     * @return 是否删除成功
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultBO deleteById(Long id) {
        if (1 == bookDAO.deleteByPrimaryKey(id)) {
            return ResultBO.success();
        }
        return ResultBO.fail(String.format("请确认记录(id=%d)是否存在", id));
    }

    /**
     * 更新
     *
     * @param bookBO
     * @return 是否更新成功
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultBO updateOne(BookBO bookBO) {
        // 1.校验对象
        if (null == bookBO.getId()) {
            return ResultBO.fail("主键不应为空");
        }
        
        bookBO.setLastModifyTime(DateUtil.getCurrentDate());

        // 2.bo 转 do
        BookDO bookDO = bookBO.toBookDO();
        // 3.持久化对象
        int count = bookDAO.updateByPrimaryKeySelective(bookDO);
        if (1 == count) {
            return ResultBO.success();
        }
        return ResultBO.fail(String.format("请确认记录(id=%d)是否存在", bookBO.getId()));
    }

    /**
     * 查询
     *
     * @param id
     * @return 查询对象
     */
    @Override
    public ResultBO<BookBO> queryById(Long id) {
        BookDO bookDO = bookDAO.selectByPrimaryKey(id);
        if (null != bookDO) {
            return ResultBO.success(new BookBO(bookDO));
        }
        return ResultBO.fail(String.format("请确认记录(id=%d)是否存在", id));
    }

    /**
     * 分页查询列表
     *
     * @param pagingQueryBO
     * @param bookQueryBO
     * @return 查询列表
     */
    @Override
    public ResultBO<PagingQueryBO<BookBO>> list(PagingQueryBO pagingQueryBO, BookQueryBO bookQueryBO) {
        BookExample bookExample = new BookExample();
        BookExample.Criteria criteria = bookExample.createCriteria();
        if (null != bookQueryBO.getStatus()) {
            criteria.andStatusEqualTo(bookQueryBO.getStatus());
        }
        if (null != bookQueryBO.getBarCode()) {
            criteria.andBarCodeEqualTo(bookQueryBO.getBarCode());
        }
        if (StringUtil.hasText(bookQueryBO.getLevelNum())) {
            criteria.andLevelNumLike(bookQueryBO.getLevelNum());
        }
        if (StringUtil.hasText(bookQueryBO.getTitle())) {
            criteria.andTitleLike(bookQueryBO.getTitle());
        }
        if (StringUtil.hasText(bookQueryBO.getOriginTitle())) {
            criteria.andOriginTitleLike(bookQueryBO.getOriginTitle());
        }
        if (StringUtil.hasText(bookQueryBO.getSubtitle())) {
            criteria.andSubtitleLike(bookQueryBO.getSubtitle());
        }
        if (StringUtil.hasText(bookQueryBO.getAuthor())) {
            criteria.andAuthorLike(bookQueryBO.getAuthor());
        }
        if (StringUtil.hasText(bookQueryBO.getBinding())) {
            criteria.andBindingLike(bookQueryBO.getBinding());
        }
        if (null != bookQueryBO.getPages()) {
            criteria.andPagesEqualTo(bookQueryBO.getPages());
        }
        if (StringUtil.hasText(bookQueryBO.getImagesMedium())) {
            criteria.andImagesMediumLike(bookQueryBO.getImagesMedium());
        }
        if (StringUtil.hasText(bookQueryBO.getImagesLarge())) {
            criteria.andImagesLargeLike(bookQueryBO.getImagesLarge());
        }
        if (null != bookQueryBO.getPubdate()) {
            criteria.andPubdateEqualTo(bookQueryBO.getPubdate());
        }
        if (StringUtil.hasText(bookQueryBO.getPublisher())) {
            criteria.andPublisherLike(bookQueryBO.getPublisher());
        }
        if (null != bookQueryBO.getIsbn10()) {
            criteria.andIsbn10EqualTo(bookQueryBO.getIsbn10());
        }
        if (null != bookQueryBO.getIsbn13()) {
            criteria.andIsbn13EqualTo(bookQueryBO.getIsbn13());
        }
        if (StringUtil.hasText(bookQueryBO.getSummary())) {
            criteria.andSummaryLike(bookQueryBO.getSummary());
        }
        if (null != bookQueryBO.getPrice()) {
            criteria.andPriceEqualTo(bookQueryBO.getPrice());
        }
        if (null != bookQueryBO.getEdition()) {
            criteria.andEditionEqualTo(bookQueryBO.getEdition());
        }
        
        long dataCount = bookDAO.countByExample(bookExample);
        pagingQueryBO.initTotal(dataCount);
        if (0 == dataCount) {
            return ResultBO.fail("没有查询到数据");
        }
        if (pagingQueryBO.getPages() < pagingQueryBO.getPageNum()) {
            return ResultBO.fail("查询页码大于总页数");
        }
        
        // 查询数据列表
        bookExample.setFrom(pagingQueryBO.getFrom());
        bookExample.setSize(pagingQueryBO.getPageSize());
        if (StringUtil.hasText(bookQueryBO.getOrderBy())) {
            bookExample.setOrderByClause(bookQueryBO.getOrderBy());
        }
        
        List<BookDO> bookDOList = bookDAO.selectByExample(bookExample);
        pagingQueryBO.setData(bookDOList.stream().map(BookBO::new).collect(Collectors.toList()));
        
        return ResultBO.success(pagingQueryBO);
    }
}