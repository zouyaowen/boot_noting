package com.trip.noting.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.PropertyPreFilter;
import com.trip.noting.binlog.BinlogMessage;
import com.trip.noting.binlog.ColumnMessage;
import com.trip.noting.entity.UserEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BinlogToJson {
    public static void main(String[] args) {
        List<UserEntity> userEntities = new ArrayList<>();
        userEntities.add(new UserEntity() {{
            this.setName("aaa");
            this.setAge(22);
        }});
        userEntities.add(new UserEntity() {{
            this.setName("bbb");
            this.setAge(33);
        }});
        System.out.println(JSON.toJSONString(userEntities));
        Object[] array = userEntities.toArray(new Object[0]);
        System.out.println(JSON.toJSONString(array));


    }

    private static String convertAfterColumns(List<ColumnMessage> afterColumns) {
        Map<String, Object> entityMap = new HashMap<>();
        for (ColumnMessage column : afterColumns) {
            String fieldName = column.getName();
            String value = column.getValue();

            // 类型转换逻辑
            Object parsedValue = StringUtils.isNumeric(value) ? parseNumber(value) : value;
            entityMap.put(fieldName, parsedValue);
        }
        return JSON.toJSONString(entityMap);
    }


    // 安全转换数值
    private static Object parseNumber(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException ex) {
                return value; // 回退为字符串
            }
        }
    }

}
