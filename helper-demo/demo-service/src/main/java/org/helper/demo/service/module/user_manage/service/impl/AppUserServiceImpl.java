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
package org.helper.demo.service.module.user_manage.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.helper.base.common.bo.PagingQueryBO;
import org.helper.base.common.bo.ResultBO;
import org.helper.base.enums.StatusEnum;
import org.helper.base.utils.DateUtil;
import org.helper.base.utils.StringUtil;
import org.helper.demo.service.module.user_manage.bo.AppUserBO;
import org.helper.demo.service.module.user_manage.bo.AppUserQueryBO;
import org.helper.demo.service.module.user_manage.dao.IAppUserDAO;
import org.helper.demo.service.module.user_manage.dataobject.AppUserDO;
import org.helper.demo.service.module.user_manage.dataobject.AppUserExample;
import org.helper.demo.service.module.user_manage.service.IAppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 类名:AppUserServiceImpl
 * 2021/10/26 23:13
 * <p>
 *
 * @author zyw
 * @version 1.0.0
 */
@Service
public class AppUserServiceImpl implements IAppUserService {
    private static final Logger logger = LoggerFactory.getLogger(AppUserServiceImpl.class);

    @Autowired
    private IAppUserDAO appUserDAO;

    /**
     * 新增
     *
     * @param appUserBO
     * @return 是否新增成功
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultBO<AppUserBO> addOne(AppUserBO appUserBO) {
        // 1.校验对象
        if (!StringUtil.hasText(appUserBO.getName())) {
            return ResultBO.fail("用户名称不能为空");
        }
        if (null == appUserBO.getMobile()) {
            return ResultBO.fail("手机号不能为空");
        }
        
        appUserBO.setStatus(StatusEnum.VALID.getStatus());
        appUserBO.setCreateTime(DateUtil.getCurrentDate());
        appUserBO.setLastModifyTime(DateUtil.getCurrentDate());

        // 2.bo 转 do
        AppUserDO appUserDO = appUserBO.toAppUserDO();
        // 3.持久化对象
        appUserDAO.insert(appUserDO);
        Long id = appUserDO.getId();
        if (null != id) {
            return ResultBO.success(new AppUserBO(appUserDO));
        }
        return ResultBO.fail("新增APP用户失败");
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
        if (1 == appUserDAO.deleteByPrimaryKey(id)) {
            return ResultBO.success();
        }
        return ResultBO.fail(String.format("请确认记录(id=%d)是否存在", id));
    }

    /**
     * 更新
     *
     * @param appUserBO
     * @return 是否更新成功
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultBO updateOne(AppUserBO appUserBO) {
        // 1.校验对象
        if (null == appUserBO.getId()) {
            return ResultBO.fail("主键不应为空");
        }
        
        appUserBO.setLastModifyTime(DateUtil.getCurrentDate());

        // 2.bo 转 do
        AppUserDO appUserDO = appUserBO.toAppUserDO();
        // 3.持久化对象
        int count = appUserDAO.updateByPrimaryKeySelective(appUserDO);
        if (1 == count) {
            return ResultBO.success();
        }
        return ResultBO.fail(String.format("请确认记录(id=%d)是否存在", appUserBO.getId()));
    }

    /**
     * 查询
     *
     * @param id
     * @return 查询对象
     */
    @Override
    public ResultBO<AppUserBO> queryById(Long id) {
        AppUserDO appUserDO = appUserDAO.selectByPrimaryKey(id);
        if (null != appUserDO) {
            return ResultBO.success(new AppUserBO(appUserDO));
        }
        return ResultBO.fail(String.format("请确认记录(id=%d)是否存在", id));
    }

    /**
     * 分页查询列表
     *
     * @param pagingQueryBO
     * @param appUserQueryBO
     * @return 查询列表
     */
    @Override
    public ResultBO<PagingQueryBO<AppUserBO>> list(PagingQueryBO pagingQueryBO, AppUserQueryBO appUserQueryBO) {
        AppUserExample appUserExample = new AppUserExample();
        AppUserExample.Criteria criteria = appUserExample.createCriteria();
        if (null != appUserQueryBO.getStatus()) {
            criteria.andStatusEqualTo(appUserQueryBO.getStatus());
        }
        if (StringUtil.hasText(appUserQueryBO.getName())) {
            criteria.andNameLike(appUserQueryBO.getName());
        }
        if (null != appUserQueryBO.getMobile()) {
            criteria.andMobileEqualTo(appUserQueryBO.getMobile());
        }
        if (StringUtil.hasText(appUserQueryBO.getEmail())) {
            criteria.andEmailLike(appUserQueryBO.getEmail());
        }
        if (StringUtil.hasText(appUserQueryBO.getToken())) {
            criteria.andTokenLike(appUserQueryBO.getToken());
        }
        
        long dataCount = appUserDAO.countByExample(appUserExample);
        pagingQueryBO.initTotal(dataCount);
        if (0 == dataCount) {
            return ResultBO.fail("没有查询到数据");
        }
        if (pagingQueryBO.getPages() < pagingQueryBO.getPageNum()) {
            return ResultBO.fail("查询页码大于总页数");
        }
        
        // 查询数据列表
        appUserExample.setFrom(pagingQueryBO.getFrom());
        appUserExample.setSize(pagingQueryBO.getPageSize());
        if (StringUtil.hasText(appUserQueryBO.getOrderBy())) {
            appUserExample.setOrderByClause(appUserQueryBO.getOrderBy());
        }
        
        List<AppUserDO> appUserDOList = appUserDAO.selectByExample(appUserExample);
        pagingQueryBO.setData(appUserDOList.stream().map(AppUserBO::new).collect(Collectors.toList()));
        
        return ResultBO.success(pagingQueryBO);
    }
}