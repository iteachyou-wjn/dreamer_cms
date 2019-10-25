/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER
 * Copyright © Dreamer CMS 2019 All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.itechyou.cms.exception;

import cn.itechyou.cms.common.Constant;

/**
 * @author TODAY <br>
 *         2018-10-30 16:51
 */
@SuppressWarnings("serial")
public class BadRequestException extends ApplicationRuntimeException {

    public BadRequestException() {
        this(Constant.BAD_REQUEST, null);
    }

    public BadRequestException(String message) {
        this(message, null);
    }

    public BadRequestException(Throwable cause) {
        this(Constant.BAD_REQUEST, cause);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

}
