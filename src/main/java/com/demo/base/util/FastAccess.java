package com.demo.base.util;

import jakarta.annotation.Nonnull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
@SuppressWarnings("unused")
public class FastAccess {
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

//======================================================================================================================

    private static final ClassValue<Map<String, VarHandle>> FIELD_CV = new ClassValue<>() {
        @Override
        protected Map<String, VarHandle> computeValue(@Nonnull Class<?> type) {
            return new ConcurrentHashMap<>();
        }
    };

    private static <T> VarHandle getVarHandle(Class<T> clazz, String fieldName) {
        return FIELD_CV.get(clazz).computeIfAbsent(fieldName, name -> bootstrapVarHandle(clazz, name));
    }

    // --- bootstrap ---

    @SneakyThrows({})
    public static <T> VarHandle bootstrapVarHandle(Class<T> clazz, String fieldName) {
        return MethodHandles.privateLookupIn(clazz, LOOKUP).unreflectVarHandle(clazz.getDeclaredField(fieldName));
    }

    // --- VarHandler Facades ---

    @SuppressWarnings("unchecked")
    public static <T, R> R getAs(T target, String fieldName) {
        return (R) getVarHandle(target.getClass(), fieldName).get(target);
    }

    public static void set(Object target, String fieldName, Object value) {
        getVarHandle(target.getClass(), fieldName).set(target, value);
    }

    @SuppressWarnings("unchecked")
    public static <T, R> R getOpaqueAs(T target, String fieldName) {
        return (R) getVarHandle(target.getClass(), fieldName).getOpaque(target);
    }

    public static void setOpaque(Object target, String fieldName, Object value) {
        getVarHandle(target.getClass(), fieldName).setOpaque(target, value);
    }

    @SuppressWarnings("unchecked")
    public static <T, R> R getAcquireAs(Object target, String fieldName) {
        return (R) getVarHandle(target.getClass(), fieldName).getAcquire(target);
    }

    public static void setRelease(Object target, String fieldName, Object value) {
        getVarHandle(target.getClass(), fieldName).setRelease(target, value);
    }

    @SuppressWarnings("unchecked")
    public static <T, R> R getVolatileAs(T target, String fieldName) {
        return (R) getVarHandle(target.getClass(), fieldName).getVolatile(target);
    }

    public static void setVolatile(Object target, String fieldName, Object value) {
        getVarHandle(target.getClass(), fieldName).setVolatile(target, value);
    }

    public static boolean compareAndSet(Object target, String fieldName, Object expected, Object newValue) {
        return getVarHandle(target.getClass(), fieldName).compareAndSet(target, expected, newValue);
    }

//======================================================================================================================

    @SneakyThrows({})
    public static <T> MethodHandle bootstrapUnbindedMethodHandle(Class<T> instance, String functionName, Class<?> returnClasses, Class<?>... parameterClasses) {
        return LOOKUP.findVirtual(instance, functionName, MethodType.methodType(returnClasses, parameterClasses));
    }

}