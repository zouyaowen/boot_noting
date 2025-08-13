package com.trip.noting.controller;

import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


@RestController
public class LuaController {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/setKey")
    public void setKey() {
        System.out.println("----setKey-----");
        Random random = new Random();
        int randomV = random.nextInt(200);
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("world", randomV);
    }

    @GetMapping("/setKeyWithLua")
    public void setKeyWithLua() {
        System.out.println("-----setKeyWithLua----");
        Resource scriptSource = new ClassPathResource("lua/setKey.lua");
        RedisScript<Object> setScript = RedisScript.of(scriptSource, Object.class);
        // 通常情况下，没有KEYS部分
        List<String> keys = new ArrayList<>();
        keys.add("setKeyWithLuaKey");
        Random random = new Random();
        int randomV = random.nextInt(200);
        // 传递给Lua脚本的参数
        Object[] args = new Object[]{"setKeyWithLuaValue-" + randomV};
        Object setRes = redisTemplate.execute(setScript, keys, args);
        System.out.println("setRes:" + JSON.toJSONString(setRes));
    }

    @GetMapping("/mSetKeyWithLua")
    public void mSetKeyWithLua() {
        System.out.println("-----setKeyWithLua----");
        Resource scriptSource = new ClassPathResource("lua/mSetKey.lua");
        RedisScript<Object> setScript = RedisScript.of(scriptSource, Object.class);
        // 通常情况下，没有KEYS部分
        List<String> keys = new ArrayList<>();
        keys.add("lua1");
        keys.add("lua2");
        Random random = new Random();
        int randomV = random.nextInt(200);
        // 传递给Lua脚本的参数
        Object[] args = new Object[]{"lua1-v", "lua2-v"};
        Object setRes = redisTemplate.execute(setScript, keys, args);
        System.out.println("setRes:" + JSON.toJSONString(setRes));
    }

    @GetMapping("/sumWithLua")
    public void sumWithLua() {
        System.out.println("-----sumWithLua----");
        Resource scriptSource = new ClassPathResource("lua/sum.lua");
        RedisScript<Long> sumScript = RedisScript.of(scriptSource, Long.class);
        // 通常情况下，没有KEYS部分
        List<String> keys = new ArrayList<>();
        // 传递给Lua脚本的参数
        Object[] args = new Object[]{10, 20};
        Long sunRes = redisTemplate.execute(sumScript, keys, args);
        System.out.println("sunRes:" + sunRes);
    }


    // public UserEntity execLua() {
    //     Resource resource = resourceLoader.getResource("classpath:myscript.lua");
    //     String luaScript;
    //     try {
    //         luaScript = new String(resource.getInputStream().readAllBytes());
    //     } catch (Exception e) {
    //         throw new RuntimeException("Unable to read Lua script file.");
    //     }
    //
    // }
}
