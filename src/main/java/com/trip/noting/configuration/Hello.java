package com.trip.noting.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Hello {

    private String helloName;
    @Autowired
    private World world;

    public String hello() {
        System.out.println("Hello.hello()");
        return "helloRes";
    }

}
