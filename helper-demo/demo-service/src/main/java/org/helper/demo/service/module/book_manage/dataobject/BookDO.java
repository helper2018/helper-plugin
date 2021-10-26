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
package org.helper.demo.service.module.book_manage.dataobject;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 类名:BookDO
 * 2021/10/26 23:13
 * <p>
 *
 * @author zyw
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDO {
    /**
     * ID
     */
    private Long id;

    /**
     * 条形码
     */
    private Long barCode;

    /**
     * 等级
     */
    private String levelNum;

    /**
     * 标题
     */
    private String title;

    /**
     * 原标题
     */
    private String originTitle;

    /**
     * 子标题
     */
    private String subtitle;

    /**
     * 作者
     */
    private String author;

    /**
     * 包装:精装,平装
     */
    private String binding;

    /**
     * 总页数
     */
    private Integer pages;

    /**
     * 封面图片(中)
     */
    private String imagesMedium;

    /**
     * 封面图片(大)
     */
    private String imagesLarge;

    /**
     * 出版日期
     */
    private Date pubdate;

    /**
     * 出版商
     */
    private String publisher;

    /**
     * isbn(10位)
     */
    private Long isbn10;

    /**
     * isbn(13位)
     */
    private Long isbn13;

    /**
     * 简介
     */
    private String summary;

    /**
     * 定价(分)
     */
    private Integer price;

    /**
     * 版次
     */
    private Byte edition;

    /**
     * 图书所属用户id
     */
    private Long ownerId;

    /**
     * 状态:1-有效，0-无效
     */
    private Byte status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date lastModifyTime;
}