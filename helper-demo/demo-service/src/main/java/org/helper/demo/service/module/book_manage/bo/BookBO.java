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
package org.helper.demo.service.module.book_manage.bo;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.helper.demo.service.module.book_manage.dataobject.BookDO;

/**
 * <p>
 * 类名:BookBO
 * 2021/10/26 23:13
 * <p>
 *
 * @author zyw
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookBO {
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

    public BookDO toBookDO() {
        BookDO bookDO = new BookDO();
        bookDO.setId(this.id);
        bookDO.setBarCode(this.barCode);
        bookDO.setLevelNum(this.levelNum);
        bookDO.setTitle(this.title);
        bookDO.setOriginTitle(this.originTitle);
        bookDO.setSubtitle(this.subtitle);
        bookDO.setAuthor(this.author);
        bookDO.setBinding(this.binding);
        bookDO.setPages(this.pages);
        bookDO.setImagesMedium(this.imagesMedium);
        bookDO.setImagesLarge(this.imagesLarge);
        bookDO.setPubdate(this.pubdate);
        bookDO.setPublisher(this.publisher);
        bookDO.setIsbn10(this.isbn10);
        bookDO.setIsbn13(this.isbn13);
        bookDO.setSummary(this.summary);
        bookDO.setPrice(this.price);
        bookDO.setEdition(this.edition);
        bookDO.setOwnerId(this.ownerId);
        bookDO.setStatus(this.status);
        bookDO.setCreateTime(this.createTime);
        bookDO.setLastModifyTime(this.lastModifyTime);
        return bookDO;
    }

    public BookBO(BookDO bookDO) {
        this.id = bookDO.getId();
        this.barCode = bookDO.getBarCode();
        this.levelNum = bookDO.getLevelNum();
        this.title = bookDO.getTitle();
        this.originTitle = bookDO.getOriginTitle();
        this.subtitle = bookDO.getSubtitle();
        this.author = bookDO.getAuthor();
        this.binding = bookDO.getBinding();
        this.pages = bookDO.getPages();
        this.imagesMedium = bookDO.getImagesMedium();
        this.imagesLarge = bookDO.getImagesLarge();
        this.pubdate = bookDO.getPubdate();
        this.publisher = bookDO.getPublisher();
        this.isbn10 = bookDO.getIsbn10();
        this.isbn13 = bookDO.getIsbn13();
        this.summary = bookDO.getSummary();
        this.price = bookDO.getPrice();
        this.edition = bookDO.getEdition();
        this.ownerId = bookDO.getOwnerId();
        this.status = bookDO.getStatus();
        this.createTime = bookDO.getCreateTime();
        this.lastModifyTime = bookDO.getLastModifyTime();
    }
}