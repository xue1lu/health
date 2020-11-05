package com.jd.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jd.health.constant.MessageConstant;
import com.jd.health.entity.Result;
import com.jd.health.service.MemberService;
import com.jd.health.service.ReportService;
import com.jd.health.service.SetmealService;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiContext;
import org.jxls.util.JxlsHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther lxy
 * @Date
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;
    @Reference
    private SetmealService setmealService;
    @Reference
    private ReportService reportService;

    @GetMapping("/getMemberReport")
    public Result getMemberReport() {
        //封装月份数据
        List<String> months = new ArrayList<String>();
        //获取当前日历
        Calendar calendar = Calendar.getInstance();
        //得到过去一年日历
        calendar.add(Calendar.YEAR, -1);
        //定义日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        //改变月份得到真实月份+1
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            //获得过去的日期
            Date date = calendar.getTime();
            //解析日期
            String format = sdf.format(date);
            //添加到数组中
            months.add(format);
        }
        //查询过去12个月会员数据
        List<Integer> memberCount=memberService.getMemberReport(months);
        //定义封装数据的模型
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("months", months);
        dataMap.put("memberCount", memberCount);
        return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, dataMap);
    }

    @GetMapping("/getSetmealReport")
    public Result getSetmealReport() {
        //定义模型接收查询套餐数量
        List<Map<String, Object>> setmealCount = setmealService.findSetmealCount();
        //定义模型接收查询套餐名称
        List<String> setmealNames= new ArrayList<String>();
        if (setmealCount != null && setmealCount.size() > 0) {
            for (Map<String, Object> map : setmealCount) {
                setmealNames.add(((String) map.get("name")));
            }
        }
        //封装查询结果
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("setmealNames", setmealNames);
        dataMap.put("setmealCount", setmealCount);
        //返回结果
        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, dataMap);
    }

    //运营统计数据
    @GetMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        Map<String, Object> dataMap = reportService.getBusinessReportData();
        return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, dataMap);

    }
    //导出运营数据报表
    @GetMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest req, HttpServletResponse response) {
        //获得报表数据
        Map<String, Object> reportData = reportService.getBusinessReportData();
        //获得模板路径
        String path = req.getSession().getServletContext().getRealPath("/template/report_template.xlsx");
        //创建工作簿
        try (XSSFWorkbook workbook = new XSSFWorkbook(path);
             //获得输出流
             ServletOutputStream outputStream = response.getOutputStream();
        ) {
            //获得工作表
            XSSFSheet sheetAt = workbook.getSheetAt(0);
            //填充数据
            sheetAt.getRow(2).getCell(5).setCellValue((String) reportData.get("reportDate"));//日期
            //会员数据
            sheetAt.getRow(4).getCell(5).setCellValue((int)reportData.get("todayNewMember"));
            sheetAt.getRow(4).getCell(7).setCellValue((int)reportData.get("totalMember"));
            sheetAt.getRow(5).getCell(5).setCellValue((int)reportData.get("thisWeekNewMember"));
            sheetAt.getRow(5).getCell(7).setCellValue((int)reportData.get("thisMonthNewMember"));

            //预约数据
            sheetAt.getRow(7).getCell(5).setCellValue((int) reportData.get("todayOrderNumber"));
            sheetAt.getRow(7).getCell(7).setCellValue((int) reportData.get("todayVisitsNumber"));
            sheetAt.getRow(8).getCell(5).setCellValue((int) reportData.get("thisWeekOrderNumber"));
            sheetAt.getRow(8).getCell(7).setCellValue((int) reportData.get("thisWeekVisitsNumber"));
            sheetAt.getRow(9).getCell(5).setCellValue((int) reportData.get("thisMonthOrderNumber"));
            sheetAt.getRow(9).getCell(7).setCellValue((int) reportData.get("thisMonthVisitsNumber"));
            //热门套餐数据
            List<Map<String, Object>> hotSetmeal = (List<Map<String, Object>>) reportData.get("hotSetmeal");
            //遍历
            int rowIndex = 12;
            if (hotSetmeal != null && hotSetmeal.size() > 0) {
                for (Map<String, Object> setmeal : hotSetmeal) {
                    //获取行
                   Row row = sheetAt.getRow(rowIndex);
                    row.getCell(4).setCellValue((String) setmeal.get("name"));
                    row.getCell(5).setCellValue((Long) setmeal.get("setmeal_count"));
                    BigDecimal proportion = (BigDecimal) setmeal.get("proportion");
                    row.getCell(6).setCellValue(proportion.doubleValue());
                    row.getCell(7).setCellValue((String) setmeal.get("remark"));
                    rowIndex++;
                }
            }
            //设置响应体格式为excel
            response.setContentType("application/vnd.ms-excel");
            //设置文件输出名称
            String filename = "运营数据统计.xlsx";
            //输出格式
            filename = new String(filename.getBytes(), "ISO-8859-1");
            //设置响应头信息
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            //输出
            workbook.write(outputStream);
            //关闭流
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //
    @GetMapping("/exportBusinessReport2")
    public void exportBusinessReport2(HttpServletRequest req, HttpServletResponse res){
        //获取数据
        Map<String, Object> reportData = reportService.getBusinessReportData();
        //获得模板路径
        String template = req.getSession().getServletContext().getRealPath("/template/report_template2.xlsx");
        //获得工作簿模板

        try (XSSFWorkbook workbook = new XSSFWorkbook(template);
             //获得输出流
             ServletOutputStream outputStream = res.getOutputStream();
        ) {

            //创建数据模型对象
            Context context = new PoiContext();
            context.putVar("obj", reportData);     // 设置内容体格式excel,
            res.setContentType("application/vnd.ms-excel");
            // 设置输出流的头信息，告诉浏览器，这是文件下载
            String filename = "运营数据统计.xlsx";
            filename = new String(filename.getBytes(),"ISO-8859-1");
            res.setHeader("Content-Disposition","attachment;filename=" + filename);
            // 把工作簿输出到输出流
            // 把数据模型中的数据填充到文件中
            JxlsHelper.getInstance().processTemplate(new FileInputStream(template),res.getOutputStream(),context);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //生成pdf
    @GetMapping("/exportBusinessReportPDF")
    public void exportBusinessReportPDF(HttpServletRequest req, HttpServletResponse res){
        // 查询统计数据
        Map<String, Object> reportData = reportService.getBusinessReportData();
        // 获取模板路径
        String template = req.getSession().getServletContext().getRealPath("/template");
        String jrxml = template + "/report_business.jrxml";
        // 编译后的路径
        String jasper = template + "/report_business.jasper";
        // 编译模板
        try {
            JasperCompileManager.compileReportToFile(jrxml,jasper);
            // 构建数据
            reportData.put("company","ABC健康");
            // parameters reportData
            // 热门套餐
            List<Map<String, Object>> hotSetmeal = (List<Map<String, Object>>) reportData.get("hotSetmeal");
            // 填充数据
            JasperPrint print = JasperFillManager.fillReport(jasper, reportData, new JRBeanCollectionDataSource(hotSetmeal));
            // 设置响应的内容体格式
            res.setContentType("application/pdf");
            //定义输出文件名字
            String filename = "运营数据统计.pdf";
            filename = new String(filename.getBytes(),"ISO-8859-1");
            // 响应头信息 附件下载
            res.setHeader("Content-Disposition","attachment;filename="+filename);
            // 导出pdf
            JasperExportManager.exportReportToPdfStream(print, res.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //获得指定时间段会员数量
    @PostMapping("/getMemberReport2")
    public Result getMemberReport2(String value1, String value2) throws Exception {
        List<String> months = new ArrayList<>();
        //定义日历格式
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
        Date date1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(value1);
        Date date2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(value2);
        Calendar calendar = Calendar.getInstance();
        //设置起始时间
        calendar.setTime(date1);
        while (calendar.getTime().before(date2)) {
            String month = sdf.format(calendar.getTime());
            months.add(month);
            calendar.add(Calendar.MONTH,1);
        }

        List<Integer> memberCount=memberService.getMemberReport(months);
        //定义封装数据的模型
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("months", months);
        dataMap.put("memberCount", memberCount);
        return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, dataMap);
    }

    //会员性别占比
    @GetMapping("/getMemberReportByGender")
    public Result getMemberReportByGender() {
        //定义模型接收查询数据
        List<Map<String, Object>> memberCount = new ArrayList<>();
        //定义模型接收查询性别
        List<String>  genderNames= new ArrayList<>();
        if (memberCount != null) {
            for (Map<String, Object> map : memberCount) {
                if ("1".equals(map.get("name"))) {
                    map.put("name", "男");
                } else if ("0".equals(map.get("name"))){
                    map.put("name", "女");
                }
                genderNames.add((String) map.get("name"));
            }
        }
        //封装查询结果
        Map<String, Object> dataMap= new HashMap<>();
        dataMap.put("genderNames", genderNames);
        dataMap.put("memberCount", memberCount);
        //响应
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, dataMap);
    }
}
