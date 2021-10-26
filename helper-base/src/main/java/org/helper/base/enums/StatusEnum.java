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
package org.helper.base.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * <p>
 * 枚举类名:StatusEnum.
 * 2021/10/12 14:31
 * <p>
 *
 * @author zyw
 * @version 1.0.0
 */
public enum StatusEnum {

    INVALID((byte) 0, "无效"),
    VALID((byte) 1, "有效");

    /**
     * 状态
     */
    @Getter
    public byte status;

    /**
     * 含义
     */
    @Getter
    public String name;

    StatusEnum(byte status, String name) {
        this.status = status;
        this.name = name;
    }

    public static StatusEnum getDataStatusEnum(byte status) {
        Optional<StatusEnum> statusEnumOptional = Arrays.stream(StatusEnum.values()).filter(statusEnum -> statusEnum.getStatus() == status).findFirst();
        return statusEnumOptional.orElse(null);
    }

}
