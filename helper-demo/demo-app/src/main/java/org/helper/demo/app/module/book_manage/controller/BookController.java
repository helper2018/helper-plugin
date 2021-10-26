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
package org.helper.demo.app.module.book_manage.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.stream.Collectors;
import org.helper.base.common.bo.PagingQueryBO;
import org.helper.base.common.bo.ResultBO;
import org.helper.base.common.req.IdReqDTO;
import org.helper.base.common.req.PagingQueryReqDTO;
import org.helper.base.common.resp.PagingQueryRespDTO;
import org.helper.base.common.resp.ResultRespDTO;
import org.helper.demo.app.module.book_manage.dto.req.BookQueryReqDTO;
import org.helper.demo.app.module.book_manage.dto.req.BookReqDTO;
import org.helper.demo.app.module.book_manage.dto.resp.BookRespDTO;
import org.helper.demo.service.module.book_manage.bo.BookBO;
import org.helper.demo.service.module.book_manage.service.IBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 类名:BookController
 * 2021/10/26 23:13
 * <p>
 *
 * @author zyw
 * @version 1.0.0
 */
@RestController
@RequestMapping("/book")
@Api("图书")
public class BookController {
    @Autowired
    private IBookService bookService;

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @PostMapping("/add/one")
    @ApiOperation(value = "新增")
    public ResultRespDTO<BookRespDTO> addOne(@RequestBody BookReqDTO req) {
        BookBO bookBO = req.toBookBO();
        ResultBO<BookBO> resultBO = bookService.addOne(bookBO);
        if (resultBO.getResult()) {
            return ResultRespDTO.success(new BookRespDTO(resultBO.getData()));
        }
        return ResultRespDTO.error(resultBO);
    }

    @PostMapping("/delete/id")
    @ApiOperation(value = "删除")
    public ResultRespDTO deleteById(@RequestBody IdReqDTO req) {
        return ResultRespDTO.result(bookService.deleteById(req.getId()));
    }

    @PostMapping("/update/one")
    @ApiOperation(value = "更新")
    public ResultRespDTO updateOne(@RequestBody BookReqDTO req) {
        BookBO bookBO = req.toBookBO();
        return ResultRespDTO.result(bookService.updateOne(bookBO));
    }

    @PostMapping("/query/id")
    @ApiOperation(value = "查询")
    public ResultRespDTO<BookRespDTO> queryById(@RequestBody IdReqDTO req) {
        ResultBO<BookBO> resultBO = bookService.queryById(req.getId());
        if (resultBO.getResult()) {
            return ResultRespDTO.success(new BookRespDTO(resultBO.getData()));
        }
        return ResultRespDTO.error(resultBO);
    }

    @PostMapping("/list")
    @ApiOperation(value = "分页查询")
    public ResultRespDTO<PagingQueryRespDTO<BookRespDTO>> list(@RequestBody PagingQueryReqDTO<BookQueryReqDTO> req) {
        ResultBO<PagingQueryBO<BookBO>> resultBO = bookService.list(req.toPagingQueryBO(), req.getQuery().toBookQueryBO());
        if (resultBO.getResult()) {
            return ResultRespDTO.success(new PagingQueryRespDTO(resultBO.getData(),resultBO.getData().getData().stream().map(BookRespDTO::new).collect(Collectors.toList())));
        }
        return ResultRespDTO.error(resultBO);
    }
}