package com.itheima.utils;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;

import java.util.Properties;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2020/10/21
 */
public class SettingCenterUtil extends PropertyPlaceholderConfigurer implements ApplicationContextAware {

    private static boolean flag = true; // 需要添加监听

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        // 加载zookeeper上的数据库配置信息
        loadZk(props);
        if(SettingCenterUtil.flag) {
            addWatch();// 对zookeeper数据库配置信息监听
        }
        // spring后续加载过程不能断，要保留调用super的方法
        super.processProperties(beanFactoryToProcess, props);
    }

    private void addWatch(){
        String connectString = "127.0.0.1:2181";
        int sessionTimeoutMs = 1000;
        int connectionTimeoutMs = 1000;
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3,1000);
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
        // 启动客户端
        client.start();

        TreeCache treeCache = new TreeCache(client,"/config");
        try {
            treeCache.start();
            treeCache.getListenable().addListener((cli,event)->{
                // 只要修改了数据库配置的，才需要刷新容器
                if(event.getType() == TreeCacheEvent.Type.NODE_UPDATED){
                    // 更新的路径
                    String path = event.getData().getPath();
                    if(path.startsWith("/config/jdbc.")){
                        // 刷新容器
                        this.applicationContext.refresh();

                    }
                }
            });
            SettingCenterUtil.flag = false;// 已经添加过了,不再添加监听
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 读取zookeeper的节点数据
     * @param pros
     */
    private void loadZk(Properties pros){
        // 创建客户端
        String connectString = "127.0.0.1:2181";
        int sessionTimeoutMs = 1000;
        int connectionTimeoutMs = 1000;
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3,1000);
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);

        client.start();

        try {
//          //<context:property-placeholder location="classpath:jdbc.properties"/>
            String driver = new String(client.getData().forPath("/config/jdbc.driver"));
            String url = new String(client.getData().forPath("/config/jdbc.url"));
            String user = new String(client.getData().forPath("/config/jdbc.user"));
            String password = new String(client.getData().forPath("/config/jdbc.password"));

            // 设置到spring容器中的配置信息里
            // key必须与 dataSource value的表达式一致
            pros.setProperty("jdbc.driver",driver);
            pros.setProperty("jdbc.url",url);
            pros.setProperty("jdbc.user",user);
            pros.setProperty("jdbc.password",password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AbstractApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (AbstractApplicationContext)applicationContext;
    }
}
