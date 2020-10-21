package com.jd;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

/**
 * @Auther lxy
 * @Date
 */
//创建测试类，设置zookeeper上的数据库配置信息
public class CreateJDBCPath {
    //创建节点注入数据库信息
    @Test
    public void CreateJDBCPath() throws Exception {
        //创建重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 1);
        //创建客户端
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", 3000, 3000, retryPolicy);
        //开启客户端
        client.start();
        //执行操作
        client.create().creatingParentsIfNeeded().forPath("/config/jdbc.driver","com.mysql.jdbc.Driver".getBytes());
        client.create().creatingParentsIfNeeded().forPath("/config/jdbc.url","jdbc:mysql://localhost:3306/jddubbo".getBytes());
        client.create().creatingParentsIfNeeded().forPath("/config/jdbc.user","root".getBytes());
        client.create().creatingParentsIfNeeded().forPath("/config/jdbc.password","root".getBytes());
        //关闭客户端
        client.close();
    }
    //修改节点,不重启服务情况下修改数据库
    @Test
    public void UpdateJDBCPath() throws Exception {
        //创建重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 1);
        //创建客户端
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", 3000, 3000, retryPolicy);
        //开启客户端
        client.start();
        //执行操作
        client.setData().forPath("/config/jdbc.url", "jdbc:mysql://localhost:3306/itcastdubbo".getBytes());
        //关闭客户端
        client.close();
    }
}
