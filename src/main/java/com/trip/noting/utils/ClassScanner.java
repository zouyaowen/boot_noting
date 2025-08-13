package com.trip.noting.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ClassScanner {


    public Map<String, Class<?>> scanEntities(String basePackage, Class<? extends Annotation> scannClass, String propertyName) {
        Map<String, Class<?>> entityMap = new HashMap<>();
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(scannClass));

        for (BeanDefinition bd : scanner.findCandidateComponents(basePackage)) {
            String className = bd.getBeanClassName();
            try {
                Class<?> clazz = Class.forName(className);
                Annotation annotation = clazz.getAnnotation(scannClass);
                if (annotation != null) {
                    // 动态提取注解属性
                    String key = extractAnnotationValue(annotation, propertyName);
                    entityMap.put(key, clazz);
                }
            } catch (ClassNotFoundException e) {
                log.error("scanEntities={}", e.getMessage());
            }
        }
        return entityMap;
    }

    /**
     * 通过反射提取注解的value属性值
     */
    private String extractAnnotationValue(Annotation annotation, String propertyName) {
        try {
            // 反射调用注解的value()方法
            if (propertyName == null || propertyName.isEmpty()) {
                propertyName = "value";
            }
            return (String) annotation.annotationType().getMethod(propertyName).invoke(annotation);
        } catch (Exception e) {
            throw new IllegalArgumentException("Annotation @" + annotation.annotationType().getSimpleName() + " must have a String value() method", e);
        }
    }
}