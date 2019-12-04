package com.ppcxy.common.exception.web;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.ppcxy.common.Constants;
import com.ppcxy.common.exception.web.entity.ExceptionResponse;
import com.ppcxy.common.utils.LogUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springside.modules.beanvalidator.BeanValidators;
import org.springside.modules.utils.Exceptions;
import org.springside.modules.utils.text.JsonMapper;
import org.springside.modules.web.MediaTypes;

import javax.validation.ConstraintViolationException;
import java.util.Map;

/**
 * @version 1.0
 */
@ControllerAdvice
public class DefaultExceptionHandler {
    
    /**
     * 没有权限 异常
     * <p/>
     * 后续根据不同的需求定制即可
     */
    @ExceptionHandler({UnauthorizedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ModelAndView processUnauthenticatedException(NativeWebRequest request, UnauthorizedException e) {
        LogUtils.logError("用户权限验证失败", e);
        ExceptionResponse exceptionResponse = ExceptionResponse.from(e);
        
        ModelAndView mv = new ModelAndView();
        mv.addObject(Constants.ERROR, exceptionResponse);
        mv.setViewName("error/exception");
        
        return mv;
    }
    
    /**
     * 没有权限 异常
     * <p/>
     * 后续根据不同的需求定制即可
     */
    @ExceptionHandler({TypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleTypeMismatchException(NativeWebRequest request, TypeMismatchException e) {
        LogUtils.logError("参数绑定异常", e);
        ExceptionResponse exceptionResponse = ExceptionResponse.from("参数绑定错误.", e);
        
        ModelAndView mv = new ModelAndView();
        mv.addObject(Constants.ERROR, exceptionResponse);
        mv.setViewName("error/exception");
        
        return mv;
    }
    
    
    /**
     * 处理RestException.
     */
    @ExceptionHandler(value = {RestException.class})
    public final ResponseEntity<?> handleException(RestException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(MediaTypes.TEXT_PLAIN_UTF_8));
        
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(ex.status)) {
            request.setAttribute("javax.servlet.error.exception", ex, 0);
        }
        
        return new ResponseEntity(ex.getMessage(), headers, ex.status);
    }
    
    /**
     * 处理JSR311 Validation异常.
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    public final ResponseEntity<?> handleException(ConstraintViolationException ex, WebRequest request) {
        Map<String, String> errors = BeanValidators.extractPropertyAndMessage(ex.getConstraintViolations());
        String body = JsonMapper.nonEmptyMapper().toJson(errors);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(MediaTypes.TEXT_PLAIN_UTF_8));
        
        return new ResponseEntity(body, headers, HttpStatus.BAD_REQUEST);
    }
    
    
    /**
     * 处理Hystrix Runtime异常, 分为两类:
     * 一类是Command内部抛出异常(返回500).
     * 一类是Hystrix已进入保护状态(返回503).
     */
    @ExceptionHandler(value = {HystrixRuntimeException.class})
    public final ResponseEntity<?> handleException(HystrixRuntimeException ex, WebRequest request) {
        HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
        String message = ex.getMessage();
        
        HystrixRuntimeException.FailureType type = ex.getFailureType();
        
        // 对命令抛出的异常进行特殊处理
        if (type.equals(HystrixRuntimeException.FailureType.COMMAND_EXCEPTION)) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = Exceptions.getErrorMessageWithNestedException(ex);
        }
        
        LogUtils.logError(message, ex);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(MediaTypes.TEXT_PLAIN_UTF_8));
        
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute("javax.servlet.error.exception", ex, 0);
        }
        
        return new ResponseEntity(ex.getMessage(), headers, status);
    }
    
    /**
     * 处理Hystrix ClientException异常(返回404).
     * ClientException表明是客户端请求参数本身的问题, 不计入异常次数统计。
     */
    @ExceptionHandler(value = {HystrixBadRequestException.class})
    public final ResponseEntity<?> handleException(HystrixBadRequestException ex, WebRequest request) {
        String message = Exceptions.getErrorMessageWithNestedException(ex);
        LogUtils.logError(message, ex);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(MediaTypes.TEXT_PLAIN_UTF_8));
        
        return new ResponseEntity(ex.getMessage(), headers, HttpStatus.BAD_REQUEST);
        
    }
}
