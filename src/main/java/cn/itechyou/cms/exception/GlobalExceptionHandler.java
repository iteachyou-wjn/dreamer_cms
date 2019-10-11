package cn.itechyou.cms.exception;

import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import cn.itechyou.cms.common.ExceptionEnum;

/**
 * 全局异常处理
 * 
 * @author jun hu
 * 
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
     * 400 - 错误的请求
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ModelAndView handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.error("错误的请求", e);
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
        logger.error("禁止访问", e);
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
        logger.error("资源没有找到", e);
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
        logger.error("没有权限！", e);
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
        logger.error("不支持的媒体类型", e);
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
    public ModelAndView handleUnauthorizedException(Exception e) {
        logger.error("内部服务器错误", e);
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
        logger.error("异常：", e);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("error/exception");
        mv.addObject("code", e.getCode());
        mv.addObject("message", e.getMessage());
        mv.addObject("reason", e.getReason());
        return mv;
    }
}