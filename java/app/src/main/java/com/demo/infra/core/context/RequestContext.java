package com.demo.infra.core.context;

public interface RequestContext<K,V> {
    void put(K key, V value);
    V get(K key);
    void destroy();
}
