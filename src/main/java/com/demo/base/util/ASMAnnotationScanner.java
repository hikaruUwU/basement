package com.demo.base.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings("unused")
public class ASMAnnotationScanner {
    private static final ResourcePatternResolver RESOURCE_RESOLVER = new PathMatchingResourcePatternResolver();

    private static final MetadataReaderFactory METADATA_FACTORY = new CachingMetadataReaderFactory();

    public static List<Class<?>> scanClassAnnotation(String packageName, Class<? extends Annotation> annotationType) throws Exception {

        List<Class<?>> results = new ArrayList<>();

        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(packageName) + "/**/*.class";

        Resource[] resources = RESOURCE_RESOLVER.getResources(packageSearchPath);

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        for (Resource resource : resources) {
            if (!resource.isReadable()) continue;

            MetadataReader reader = METADATA_FACTORY.getMetadataReader(resource);

            if (reader.getAnnotationMetadata().hasAnnotation(annotationType.getName())) {

                Class<?> clazz = Class.forName(reader.getClassMetadata().getClassName(), false, classLoader);

                results.add(clazz);
            }
        }

        return results;
    }

    public static Map<Class<?>, List<Method>> scanMethodAnnotation(String packageName, Class<? extends Annotation> annotationType) throws Exception {

        Map<Class<?>, List<Method>> resultMap = new HashMap<>();

        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(packageName) + "/**/*.class";

        Resource[] resources = RESOURCE_RESOLVER.getResources(packageSearchPath);

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        for (Resource resource : resources) {

            MetadataReader reader = METADATA_FACTORY.getMetadataReader(resource);

            Set<MethodMetadata> annotatedMethods = reader.getAnnotationMetadata().getAnnotatedMethods(annotationType.getName());

            if (annotatedMethods.isEmpty()) continue;

            Class<?> clazz = Class.forName(reader.getClassMetadata().getClassName(), false, classLoader);

            Method[] declaredMethods = clazz.getDeclaredMethods();

            Map<String, Method> methodMap = new HashMap<>();
            for (Method m : declaredMethods) {
                methodMap.put(m.getName(), m);
            }

            List<Method> methods = new ArrayList<>();

            for (MethodMetadata mm : annotatedMethods) {
                Method m = methodMap.get(mm.getMethodName());
                if (m != null) {
                    methods.add(m);
                }
            }

            resultMap.put(clazz, methods);
        }

        return resultMap;
    }
}