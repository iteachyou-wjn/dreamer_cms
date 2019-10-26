/**
 * Original Author -> 杨海健 (taketoday@foxmail.com) https://taketoday.cn
 * Copyright © TODAY & 2017 - 2019 All Rights Reserved.
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *   
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see [http://www.gnu.org/licenses/]
 */
package cn.itechyou.cms.common;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import cn.itechyou.cms.utils.StringUtils;

/**
 * @author TODAY <br>
 *         2019-04-20 21:58
 */
public class PageableMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Pageable.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, //
                                  ModelAndViewContainer mavContainer, //
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception//
    {
        return new NativeWebRequestPageable(webRequest);
    }

    public static final class NativeWebRequestPageable implements Pageable, Serializable {

        private static final long serialVersionUID = 1L;

        private Integer size;
        private Integer current;

        private final NativeWebRequest request;

        public NativeWebRequestPageable(NativeWebRequest nativeWebRequest) {
            this.request = nativeWebRequest;
        }

        @Override
        public int getCurrent() {
            if (current == null) {
                final String parameter = request.getParameter(Constant.PARAMETER_CURRENT);
                if (StringUtils.isEmpty(parameter)) {
                    current = 1;
                }
                else if ((current = Integer.valueOf(parameter)) <= 0) {
                    throw new IllegalArgumentException("only 'page > 0'");
                }
            }
            return current.intValue();
        }

        @Override
        public int getSize() {
            if (size == null) {
                int s;
                final String parameter = request.getParameter(Constant.PARAMETER_SIZE);
                if (StringUtils.isEmpty(parameter)) {
                    //                s = BlogConfiguration.getInstance().getListSize();
                    s = Constant.DEFAULT_LIST_SIZE;
                }
                else {
                    s = Integer.parseInt(parameter);
                    if (s <= 0) {
                        throw new IllegalArgumentException("only 'size > 0'");
                    }
                    // if (s > Configuration.getInstance().getMaxPageSize()) { TODO
                    //      throw accessForbidden();
                    // }
                }
                return size = s;
            }
            return size.intValue();
        }

        // Object
        // ----------------------

        @Override
        public boolean equals(Object obj) {

            if (obj == this) {
                return true;
            }

            if (obj instanceof Pageable) {
                final Pageable other = (Pageable) obj;
                return other.getCurrent() == this.current && other.getSize() == this.size;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(size, current);
        }
    }

}
