package com.jd.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jd.health.constant.MessageConstant;
import com.jd.health.constant.RedisMessageConstant;
import com.jd.health.entity.Result;
import com.jd.health.pojo.CheckGroup;
import com.jd.health.pojo.CheckItem;
import com.jd.health.pojo.Order;
import com.jd.health.pojo.Setmeal;
import com.jd.health.service.OrderService;
import com.jd.health.service.SetmealService;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
@RestController
@RequestMapping("/order")
public class OrderMobileController {

    //注入jedis
    @Autowired
    private JedisPool jedisPool;
    //订阅预约查询服务
    @Reference
    private OrderService orderService;
    @Reference
    private SetmealService setmealService;

    @PostMapping("/submit")
    public Result submit(@RequestBody Map<String, String> paraMap) {
        //获取jedis
        Jedis jedis = jedisPool.getResource();
        //获取手机号
        String telephone = paraMap.get("telephone");
        //验证码判断
        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone;
        String codeInRedis = jedis.get(key);
        if (StringUtils.isEmpty(codeInRedis)) {
            //没有验证码,重新发送
            return new Result(false, MessageConstant.RSEND_VALIDATECODE);
        }
        //如果有验证码,比较
        //获取前端传的验证码
        String validateCode = paraMap.get("validateCode");
        if (!codeInRedis.equals(validateCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //走到这里表示验证码通过,移除生成的验证码
       jedis.del(key);//测试时可不执行
        //设置预约类型
        paraMap.put("orderType", Order.ORDERTYPE_WEIXIN);
        //调用业务层
        Order order=orderService.submitOrder(paraMap);
        return new Result(true, MessageConstant.ORDER_SUCCESS, order);

    }

    //根据预约成功的id查询预约信息
    @PostMapping("/findById")
    public Result findById(@RequestParam("id") int orderId) {
       Map<String, Object> orderInfo= orderService.findOrderDetailById(orderId);
        return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, orderInfo);
    }

    //导出体检预约成功回执单
    @RequestMapping("/exportSetmealInfo")
    public void exportSetmealInfo(Integer id, HttpServletRequest request, HttpServletResponse response) {
        //根据订单id查询订单信息,包含套餐id
        Map<String, Object> orderInfo = orderService.findOrderDetailById(id);
        //获取套餐的id
        Integer setmeald = (Integer) orderInfo.get("setmeal_id");
        //查询套餐详情
        Setmeal setmealDetail = setmealService.findDetailById3(setmeald);
        //创建pdf文档
        Document document = new Document();
        //设置响应头
        response.setHeader("Content-Disposition","attachment;filename=orderInfo.pdf");
        //设置响应格式
        response.setContentType("application/pdf");
        //pdf关联响应流
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            //打开pdf
            document.open();
            //处理中文字体
            Font font = new Font(BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED));

            //填充数据
            document.add(new Paragraph("预约信息:", font));
            document.add(new Paragraph("体检人：" + orderInfo.get("member"),font));
            document.add(new Paragraph("体检套餐：" + orderInfo.get("setmeal"),font));
            document.add(new Paragraph("体检日期：" + orderInfo.get("orderDate"),font));
            document.add(new Paragraph("预约类型：" + orderInfo.get("orderType"),font));
            document.add(new Paragraph(""));
            document.add(new Paragraph("套餐信息:",font));

            //创建3列表格
            Table table = new Table(3);
            //设置表格样式
            table.setWidth(80);//宽度
            table.setBorder(1);//边框
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER); //水平对齐方式
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP); // 垂直对齐方式
            //设置表格属性
            table.setBorderColor(new Color(0, 0, 255));//表格边框颜色
            table.setPadding(5);//设置间距
            table.setAlignment(Element.ALIGN_CENTER);//设置字体居中
            table.addCell(buildCell("项目名称",font));
            table.addCell(buildCell("项目内容",font));
            table.addCell(buildCell("项目解读",font));
            //检查组数据
            List<CheckGroup> checkGroups = setmealDetail.getCheckGroups();
            if (checkGroups != null) {
                for (CheckGroup checkGroup : checkGroups) {
                    //检查组名称
                    table.addCell(buildCell(checkGroup.getName(), font));
                    //检查项名称拼接
                    StringBuilder sb = new StringBuilder();
                    //检查项数据
                    List<CheckItem> checkItems = checkGroup.getCheckItems();
                    if (checkItems!=null) {
                        for (CheckItem checkItem : checkItems) {
                            //检查项名称
                            sb.append(checkItem.getName()).append(" ");
                        }
                        //去除最后空格
                        sb.setLength(sb.length() - 1);
                    }
                    table.addCell(buildCell(sb.toString(), font));
                    //解读
                    table.addCell(buildCell(checkGroup.getRemark(), font));
                }
            }
            document.add(table);
            //关闭文档
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 传递内容和字体样式，生成单元格
    private Cell buildCell(String content, Font font)throws BadElementException {
        Phrase phrase = new Phrase(content, font);
        return new Cell(phrase);
    }
}
