package com.jd.health.controller;



import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.jd.health.entity.Result;
import com.jd.health.exception.HealthException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 * 全局异常处理通知
 * ExceptionHandler获取的异常,范围从小到大,返回的是json数据
 *
 */
@RestControllerAdvice
public class HealExceptionAdvice {
    /**
     * info:打印日志
     * debug:记录重要数据,id,userid
     * error:异常错误
     */
    private static final Logger log = LoggerFactory.getLogger(HealExceptionAdvice.class);

    /**
     * 自定义抛出异常处理
     */
    @ExceptionHandler(HealthException.class)
    public Result handleHealthException(HealthException heal) {
        //返回异常信息
        return new Result(false, heal.getMessage());
    }

    /**
     * 未知异常的处理
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception ex) {
        log.error("发生异常", ex);
        return new Result(false, "发生未知错误异常,请联系管理员");
    }
    private Result handleUserPassword(){
        return new Result(false, "用户名或密码错误");
    }
    /**
     * 密码错误
     */
    @ExceptionHandler(BadCredentialsException.class)
    public Result handleBadCredentialsException(BadCredentialsException bce) {
        return handleUserPassword();
    }

    /**
     * 用户名不存在
     */
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public Result handleInternalAuthenticationServiceException(InternalAuthenticationServiceException iase) {
        return handleUserPassword();
    }
    /**
     * 权限不足
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result handleAccessDeniedException(AccessDeniedException ade) {
        log.error("权限不足",ade);
        return new Result(false, "权限不足,请联系管理员");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException mane) {
        //获取异常结果
        BindingResult bindingResult = mane.getBindingResult();
        //获取属性校验的错误
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuilder sb= new StringBuilder();
        if (null != fieldErrors) {
            for (FieldError fieldError : fieldErrors) {
                //获得属性名
                String fieldName = fieldError.getField();
                //获得校验未通过的提示信息
                String defaultMessage = fieldError.getDefaultMessage();
                sb.append(defaultMessage).append("; ");
            }
            if (sb.length() > 0) {
                sb.setLength(sb.length()-2);
            }
        }
        return new Result(false, sb.toString());
    }

}
