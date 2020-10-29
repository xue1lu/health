package com.jd.health.controller;
import com.alibaba.dubbo.config.annotation.Reference;

import com.jd.health.constant.MessageConstant;
import com.jd.health.entity.PageResult;
import com.jd.health.entity.Result;
import com.jd.health.pojo.QueryPageBean;
import com.jd.health.pojo.Setmeal;
import com.jd.health.service.SetmealService;
import com.jd.health.utils.QiNiuUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Auther lxy
 * @Date
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;
    private static final Logger log = LoggerFactory.getLogger(SetmealController.class);
    @Autowired
    private JedisPool jedisPool;
    //图片上传
    @PostMapping("/upload")
    public Result upload(MultipartFile imgFile) {
        //打印错误日志

        //获取文件名
        String originalFilename = imgFile.getOriginalFilename();
        //获取后缀名
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        //生成文件名
        String uniqueName = UUID.randomUUID().toString() + substring;
        //图片上传到七牛云
        try {
            QiNiuUtils.uploadViaByte(imgFile.getBytes(),uniqueName);
        } catch (IOException e) {
            log.error("上传图片失败", e);
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
        //返回前端数据map类型
        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("imgName", uniqueName);
        dataMap.put("domain", QiNiuUtils.DOMAIN);
        return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, dataMap);
    }

    //分页查询
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
       //调用service
        PageResult<Setmeal> pageResult=setmealService.findPage(queryPageBean);
        //响应
        return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS, pageResult);
    }


    //新增套餐
    @PostMapping("/add")
    public Result add(@RequestBody Setmeal setmeal,Integer[] checkgroupIds) {
        Integer setmealId = setmealService.add(setmeal, checkgroupIds);
        //获取jedis
        Jedis jedis = jedisPool.getResource();
        jedis.sadd("setmeal:static:html", setmealId + "|1|" + System.currentTimeMillis());
        //归还连接池
        jedis.close();

        //响应
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    //根据id查询套餐
    @PostMapping("/findById")
    public Result findById(int setmealId) {
        Setmeal setmeal=setmealService.findById(setmealId);
        //前端需要回显图片全路径
        //定义集合对返回数据进行封装,文字和图片分开
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("setmeal", setmeal);
        resultMap.put("imageUrl", QiNiuUtils.DOMAIN + setmeal.getImg());
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, resultMap);
    }

    //根据套餐id查询勾选关联的检查组ids
    @PostMapping("/findSetmealCheckGroupBySetmealId")
    public Result findSetmealCheckGroupBySetmealId(int setmealId) {
        List<Integer> checkgroupIds=setmealService.findSetmealCheckGroupBySetmealId(setmealId);
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkgroupIds);
    }

    //更新套餐
    @PostMapping("/update")
    public Result update(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        setmealService.update(setmeal, checkgroupIds);
        Jedis jedis = jedisPool.getResource();
        jedis.sadd("setmeal:static:html", setmeal.getId() + "|1|" + System.currentTimeMillis());
        jedis.close();
        return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);

    }

    //根据id删除套餐
    @PostMapping("/deleteById")
    public Result deleteById(int setmealId) {
        setmealService.deleteById(setmealId);
        //获得jedis
        Jedis jedis = jedisPool.getResource();
        jedis.sadd("setmeal:static:html", setmealId + "|0|" + System.currentTimeMillis());
        jedis.close();

        return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
    }


}
