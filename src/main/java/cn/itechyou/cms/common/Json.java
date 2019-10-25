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

import java.util.function.Function;

/**
 * @author TODAY <br>
 *         2019-10-25 21:03
 */
public class Json implements Result {

    private static final long serialVersionUID = 1L;

    private Object data;
    private String message;
    private int code = 200;
    private boolean success;

    /**
     * Apply the common {@link Json} result
     * 
     * @param <T>
     * @param func
     *            the {@link Function}
     * @param param
     *            parameter
     * @return
     */
    public static final <T> Json apply(Function<T, Boolean> func, T param) {
        if (func.apply(param)) {
            return Json.ok();
        }
        return Json.failed();
    }

    /**
     * 
     * @param <T>
     * @param success
     * @return
     */
    public static final <T> Json apply(boolean success) {
        if (success) {
            return Json.ok();
        }
        return Json.failed();
    }

    /**
     * @param success
     *            if success
     * @param status
     *            error status
     * @param message
     *            the message of the response
     * @param data
     *            response data
     */
    public static Json create(boolean success, int status, String message, Object data) {
        return new Json()//
                .data(data)//
                .message(message)//
                .code(status)//
                .success(success);
    }

    public static Json ok() {
        return create(true, STATUS_SUCCESS, OPERATION_OK, null);
    }

    public static Json ok(String message, Object data) {
        return create(true, STATUS_SUCCESS, message, data);
    }

    public static Json ok(Object data) {
        return create(true, STATUS_SUCCESS, OPERATION_OK, data);
    }

    public static Json ok(String message) {
        return create(true, STATUS_SUCCESS, message, null);
    }

    public static Json ok(CmsStatus status) {
        return create(true, status.getCode(), status.getDescription(), null);
    }

    /**
     * default failed json
     */
    public static Json failed() {
        return create(false, STATUS_FAILED, OPERATION_ERROR, null);
    }

    public static Json failed(Object data) {
        return create(false, STATUS_FAILED, OPERATION_ERROR, data);
    }

    public static Json failed(CmsStatus status) {
        return create(false, status.getCode(), status.getDescription(), null);
    }

    public static Json failed(String message) {
        return create(false, STATUS_FAILED, message, null);
    }

    public static Json failed(String message, Object data) {
        return create(false, STATUS_FAILED, message, data);
    }

    public static Json failed(String message, int status) {
        return create(false, status, message, null);
    }

    public static Json failed(String message, int status, Object data) {
        return create(false, status, message, data);
    }

    public static Json badRequest() {
        return badRequest(BAD_REQUEST);
    }

    public static Json badRequest(String msg) {
        return create(false, 400, msg, null);
    }

    public static Json notFound() {
        return notFound(NOT_FOUND);
    }

    public static Json notFound(String msg) {
        return create(false, 404, msg, null);
    }

    public static Json unauthorized() {
        return unauthorized(UNAUTHORIZED);
    }

    public static Json unauthorized(String msg) {
        return failed(msg, 401);
    }

    public static Json accessForbidden() {
        return accessForbidden(ACCESS_FORBIDDEN);
    }

    public static Json accessForbidden(String msg) {
        return failed(msg, 403);
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return new StringBuilder()//
                .append("{\"message\":\"").append(message)//
                .append("\",\"code\":").append(code)//
                .append(",\"data\":\"").append(data)//
                .append("\",\"success\":").append(success)//
                .append("}")//
                .toString();
    }

    public final Json data(Object data) {
        this.data = data;
        return this;
    }

    public final Json message(String message) {
        this.message = message;
        return this;
    }

    public final Json code(int code) {
        this.code = code;
        return this;
    }

    public final Json success(boolean success) {
        this.success = success;
        return this;
    }

}
