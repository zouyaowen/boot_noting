package com.trip.noting.controller;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SheetExtData {
    @ExcelProperty("数据类型")
    private String name;
    // 使用索引标记实现横向扩展
    @ExcelProperty(value = "nameList", index = 1) // index从1开始
    private List<String> nameList;
}