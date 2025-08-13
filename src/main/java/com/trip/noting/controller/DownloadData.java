package com.trip.noting.controller;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DownloadData {
    @ExcelProperty("名称")
    private String name;
    @ExcelProperty("时间")
    private Date time;
    @ExcelProperty("数字")
    private Double number;
}