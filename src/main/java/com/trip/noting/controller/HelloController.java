package com.trip.noting.controller;

import com.alibaba.fastjson2.JSON;
import com.trip.noting.configuration.Hello;
import org.springframework.ui.Model;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.Date;

@RestController
@Slf4j
public class HelloController {

    // SpringBean默认是单例模式
    // 多例方案一：bean增加注解@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)且依赖注入的使用方也添加注解才可以，使用不优雅
    // 多例方案二：对象工厂实时获取对象，每次创建的都是新的对象
    //     方式一：使用applicationContext每次从容器中重新获取的时候都是重新创建一个新的实例:applicationContext.getBean(Hello.class);
    //     方式二：使用BeanFactory工厂注入后实时获取：private BeanFactory factory; factory.getBean(Hello.class);
    //     方式三：使用ObjectFactory泛型的方式，使用更加简洁 ：ObjectFactory<Hello> helloBeanFactory;helloBeanFactory.getObject();
    //
    // 多例方案三：使用Lookup注解通过容器覆盖对应的方法，返回一个原型实例对象
    // 以上推荐使用方案二中的方式三

    // 使用
    @Autowired
    private Hello hello;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private BeanFactory factory;
    @Autowired
    private ObjectFactory<Hello> helloBeanFactory;
    @Autowired
    private RestTemplate restTemplate;

    public static class User {
        public String name;
        public Integer age;
        public Integer gender;
        public String date;
    }

    public static class UserRec {
        public String name;
        public Integer age;
        public Integer gender;
        public Calendar date;
    }

    @GetMapping("/hello")
    public String hello(User user) {
        System.out.println(JSON.toJSONString(user));
        String hello1 = hello.hello();
        System.out.println(hello1);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> httpEntity = new HttpEntity<>(new User() {{
            this.name = "123";
            this.age = 33;
        }}, headers);
        ResponseEntity<UserRec> userResponseEntity = restTemplate.postForEntity("http://127.0.0.1:8088/world", httpEntity, UserRec.class);
        System.out.println(JSON.toJSONString(userResponseEntity));
        return hello1;
    }

    @PostMapping("/world")
    public User world(@RequestBody User user) {
        System.out.println(JSON.toJSONString(user));
        System.out.println("===============");
        return new User() {{
            this.name = "resp";
            this.age = 22;
            this.date = "/Date(1716962646008+0800)/";
        }};
    }


}
