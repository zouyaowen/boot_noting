package com.trip.noting.controller;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;


@Data
public class RoomType {
    @ExcelProperty("结构化房型名称")
    private String name;
    @ExcelProperty("物理房型编码")
    private String code;
}
