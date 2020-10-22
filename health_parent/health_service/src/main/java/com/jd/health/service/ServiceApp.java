package com.jd.health.service;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @Auther lxy
 * @Date
 */
public class ServiceApp {
    public static void main(String[] args) throws IOException {
        new ClassPathXmlApplicationContext("applicationContext-service.xml");
        System.in.read();
    }
}
