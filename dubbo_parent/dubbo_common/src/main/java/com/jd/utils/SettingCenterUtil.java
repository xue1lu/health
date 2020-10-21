package com.jd.utils;


import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;


import java.util.Properties;

/**
 * @Auther lxy
 * @Date
 */
//编写载入zookeeper中配置文件，传递到Properties属性中
public class SettingCenterUtil extends PropertyPlaceholderConfigurer implements ApplicationContextAware {





    //获取zookeeper中数据库的配置
    private void loadZookeeper(Properties props) {
        //创建重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 1);
        //创建客户端
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", 1000, 1000, retryPolicy);
        //开启客户端
        client.start();
        //执行获取数据操作
        try {
            String driver = new String(client.getData().forPath("/config/jdbc.driver"));
            String url = new String(client.getData().forPath("/config/jdbc.url"));
            String user = new String(client.getData().forPath("/config/jdbc.user"));
            String password = new String(client.getData().forPath("/config/jdbc.password"));

            //设置数据库配置
            props.setProperty("jdbc.driver", driver);
            props.setProperty("jdbc.url", url);
            props.setProperty("jdbc.user", user);
            props.setProperty("jdbc.password", password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //重写processProperties方法
    /**
     * 处理properties内容,相当于此标签
     *  <context:property-placeholder location="classpath:jdbc.properties"></context:property-placeholder>
     * @param beanFactoryToProcess
     * @param props
     * @throws BeansException
     */
    /**
     * 标识是否需要监听
     */
    private static boolean flag = true;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        //设置数据库的配置,读取zookeeper中数据库数据
        loadZookeeper(props);
        if (flag) {
            //表示已经订阅,执行监听就行
            addWatch();
            flag = false;
        }
        super.processProperties(beanFactoryToProcess, props);

    }

    //添加监听机制
    private void addWatch() {
        //创建重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3, 3000);
        //创建客户端
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", 3000, 3000, retryPolicy);
        //开启客户端
        client.start();
        //创建监听对象
        TreeCache treeCache = new TreeCache(client, "/config");
        //开启缓存
        try {
            treeCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //添加监听对象
        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                if (event.getType() == TreeCacheEvent.Type.NODE_UPDATED) {
                    String path = event.getData().getPath();
                    System.out.println("执行了修改的路径" + path);
                    if (path.startsWith("/config/jdbc")) {
                        //修改了数据库配置
                        //刷新spring容器,重新加载数据库配置
                        applicationContext.refresh();
                    }
                }

            }

        });

    }

    private AbstractApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (AbstractApplicationContext) applicationContext;
    }
}
