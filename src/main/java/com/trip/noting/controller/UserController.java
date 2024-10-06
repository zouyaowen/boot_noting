package com.trip.noting.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping
public class UserController {

    @RequestMapping("/hello_")
    public String hello(Model model) {
        // 1.纯文本形式的参数
        model.addAttribute("name", "freemarker");// 参数1属性名称(在页面中使用),参数2模板数据
        // 2.实体类相关的参数
        Student student = new Student() {{
            setName("小舞");
            setAge(18);
        }};
        System.out.println(student);
        model.addAttribute("stu", student);
        return "helloworld"; // 模板文件名称
    }

    @RequestMapping("/ftl")
    public ModelAndView ftl(Model model) {
        ModelAndView modelAndView = new ModelAndView("index");
        // 添加 title 属性到 Model
        System.out.println("------------ftl----------");
        modelAndView.addObject("title", "Freemarker");
        return modelAndView;
    }
}
