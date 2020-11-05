package com.jd.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jd.health.pojo.Setmeal;
import com.jd.health.service.SetmealService;
import com.jd.health.utils.QiNiuUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther lxy
 * @Date
 */
//生成静态页面
@Component
public class GenerateHtmlJob {
    private static final Logger log = LoggerFactory.getLogger(GenerateHtmlJob.class);
    @Autowired
    private Configuration configuration;
    @Autowired
    private JedisPool jedisPool;
    @Value("${out_put_path}")
    private String out_put_path;
    @Reference
    private SetmealService setmealService;

    //freemarker使用配置
    @PostConstruct//等同于.xml文件中的init-method
    public void init() {
        //获得模板路径
        configuration.setClassForTemplateLoading(this.getClass(), "/ftl");
        //设置编码
        configuration.setDefaultEncoding("utf-8");
    }

    //执行任务策略
    @Scheduled(initialDelay = 3000, fixedDelay = 30000)
    public void generateHtml() {
        log.info("任务策略执行了");
        //获取要生成的套餐的id
        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";
        //从redis中获取需要处理的套餐集合
        Set<String> stringSet = jedis.smembers(key);
        // id|操作符|时间戳
        log.debug("stringSet:{}", stringSet == null ? 0 : stringSet.size());
        //遍历set集合
        if (stringSet != null && stringSet.size() > 0) {
            for (String str : stringSet) {
                log.debug("str:{}", str);
                //截取字符串
                String[] split = str.split("\\|");
                String setmealId = split[0];
                String operation = split[1];

                if ("1".equals(operation)) {
                    //生成套餐详情页面
                    log.info("生成套餐详情页面");
                    try {
                        generateSetmealDetail(setmealId);
                        log.info("生成套餐详细页面成功,setmealId:{}",setmealId);
                    } catch (Exception e) {
                        log.error("生成套餐详细页面失败", e);
                    }
                } else if ("0".equals(operation)) {
                    //删除套餐详情页面
                    log.info("删除套餐详情页面,setmealId:{}",setmealId);
                    removeFile(setmealId);


                }
                //删除对应的redis任务
                jedis.srem(key, str);
            }
            //生产套餐列表页面
            log.info("生成套餐列表页面");
            try {
                generateSetmealList();
                log.info("生成套餐列表页面成功");
            } catch (Exception e) {
                log.error("生成套餐列表失败", e);
            }
        }
        //关闭redis
        jedis.close();

    }

    //生成套餐列表页面
    private void generateSetmealList() throws Exception {
        //获得模板
        //  Template template = configuration.getTemplate("mobile_setmeal.ftl");
        //构建模板填充数据
        Map<String, Object> dataMap = new HashMap<String, Object>();
        //查询所有套餐信息
        List<Setmeal> setmealList = setmealService.findAll();
        //设置图片路径
        setmealList.forEach(setmeal -> setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg()));
        //放到数据模板中
        dataMap.put("setmealList", setmealList);
        //定义writer保存到指定路径下
        String filename = out_put_path + "/mobile_setmeal.html";
        // BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));
        //调用模板的process方法
        //template.process(dataMap, writer);
        //关闭流
        //writer.flush();
        // writer.close();
        doGenerate("mobile_setmeal.ftl", dataMap, filename);

    }

    //
    //生成套餐详情页面
    private void generateSetmealDetail(String setmealId) throws Exception {
        //获得数据填充模板
        Map<String, Object> dataMap = new HashMap<String, Object>();
        //查询详细套餐信息
        Setmeal setmeal = setmealService.findDetailById3(Integer.valueOf(setmealId));
        //补全路径
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        //数据放入模板
        dataMap.put("setmeal", setmeal);
        //设置保存路径
        String filename = out_put_path + "/setmeal_" + setmealId + ".html";
        //生成页面
        doGenerate("mobile_setmeal_detail.ftl", dataMap, filename);


    }

    //删除套餐详情文件
    private void removeFile(String setmealId) {
        //指定路径
        String filename = out_put_path + "/setmeal" + setmealId + ".html";
        //创建file对象
        File file = new File(filename);
        if (file.exists()) {
            file.delete();
        }

    }

    //抽取公共 的生成页面方法
    private void doGenerate(String templateName, Map<String, Object> dataMap, String filename) throws Exception {
        //获得模板
        Template template = configuration.getTemplate(templateName);
        //定义输出流writer保存到指定路径下
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));
        //向模板中填充数据
        template.process(dataMap, writer);
        //关闭流
        writer.flush();
        writer.close();

    }
}
