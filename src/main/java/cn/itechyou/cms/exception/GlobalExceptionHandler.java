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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import cn.itechyou.cms.common.ExceptionEnum;
import cn.itechyou.cms.common.Json;
import cn.itechyou.cms.common.Result;

/**
 * 全局异常处理
 * @author jun hu
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 400 - 错误的请求
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ModelAndView handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("错误的请求", e);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("error/400");
        mv.addObject("code", ExceptionEnum.HTTP_BAD_REQUEST.getCode());
        mv.addObject("message", ExceptionEnum.HTTP_BAD_REQUEST.getMessage());
        return mv;
    }

    /**
     * 403 - 禁止访问
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UnauthorizedException.class)
    public ModelAndView handleUnauthorizedException(UnauthorizedException e) {
        log.error("禁止访问", e);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("error/403");
        mv.addObject("code", ExceptionEnum.HTTP_FORBIDDEN.getCode());
        mv.addObject("message", ExceptionEnum.HTTP_FORBIDDEN.getMessage());
        return mv;
    }

    /**
     * 404 -没有找到
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleNotFoundException(NoHandlerFoundException e) {
        log.error("资源没有找到", e);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("error/404");
        mv.addObject("code", ExceptionEnum.HTTP_NOT_FOUND.getCode());
        mv.addObject("message", ExceptionEnum.HTTP_NOT_FOUND.getMessage());
        return mv;
    }

    /**
     * 405 - 方法不允许
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ModelAndView handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("没有权限！", e);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("error/405");
        mv.addObject("code", ExceptionEnum.HTTP_METHOD_NOT_ALLOWED.getCode());
        mv.addObject("message", ExceptionEnum.HTTP_METHOD_NOT_ALLOWED.getMessage());
        return mv;
    }

    /**
     * 415 - 不支持的媒体类型
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ModelAndView handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        log.error("不支持的媒体类型", e);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("error/415");
        mv.addObject("code", ExceptionEnum.HTTP_UNSUPPORTED_MEDIA_TYPE.getCode());
        mv.addObject("message", ExceptionEnum.HTTP_UNSUPPORTED_MEDIA_TYPE.getMessage());
        return mv;
    }

    /**
     * 500 - 内部服务器错误
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e) {
        log.error("内部服务器错误", e);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("error/500");
        mv.addObject("code", ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getCode());
        mv.addObject("message", ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getMessage());
        return mv;
    }

    /**
     * CMSException
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(CmsException.class)
    public ModelAndView cmsException(CmsException e) {
        log.error("异常：", e);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("error/exception");
        mv.addObject("code", e.getCode());
        mv.addObject("message", e.getMessage());
        mv.addObject("reason", e.getReason());
        return mv;
    }

    // ---------------------------------------

//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ExceptionHandler(UnauthorizedException.class)
//    public Json unauthorized() {
//        return Json.unauthorized("需要登录");
//    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessForbiddenException.class)
    public Json accessForbidden() {
        //        log.error("Access Forbidden");
        return Json.accessForbidden("权限不足");
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler({ IllegalArgumentException.class, HttpMessageNotReadableException.class })
//    public Json badRequest(IllegalArgumentException illegalArgumentException) {
//        //        log.error("Bad request", illegalArgumentException);
//        return Json.badRequest(illegalArgumentException.getMessage());
//    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ NotFoundException.class })
    public Json notFound(NotFoundException exceededException) {
        return Json.notFound(exceededException.getMessage());
    }

    @ExceptionHandler({ BindException.class, MethodArgumentNotValidException.class })
    public Result validExceptionHandler(Exception e) {

        final BindingResult result;
        if (e instanceof MethodArgumentNotValidException) {
            result = ((MethodArgumentNotValidException) e).getBindingResult();
        }
        else if (e instanceof BindException) {
            result = (BindingResult) e;
        }
        else {
            return Json.failed();
        }

        final List<ObjectError> allErrors = result.getAllErrors();

        final Map<String, String> model = new HashMap<>(16);

        for (ObjectError error : allErrors) {
            if (error instanceof FieldError) {
                final String field = ((FieldError) error).getField();
                final String defaultMessage = error.getDefaultMessage();
                model.put(field, defaultMessage);

                log.warn("[{}] -> [{}]", field, defaultMessage);
            }
        }
        return Json.failed(model);
    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public Json error(Exception exception) {
//        log.error("An Exception occurred", exception);
//        return Json.failed(exception.getMessage());
//    }
//
//    @ExceptionHandler(ApplicationException.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public Json applicationException(ApplicationException exception) {
//        log.error("An exception occurred", exception);
//        return error(exception);
//    }
//
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(ApplicationRuntimeException.class)
//    public Json applicationRuntimeException(ApplicationRuntimeException exception) {
//
//        log.error("Runtime exception occurred", exception);
//        return error(exception);
//    }

}
