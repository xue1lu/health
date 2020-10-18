package com.jd.controller;

import com.jd.pojo.Items;
import com.jd.service.ItemsService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @Auther lxy
 * @Date
 */
//@RestController
@Controller
@RequestMapping("/items")
public class ItemsController {
    //调用业务层
    @Autowired
    private ItemsService itemsService;

    //查询方法
    @GetMapping("/findAll")
    public String findAll(Model model) {
        System.out.println("控制层调用查询方法....");
        List<Items> items = itemsService.findAll();
        //存入回显
        model.addAttribute("items", items);
        //返回
        return "items";

    }
    //查询方法
//    @GetMapping("/findAll")
   /* @RequestMapping("/findAll")
    public ModelAndView findAll(ModelAndView modelAndView) {
        System.out.println("控制层调用查询方法....");
        List<Items> items = itemsService.findAll();
        //存入回显
        modelAndView.addObject("items", items);
        //返回
        modelAndView.setViewName("items");
        return modelAndView;

    }*/


    //新增
//    @RequestMapping("/save")
    @PostMapping("/save")
    public String save(Items items) {
        System.out.println("控制层调用插入方法....");
        itemsService.save(items);
        return "redirect:/items/findAll";
    }



}
