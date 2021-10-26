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
package org.helper.demo.app.module.book_manage.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.helper.base.common.req.BaseQueryReqDTO;
import org.helper.demo.service.module.book_manage.bo.BookQueryBO;

/**
 * <p>
 * 类名:BookQueryReqDTO
 * 2021/10/26 23:13
 * <p>
 *
 * @author zyw
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@ApiModel("BookQueryReqDTO")
public class BookQueryReqDTO extends BaseQueryReqDTO {
    @ApiModelProperty(value = "条形码", example = "1", dataType = "Long", required = false)
    private Long barCode;

    @ApiModelProperty(value = "等级(maxLen=16)", example = "helper", dataType = "String", required = false)
    private String levelNum;

    @ApiModelProperty(value = "标题(maxLen=128)", example = "helper", dataType = "String", required = true)
    private String title;

    @ApiModelProperty(value = "原标题(maxLen=128)", example = "helper", dataType = "String", required = false)
    private String originTitle;

    @ApiModelProperty(value = "子标题(maxLen=128)", example = "helper", dataType = "String", required = false)
    private String subtitle;

    @ApiModelProperty(value = "作者(maxLen=64)", example = "helper", dataType = "String", required = true)
    private String author;

    @ApiModelProperty(value = "包装:精装,平装(maxLen=16)", example = "helper", dataType = "String", required = false)
    private String binding;

    @ApiModelProperty(value = "总页数", example = "100000", dataType = "Integer", required = false)
    private Integer pages;

    @ApiModelProperty(value = "封面图片(中)(maxLen=512)", example = "helper", dataType = "String", required = false)
    private String imagesMedium;

    @ApiModelProperty(value = "封面图片(大)(maxLen=512)", example = "helper", dataType = "String", required = false)
    private String imagesLarge;

    @ApiModelProperty(value = "出版日期", example = "2021-10-26 23:13:09", dataType = "Date", required = false)
    private Date pubdate;

    @ApiModelProperty(value = "出版商(maxLen=128)", example = "helper", dataType = "String", required = false)
    private String publisher;

    @ApiModelProperty(value = "isbn(10位)", example = "1", dataType = "Long", required = false)
    private Long isbn10;

    @ApiModelProperty(value = "isbn(13位)", example = "1", dataType = "Long", required = false)
    private Long isbn13;

    @ApiModelProperty(value = "简介(maxLen=4096)", example = "helper", dataType = "String", required = false)
    private String summary;

    @ApiModelProperty(value = "定价(分)", example = "100000", dataType = "Integer", required = false)
    private Integer price;

    @ApiModelProperty(value = "版次", example = "1", dataType = "Byte", required = true)
    private Byte edition;

    public BookQueryBO toBookQueryBO() {
        BookQueryBO bookQueryBO = new BookQueryBO();
        bookQueryBO.setBarCode(this.barCode);
        bookQueryBO.setLevelNum(this.levelNum);
        bookQueryBO.setTitle(this.title);
        bookQueryBO.setOriginTitle(this.originTitle);
        bookQueryBO.setSubtitle(this.subtitle);
        bookQueryBO.setAuthor(this.author);
        bookQueryBO.setBinding(this.binding);
        bookQueryBO.setPages(this.pages);
        bookQueryBO.setImagesMedium(this.imagesMedium);
        bookQueryBO.setImagesLarge(this.imagesLarge);
        bookQueryBO.setPubdate(this.pubdate);
        bookQueryBO.setPublisher(this.publisher);
        bookQueryBO.setIsbn10(this.isbn10);
        bookQueryBO.setIsbn13(this.isbn13);
        bookQueryBO.setSummary(this.summary);
        bookQueryBO.setPrice(this.price);
        bookQueryBO.setEdition(this.edition);
        return bookQueryBO;
    }
}