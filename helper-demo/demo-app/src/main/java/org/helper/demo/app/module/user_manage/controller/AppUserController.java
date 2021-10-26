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
package org.helper.demo.app.module.user_manage.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.stream.Collectors;
import org.helper.base.common.bo.PagingQueryBO;
import org.helper.base.common.bo.ResultBO;
import org.helper.base.common.req.IdReqDTO;
import org.helper.base.common.req.PagingQueryReqDTO;
import org.helper.base.common.resp.PagingQueryRespDTO;
import org.helper.base.common.resp.ResultRespDTO;
import org.helper.demo.app.module.user_manage.dto.req.AppUserQueryReqDTO;
import org.helper.demo.app.module.user_manage.dto.req.AppUserReqDTO;
import org.helper.demo.app.module.user_manage.dto.resp.AppUserRespDTO;
import org.helper.demo.service.module.user_manage.bo.AppUserBO;
import org.helper.demo.service.module.user_manage.service.IAppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 类名:AppUserController
 * 2021/10/26 23:13
 * <p>
 *
 * @author zyw
 * @version 1.0.0
 */
@RestController
@RequestMapping("/appUser")
@Api("APP用户")
public class AppUserController {
    @Autowired
    private IAppUserService appUserService;

    private static final Logger logger = LoggerFactory.getLogger(AppUserController.class);

    @PostMapping("/add/one")
    @ApiOperation(value = "新增")
    public ResultRespDTO<AppUserRespDTO> addOne(@RequestBody AppUserReqDTO req) {
        AppUserBO appUserBO = req.toAppUserBO();
        ResultBO<AppUserBO> resultBO = appUserService.addOne(appUserBO);
        if (resultBO.getResult()) {
            return ResultRespDTO.success(new AppUserRespDTO(resultBO.getData()));
        }
        return ResultRespDTO.error(resultBO);
    }

    @PostMapping("/delete/id")
    @ApiOperation(value = "删除")
    public ResultRespDTO deleteById(@RequestBody IdReqDTO req) {
        return ResultRespDTO.result(appUserService.deleteById(req.getId()));
    }

    @PostMapping("/update/one")
    @ApiOperation(value = "更新")
    public ResultRespDTO updateOne(@RequestBody AppUserReqDTO req) {
        AppUserBO appUserBO = req.toAppUserBO();
        return ResultRespDTO.result(appUserService.updateOne(appUserBO));
    }

    @PostMapping("/query/id")
    @ApiOperation(value = "查询")
    public ResultRespDTO<AppUserRespDTO> queryById(@RequestBody IdReqDTO req) {
        ResultBO<AppUserBO> resultBO = appUserService.queryById(req.getId());
        if (resultBO.getResult()) {
            return ResultRespDTO.success(new AppUserRespDTO(resultBO.getData()));
        }
        return ResultRespDTO.error(resultBO);
    }

    @PostMapping("/list")
    @ApiOperation(value = "分页查询")
    public ResultRespDTO<PagingQueryRespDTO<AppUserRespDTO>> list(@RequestBody PagingQueryReqDTO<AppUserQueryReqDTO> req) {
        ResultBO<PagingQueryBO<AppUserBO>> resultBO = appUserService.list(req.toPagingQueryBO(), req.getQuery().toAppUserQueryBO());
        if (resultBO.getResult()) {
            return ResultRespDTO.success(new PagingQueryRespDTO(resultBO.getData(),resultBO.getData().getData().stream().map(AppUserRespDTO::new).collect(Collectors.toList())));
        }
        return ResultRespDTO.error(resultBO);
    }
}