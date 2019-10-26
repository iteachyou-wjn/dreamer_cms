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
package cn.itechyou.cms.common;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 
 * @author TODAY <br>
 *         2019-10-26 22:34
 */
public interface Constant extends Serializable {

    /** 项目名称 */
    String projecName = "梦想家CMS内容管理系统";

    String SESSION_USER = "SESSION_USER";

    String PAGE_NUM_KEY = "pageNum";

    String PAGE_SIZE_KEY = "pageSize";

    Integer PAGE_NUM_VALUE = 1;

    Integer PAGE_SIZE_VALUE = 10;

    String KAPTCHA = "KAPTCHA";

    String ERROR = "/admin/common/error";

    String BLANK = "";

    String DEFAULT_ENCODING = "UTF-8";
    Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    String OPERATION_OK = CmsStatus.HTTP_SUCCESS.getDescription();
    String OPERATION_ERROR = CmsStatus.HTTP_ERROR.getDescription();

    String NOT_FOUND = "Not Found";
    String BAD_REQUEST = "Bad Request";
    String UNAUTHORIZED = "Unauthorized";
    String ACCESS_FORBIDDEN = "Access Forbidden";
    String METHOD_NOT_ALLOWED = "Method Not Allowed";
    String INTERNAL_SERVER_ERROR = "Internal Server Error";

    int DEFAULT_LIST_SIZE = 8;
    
    String  PARAMETER_SIZE              = "size";
    String  PARAMETER_CURRENT           = "current";
    String SPLIT_REGEXP = "[;|,]";

}
