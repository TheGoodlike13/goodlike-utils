package eu.goodlike.cache.impl;

import com.google.common.base.MoreObjects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import eu.goodlike.cache.CacheWrapper;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

/**
 * Guava cache wrapper, should only be used if Guava dependency is available
 */
public final class GuavaCacheWrapper<K, V> implements CacheWrapper<K, V> {

    @Override
    public V get(K key) {
        return loadingCache.getUnchecked(key);
    }

    @Override
    public Map<K, V> getAll(Iterable<? extends K> keys) {
        try {
            return loadingCache.getAll(keys);
        } catch (ExecutionException e) {
            throw new IllegalStateException("Execution failed when getting all keys", e);
        }
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

    public GuavaCacheWrapper(Function<? super K, ? extends V> cacheLoader) {
        loadingCache = CacheBuilder.newBuilder()
                .softValues()
                .build(CacheLoader.from(cacheLoader::apply));
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
        if (!(o instanceof GuavaCacheWrapper)) return false;
        GuavaCacheWrapper<?, ?> that = (GuavaCacheWrapper<?, ?>) o;
        return Objects.equals(loadingCache, that.loadingCache);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loadingCache);
    }

}
