package com.jd;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @Auther lxy
 * @Date
 */
public class ProviderApplication {
    public static void main(String[] args) throws IOException {
        //启动spring容器
        new ClassPathXmlApplicationContext("spring-provider.xml");
        //线程阻塞
        System.in.read();
    }
}
