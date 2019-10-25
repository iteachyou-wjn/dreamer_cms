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
package cn.itechyou.cms.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import cn.itechyou.cms.common.Constant;
import cn.itechyou.cms.entity.User;
import cn.itechyou.cms.exception.AccessForbiddenException;
import cn.itechyou.cms.exception.BadRequestException;
import cn.itechyou.cms.exception.NotFoundException;
import cn.itechyou.cms.exception.UnauthorizedException;

/**
 * @author TODAY <br>
 *         2019-04-21 09:44
 */
//@Slf4j
public abstract class WebUtils {

    public static boolean isAjax(HttpServletRequest webRequest) {
        final String requestedWith = webRequest.getHeader("X-Requested-With");
        if (requestedWith == null) {
            return false;
        }
        return "XMLHttpRequest".equals(requestedWith);
    }

    /**
     * Require Login
     */
    public static void requireLogin() {

        final Object attribute = getLoginUser();

        if (attribute instanceof User) {
            return;
        }
        throw unauthorized();
    }

    /**
     * 
     * @return
     */
    public static Object getLoginUser() {
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            return requestAttributes.getAttribute(Constant.SESSION_USER, RequestAttributes.SCOPE_SESSION);
        }
        return null;
    }

    /**
     * 
     * @param obj
     * @return
     */
    public static <T> T nonNull(T obj) {
        if (obj == null) {
            throw new NotFoundException("请求了不存在的资源");
        }
        return obj;
    }

    public static BadRequestException badRequest() {
        return new BadRequestException();
    }

    public static UnauthorizedException unauthorized() {
        return new UnauthorizedException();
    }

    public static NotFoundException notFound() {
        return new NotFoundException();
    }

    public static AccessForbiddenException accessForbidden() {
        return new AccessForbiddenException();
    }

}
