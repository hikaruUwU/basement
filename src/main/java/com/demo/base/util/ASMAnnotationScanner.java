package com.demo.base.util;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Component
@SuppressWarnings("unused")
public class ASMAnnotationScanner implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    private final MetadataReaderFactory metadataFactory = new CachingMetadataReaderFactory();

    private final Map<String, Resource[]> resourceCache = new ConcurrentHashMap<>();

    private String baseSearchPath;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        try {
            this.baseSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(AutoConfigurationPackages.get(applicationContext).getFirst()) + "/**/*.class";
            log.info(baseSearchPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Resource[] getPackageResources() {
        if (baseSearchPath == null) {
            throw new IllegalStateException();
        }
        return resourceCache.computeIfAbsent(baseSearchPath, path -> {
            try {
                return resourceResolver.getResources(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public List<Class<?>> scanClassAnnotation(Class<? extends Annotation> annotationType) throws Exception {
        List<Class<?>> results = new ArrayList<>();
        Resource[] resources = getPackageResources();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        for (Resource resource : resources) {
            if (!resource.isReadable()) continue;

            MetadataReader reader = metadataFactory.getMetadataReader(resource);

            if (reader.getAnnotationMetadata().hasAnnotation(annotationType.getName())) {
                String className = reader.getClassMetadata().getClassName();
                results.add(Class.forName(className, false, classLoader));
            }
        }
        return results;
    }

    public Map<Class<?>, List<Method>> scanMethodAnnotation(Class<? extends Annotation> annotationType) throws Exception {
        Map<Class<?>, List<Method>> resultMap = new HashMap<>();
        Resource[] resources = getPackageResources();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        for (Resource resource : resources) {
            if (!resource.isReadable()) continue;

            MetadataReader reader = metadataFactory.getMetadataReader(resource);

            if (reader.getAnnotationMetadata().getAnnotatedMethods(annotationType.getName()).isEmpty()) {
                continue;
            }

            Class<?> clazz = Class.forName(reader.getClassMetadata().getClassName(), false, classLoader);
            List<Method> annotatedMethods = new ArrayList<>();

            ReflectionUtils.doWithMethods(clazz, method -> {
                if (AnnotatedElementUtils.hasAnnotation(method, annotationType)) {
                    annotatedMethods.add(method);
                }
            });

            if (!annotatedMethods.isEmpty()) {
                resultMap.put(clazz, annotatedMethods);
            }
        }
        return resultMap;
    }
}