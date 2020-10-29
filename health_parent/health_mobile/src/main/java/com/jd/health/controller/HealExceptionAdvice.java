package com.jd.health.controller;



import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.jd.health.entity.Result;
import com.jd.health.exception.HealthException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

}
