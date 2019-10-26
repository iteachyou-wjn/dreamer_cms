/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER
 * Copyright © Dreamer CMS 2019 All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.itechyou.cms.common;


import lombok.Getter;

/**
 * @author TODAY <br>
 * 		   2019-10-26 15:29
 */
@Getter
public enum UserStatus {

    NORMAL(0, "正常"),
    INACTIVE(1, "账号尚未激活"),
    LOCKED(2, "账号被锁"),
    RECYCLE(3, "账号不存在");

//    @EnumValue
    private final int code;
    private final String msg;

    UserStatus(int code, String msg) {
        this.msg = msg;
        this.code = code;
    }

}
