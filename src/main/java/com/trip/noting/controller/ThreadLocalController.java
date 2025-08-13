package com.trip.noting.controller;

import com.alibaba.fastjson2.JSON;
import com.trip.noting.entity.UserEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThreadLocalController {

    private final ThreadLocal<UserEntity> localUser = new ThreadLocal<>();

    @GetMapping("/getUser")
    public UserEntity getUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("userEntity.getName");
        userEntity.setAge(22);
        localUser.set(userEntity);
        UserEntity userEntity1 = localUser.get();
        new Thread(() -> {
            UserEntity userEntity2 = localUser.get();
            if (userEntity2 != null) {
                System.out.println("userEntity2" + JSON.toJSONString(userEntity2));
            } else {
                System.out.println("userEntity2 is null");
            }
        }).start();
        System.out.println(JSON.toJSONString(userEntity1));
        localUser.remove();
        return userEntity;
    }
}
