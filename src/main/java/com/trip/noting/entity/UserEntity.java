package com.trip.noting.entity;

import com.trip.noting.annotation.Entity;
import lombok.Data;

@Entity("user")
@Data
public class UserEntity {
    // 类内容
    private String name;
    private Integer age;
}