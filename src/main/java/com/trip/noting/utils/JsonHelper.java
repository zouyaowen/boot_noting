package com.trip.noting.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
    改用JacksonHelper
 */
@Slf4j
public class JsonHelper {
    
    
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 对象的所有字段全部列入
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        // 支持Java8 时间格式
        // objectMapper.registerModule(new JavaTimeModule());

        // objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // 全局忽略空字段
        // objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // 取消默认转换timesstamps(时间戳)形式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // 忽略空bean转json错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // 忽略在json字符串中存在，在java类中不存在字段，防止错误
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 忽略字段大小写
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);

        // 允许字段名不加双引号
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        // 允许出现单引号
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        // 存在问题，先注释掉
        // objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 序列化T对象为JSON字符串
     */
    public static <T> String toJSONString(T obj) {
        if (obj == null) {
            return null;
        }

        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("JsonHelper-toJSONString", e);
        }
        return null;
    }

    /**
     * 从JSON字符串中反序列化T对象
     */
    public static <T> T parseFromJson(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json) || clazz == null) {
            return null;
        }
        try {
            if (StringUtils.isNotEmpty(json)) {
                return clazz.equals(String.class) ? (T) json : objectMapper.readValue(json, clazz);
            }
        } catch (Exception e) {
            log.error("JsonHelper-parseFromJson", e);
        }
        return null;
    }

    /**
     * 从JSON字符串中反序列化List<T>集合对象
     */
    public static <T> List<T> parseListFromJson(String json, Class<?>... elements) {
        List<T> list = getListFromJson(json, elements);
        return list != null ? list : new ArrayList<>();
    }

    private static <T> List<T> getListFromJson(String json, Class<?>... elements) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, elements);

        try {
            if (StringUtils.isNotEmpty(json)) {
                return objectMapper.readValue(json, javaType);
            }
        } catch (Exception e) {
            log.error("JsonHelper-getListFromJson", e);
        }
        return null;
    }


    // requestPayload
    public static <T> List<T> getListFromRequestPayload(String requestPayload, String parameterName, Class<?>... elements) {
        List<T> list = getList(requestPayload, parameterName, elements);
        return list != null ? list : new ArrayList<>();
    }

    private static <T> List<T> getList(String requestPayload, String parameterName, Class<?>... elements) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, elements);

        try {
            if (StringUtils.isNotEmpty(requestPayload) && StringUtils.isNotEmpty(parameterName)) {
                JsonNode jsonNode = objectMapper.readTree(requestPayload);
                Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> entry = fields.next();
                    if (entry.getKey().equalsIgnoreCase(parameterName) && entry.getValue() != null) {
                        String str1 = entry.getValue().toString();
                        String str2 = entry.getValue().asText();
                        return objectMapper.readValue(str2.isEmpty() ? str1 : str2, javaType);
                    }
                }
            }
        } catch (IOException e) {
            log.error("JsonHelper-getList", e);
        }
        return null;
    }

    /**
     * 用于序列化SOA对象时去除SCHEMA字段
     */
    public static <T> String getJsonSerializeFilterForSoa(Object object) {
        // 对"schema"属性屏蔽
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("", SimpleBeanPropertyFilter.serializeAllExcept("schema"));
        objectMapper.setFilterProvider(filterProvider);

        String result = "";
        try {
            result = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("JsonHelper-getJsonSerializeFilterForSoa", e);
        }
        return result;
    }

    // String
    public static String getStringFromRequestPayload(String requestPayload, String parameterName) {
        try {
            if (StringUtils.isNotEmpty(requestPayload) && StringUtils.isNotEmpty(parameterName)) {
                JsonNode jsonNode = objectMapper.readTree(requestPayload);
                Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> entry = fields.next();
                    if (entry.getKey().equalsIgnoreCase(parameterName) && entry.getValue() != null) {
                        return entry.getValue().asText();
                    }
                }
            }
        } catch (IOException e) {
            log.error("JsonHelper-getStringFromRequestPayload", e);
        }
        return null;
    }
}
