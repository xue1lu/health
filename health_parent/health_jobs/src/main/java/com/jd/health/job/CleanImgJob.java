package com.jd.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jd.health.service.SetmealService;
import com.jd.health.utils.QiNiuUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
@Component("cleanImgJob")
public class CleanImgJob {
    //订阅服务
    @Reference
    private SetmealService setmealService;

    public void cleanImg() {
        //查出七牛云上所有图片
        List<String> imgIn7Niu = QiNiuUtils.listFile();
        //差出数据库中所有图片
        List<String> imgInDb = setmealService.findImgs();
        //七牛云减去数据库就是差值垃圾图片
        imgIn7Niu.removeAll(imgIn7Niu);
        //把剩下图片名转成数组格式
        String[] strings = imgIn7Niu.toArray(new String[]{});
        //删除七牛云图片
        QiNiuUtils.removeFiles(strings);

    }
}
