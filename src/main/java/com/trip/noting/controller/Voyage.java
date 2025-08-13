package com.trip.noting.controller;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;


@Data
public class Voyage {
    @ExcelProperty("航线Id/航线信息")
    private String name;
}
