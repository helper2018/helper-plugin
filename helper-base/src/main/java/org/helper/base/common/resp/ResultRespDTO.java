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
package org.helper.base.common.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.helper.base.common.bo.ResultBO;
import org.helper.base.enums.ResultCodeEnum;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * <p>
 * 类名:ResultDTO.
 * 2021/10/12 13:59
 * <p>
 *
 * @author zyw
 * @version 1.0.0
 */
@Data
@ApiModel("ResultRespDTO")
public class ResultRespDTO<T> {

    @ApiModelProperty(value = "1-成功 0-失败 10001-其他异常code(ErrorTypeEnum)", example = "1", dataType = "Integer", required = true)
    private Integer code;

    @ApiModelProperty(value = "消息", example = "成功", dataType = "String", required = true)
    private String msg;

    @ApiModelProperty(value = "返回数据", example = "T", dataType = "T", required = true)
    private T data;

    @ApiModelProperty(value = "响应时间", example = "2021-10-10 08:07:01", dataType = "Date", required = true)
    private Date timestamp = new Date();

    private static ResultRespDTO of(int code, String msg) {
        ResultRespDTO resultRespDTO = new ResultRespDTO();
        resultRespDTO.setCode(code);
        resultRespDTO.setMsg(msg);
        return resultRespDTO;
    }

    public static ResultRespDTO result(ResultBO resultBO) {
        ResultRespDTO resultRespDTO;
        if (resultBO.getResult()) {
            resultRespDTO = ResultRespDTO.of(ResultCodeEnum.SUCCESS.getCode(), null);
        } else {
            resultRespDTO = ResultRespDTO.of(ResultCodeEnum.ERROR.getCode(), resultBO.getFailReason());
        }
        return resultRespDTO;
    }

    public static <M> ResultRespDTO<M> success(M data) {
        ResultRespDTO<M> resultRespDTO = ResultRespDTO.of(ResultCodeEnum.SUCCESS.getCode(), null);
        resultRespDTO.setData(data);
        return resultRespDTO;
    }

    public static ResultRespDTO error(ResultBO resultBO) {
        return ResultRespDTO.of(ResultCodeEnum.ERROR.getCode(), resultBO.getFailReason());
    }

    public static ResultRespDTO error(String msg) {
        return ResultRespDTO.of(ResultCodeEnum.ERROR.getCode(), StringUtils.hasText(msg) ?
                msg : ResultCodeEnum.ERROR.getMsg());
    }
}




