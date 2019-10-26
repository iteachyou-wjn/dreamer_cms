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
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * @author TODAY <br>
 *         2019-10-26 22:27
 */
@Getter
@Setter
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    //	private Date createTime;
    /** 创建时间 */
    private LocalDateTime createTime;
    private String createBy;
    /** 最后更改时间 */
    //    private LocalDateTime lastModify;
    private String updateBy;
    private LocalDateTime updateTime;
}
