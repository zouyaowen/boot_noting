package com.trip.noting.controller;

import com.alibaba.fastjson2.JSON;
import com.trip.noting.utils.EntityScanner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
public class ScannerController {
    @GetMapping("scannerEntity")
    public void scannerEntity() throws IOException {
        EntityScanner scanner = new EntityScanner();
        Map<String, Class<?>> stringClassMap = scanner.scanEntities("com.trip.noting.entity");
        System.out.println(JSON.toJSONString(stringClassMap));
    }
}
