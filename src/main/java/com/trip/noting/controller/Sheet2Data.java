package com.trip.noting.controller;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class Sheet2Data {
    @ExcelProperty("用户ID")
    private Long id;
    @ExcelProperty("用户名称")
    private String name;
    @ExcelProperty("用户年龄")
    private Integer age;
}