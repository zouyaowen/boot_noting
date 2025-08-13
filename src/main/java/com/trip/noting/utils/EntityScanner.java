package com.trip.noting.utils;

import com.trip.noting.annotation.Entity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class EntityScanner {

    public Map<String, Class<?>> scanEntities(String basePackage) {
        Map<String, Class<?>> entityMap = new HashMap<>();
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));

        for (BeanDefinition bd : scanner.findCandidateComponents(basePackage)) {
            String className = bd.getBeanClassName();
            try {
                Class<?> clazz = Class.forName(className);
                Entity entityAnnotation = clazz.getAnnotation(Entity.class);
                if (entityAnnotation != null) {
                    String key = entityAnnotation.value();
                    entityMap.put(key, clazz);
                }
            } catch (ClassNotFoundException e) {
                log.error("scanEntities:{}", e.getMessage());
            }
        }
        return entityMap;
    }
}