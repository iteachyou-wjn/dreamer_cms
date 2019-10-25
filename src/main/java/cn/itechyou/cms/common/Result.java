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

/**
 * The request result
 * 
 * @author TODAY <br>
 * 		   2019-10-25 21:01
 */
public interface Result extends Constant {
    
    public static final int STATUS_SUCCESS = 200;
    public static final int STATUS_FAILED = 500;

    Object getData();

    int getCode();

    boolean isSuccess();

    String getMessage();
}
