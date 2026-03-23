package com.demo.base.util;

import jakarta.annotation.Nonnull;

import java.lang.invoke.VarHandle;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@SuppressWarnings("unused")
public class SimpleSoftConcurrentHashMapCache<K, V> implements Map<K, V> {
    private final Map<K, SoftValue<K, V>> map = new ConcurrentHashMap<>();
    private final ReferenceQueue<V> queue = new ReferenceQueue<>();
    @SuppressWarnings("unused")
    protected long p01, p02, p03, p04, p05, p06, p07 = Integer.MAX_VALUE;
    @SuppressWarnings("FieldMayBeFinal")
    private volatile long opCount = 0;
    @SuppressWarnings("unused")
    protected long p11, p12, p13, p14, p15, p16, p17 = Integer.MIN_VALUE;
    @SuppressWarnings("FieldMayBeFinal")
    private volatile int cleaning = 0;
    @SuppressWarnings("unused")
    protected long p21, p22, p23, p24, p25, p26, p27 = Integer.MAX_VALUE;

    private static final VarHandle OP_COUNT_VH = FastAccess.bootstrapVarHandle(SimpleSoftConcurrentHashMapCache.class, "opCount");
    private static final VarHandle CLEANING_VH = FastAccess.bootstrapVarHandle(SimpleSoftConcurrentHashMapCache.class, "cleaning");

    private static final int CLEANUP_THRESHOLD_MASK = 64 - 1;

    private static class SoftValue<K, V> extends SoftReference<V> {
        private final K key;

        public SoftValue(K key, V value, ReferenceQueue<V> queue) {
            super(value, queue);
            this.key = key;
        }
    }

    @SuppressWarnings("unchecked")
    private void processQueueIfNeeded() {
        long count = (long) OP_COUNT_VH.getAndAdd(this, 1L);

        if ((count & CLEANUP_THRESHOLD_MASK) == 0) {
            if (CLEANING_VH.compareAndSet(this, 0, 1)) {
                try {
                    SoftValue<K, V> sv;
                    while ((sv = (SoftValue<K, V>) queue.poll()) != null) {
                        map.remove(sv.key, sv);
                    }
                } finally {
                    CLEANING_VH.setVolatile(this, 0);
                }
            }
        }
    }

    @Override
    public V get(Object key) {
        processQueueIfNeeded();
        SoftValue<K, V> sv = map.get(key);
        return (sv != null) ? sv.get() : null;
    }

    @Override
    public V put(K key, V value) {
        processQueueIfNeeded();
        SoftValue<K, V> prev = map.put(key, new SoftValue<>(key, value, queue));
        return (prev != null) ? prev.get() : null;
    }

    @Override
    public V remove(Object key) {
        processQueueIfNeeded();
        SoftValue<K, V> prev = map.remove(key);
        return (prev != null) ? prev.get() : null;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        if (oldValue == null || newValue == null) return false;
        processQueueIfNeeded();

        final boolean[] replaced = {false};

        map.computeIfPresent(key, (k, currentSv) -> {
            V currentValue = currentSv.get();

            if (currentValue == null) {
                return null;
            }

            if (Objects.equals(currentValue, oldValue)) {
                replaced[0] = true;
                return new SoftValue<>(key, newValue, queue);
            }
            return currentSv;
        });

        return replaced[0];
    }

    @Override
    public int size() {
        processQueueIfNeeded();
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        processQueueIfNeeded();
        return map.isEmpty();
    }

    @Override
    public void clear() {
        map.clear();
        while (queue.poll() != null) {
            continue;
        }
    }

    @Override
    public @Nonnull Set<K> keySet() {
        processQueueIfNeeded();
        return map.keySet();
    }

    @Override
    public @Nonnull Collection<V> values() {
        processQueueIfNeeded();
        return new AbstractCollection<>() {
            @Override
            public @Nonnull Iterator<V> iterator() {
                return new Iterator<>() {
                    private final Iterator<SoftValue<K, V>> it = map.values().iterator();
                    private V nextValue = null;

                    @Override
                    public boolean hasNext() {
                        while (nextValue == null && it.hasNext()) {
                            nextValue = it.next().get();
                        }
                        return nextValue != null;
                    }

                    @Override
                    public V next() {
                        if (!hasNext()) throw new NoSuchElementException();
                        V val = nextValue;
                        nextValue = null;
                        return val;
                    }
                };
            }

            @Override
            public int size() {
                return SimpleSoftConcurrentHashMapCache.this.size();
            }
        };
    }

    @Override
    public @Nonnull Set<Map.Entry<K, V>> entrySet() {
        processQueueIfNeeded();
        return new AbstractSet<>() {
            @Override
            public @Nonnull Iterator<Map.Entry<K, V>> iterator() {
                return new Iterator<>() {
                    private final Iterator<Map.Entry<K, SoftValue<K, V>>> it = map.entrySet().iterator();
                    private Map.Entry<K, V> nextEntry = null;

                    @Override
                    public boolean hasNext() {
                        while (nextEntry == null && it.hasNext()) {
                            Map.Entry<K, SoftValue<K, V>> rawEntry = it.next();
                            SoftValue<K, V> sv = rawEntry.getValue();
                            V value = (sv != null) ? sv.get() : null;

                            if (value != null) {

                                nextEntry = new AbstractMap.SimpleImmutableEntry<>(rawEntry.getKey(), value);
                            }

                        }
                        return nextEntry != null;
                    }

                    @Override
                    public Map.Entry<K, V> next() {
                        if (!hasNext()) throw new NoSuchElementException();
                        Map.Entry<K, V> res = nextEntry;
                        nextEntry = null;
                        return res;
                    }

                    @Override
                    public void remove() {
                        it.remove();
                    }
                };
            }

            @Override
            public int size() {
                return SimpleSoftConcurrentHashMapCache.this.size();
            }
        };
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (value == null) return false;
        processQueueIfNeeded();
        for (SoftValue<K, V> sv : map.values()) {
            V actualValue = sv.get();
            if (actualValue != null && Objects.equals(value, actualValue)) {
                return true;
            }
        }
        return false;
    }

    public V computeIfAbsent(K key, @Nonnull Function<? super K, ? extends V> mappingFunction) {
        processQueueIfNeeded();

        SoftValue<K, V> newSv = map.compute(key, (k, currentSv) -> {

            if (currentSv != null && currentSv.get() != null) {
                return currentSv;
            }

            V newValue = mappingFunction.apply(k);
            if (newValue == null) return null;

            return new SoftValue<>(k, newValue, queue);
        });

        return (newSv != null) ? newSv.get() : null;
    }

    @Override
    public void putAll(@Nonnull Map<? extends K, ? extends V> m) {
        m.forEach(this::put);
    }
}