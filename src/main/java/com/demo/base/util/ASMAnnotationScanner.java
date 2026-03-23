package com.demo.base.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.lang.annotation.Annotation;
import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.util.ClassUtils.convertClassNameToResourcePath;

@SuppressWarnings("unused,unchecked")
public class ASMAnnotationScanner {
    private static final ResourcePatternResolver RESOURCE_RESOLVER = new PathMatchingResourcePatternResolver();

    private static final MetadataReaderFactory METADATA_FACTORY = new CachingMetadataReaderFactory();

    private static final Map<String, SoftReference<Object>> SOFT_CACHE = new ConcurrentHashMap<>();

    public static List<Class<?>> scanClassAnnotation(String packageName, Class<? extends Annotation> annotationType) throws Exception {
        String cacheKey = packageName + ":" + annotationType.getName() + ":CLS";
        Object cached = getFromCache(cacheKey);
        if (cached != null) return (List<Class<?>>) cached;

        List<Class<?>> results = new ArrayList<>();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                convertClassNameToResourcePath(packageName) + "/**/*.class";

        Resource[] resources = RESOURCE_RESOLVER.getResources(packageSearchPath);
        for (Resource resource : resources) {
            if (resource.isReadable()) {
                MetadataReader reader = METADATA_FACTORY.getMetadataReader(resource);
                if (reader.getAnnotationMetadata().hasAnnotation(annotationType.getName())) {
                    results.add(Class.forName(reader.getClassMetadata().getClassName()));
                }
            }
        }
        SOFT_CACHE.put(cacheKey, new SoftReference<>(results));
        return results;
    }

    public static Map<Class<?>, List<Method>> scanMethodAnnotation(String packageName, Class<? extends Annotation> annotationType) throws Exception {
        String cacheKey = packageName + ":" + annotationType.getName() + ":MET";
        Object cached = getFromCache(cacheKey);
        if (cached != null) return (Map<Class<?>, List<Method>>) cached;

        Map<Class<?>, List<Method>> resultMap = new HashMap<>();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + convertClassNameToResourcePath(packageName) + "/**/*.class";

        Resource[] resources = RESOURCE_RESOLVER.getResources(packageSearchPath);
        for (Resource resource : resources) {
            MetadataReader reader = METADATA_FACTORY.getMetadataReader(resource);
            Set<MethodMetadata> annotatedMethods = reader.getAnnotationMetadata()
                    .getAnnotatedMethods(annotationType.getName());

            if (!annotatedMethods.isEmpty()) {
                Class<?> clazz = Class.forName(reader.getClassMetadata().getClassName());
                List<Method> methods = new ArrayList<>();
                for (MethodMetadata mm : annotatedMethods) {
                    for (Method m : clazz.getDeclaredMethods()) {
                        if (m.getName().equals(mm.getMethodName())) {
                            methods.add(m);
                        }
                    }
                }
                resultMap.put(clazz, methods);
            }
        }
        SOFT_CACHE.put(cacheKey, new SoftReference<>(resultMap));
        return resultMap;
    }

    private static Object getFromCache(String key) {
        SoftReference<Object> ref = SOFT_CACHE.get(key);
        return (ref != null) ? ref.get() : null;
    }
}
