package com.jd.health.exception;

/**
 * @Auther lxy
 * @Date
 */
public class HealthException extends RuntimeException{
    /*自定义异常区分系统异常与业务异常*/

    public HealthException(String message) {
        super(message);
    }
}
