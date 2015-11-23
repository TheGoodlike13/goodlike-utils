package eu.goodlike.cache.impl;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.base.MoreObjects;
import eu.goodlike.cache.CacheWrapper;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * Caffeine cache wrapper, should only be used if Caffeine dependency is available
 */
public final class CaffeineCacheWrapper<K, V> implements CacheWrapper<K, V> {

    @Override
    public V get(K key) {
        return loadingCache.get(key);
    }

    @Override
    public Map<K, V> getAll(Iterable<? extends K> keys) {
        return loadingCache.getAll(keys);
    }

    @Override
    public void refresh(K key) {
        loadingCache.refresh(key);
    }

    @Override
    public V getIfPresent(Object key) {
        return loadingCache.getIfPresent(key);
    }

    @Override
    public Map<K, V> getAllPresent(Iterable<?> keys) {
        return loadingCache.getAllPresent(keys);
    }

    @Override
    public void put(K key, V value) {
        loadingCache.put(key, value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        loadingCache.putAll(map);
    }

    @Override
    public void invalidate(Object key) {
        loadingCache.invalidate(key);
    }

    @Override
    public void invalidateAll(Iterable<?> keys) {
        loadingCache.invalidateAll(keys);
    }

    @Override
    public void invalidateAll() {
        loadingCache.invalidateAll();
    }

    @Override
    public ConcurrentMap<K, V> asMap() {
        return loadingCache.asMap();
    }

    @Override
    public void cleanUp() {
        loadingCache.cleanUp();
    }

    // CONSTRUCTORS

    public CaffeineCacheWrapper(Function<? super K, ? extends V> cacheLoader) {
        loadingCache = Caffeine.newBuilder()
                .softValues()
                .build(cacheLoader::apply);
    }

    // PRIVATE

    private final LoadingCache<K, V> loadingCache;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("loadingCache", loadingCache)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CaffeineCacheWrapper)) return false;
        CaffeineCacheWrapper<?, ?> that = (CaffeineCacheWrapper<?, ?>) o;
        return Objects.equals(loadingCache, that.loadingCache);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loadingCache);
    }

}
