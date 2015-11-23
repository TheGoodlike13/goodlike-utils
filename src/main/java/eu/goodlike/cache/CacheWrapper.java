package eu.goodlike.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * Wraps basic LoadingCache functionality; check implementations for in depth documentation
 */
public interface CacheWrapper<K, V> {

    /**
     * @return value for a given key, computing if it is not present
     * @throws NullPointerException if key is null
     */
    V get(K key);

    /**
     * @return values for given keys, computing if they are not present
     * @throws NullPointerException if keys are or contain null
     */
    Map<K, V> getAll(Iterable<? extends K> keys);

    /**
     * Recomputes the value for given key
     * @throws NullPointerException if key is null
     */
    void refresh(K key);

    /**
     * @return value for a given key, null if it not in the cache yet
     * @throws NullPointerException if key is null
     */
    V getIfPresent(Object key);

    /**
     * @return values for given keys, null if it not in the cache yet
     * @throws NullPointerException if keys are or contain null
     */
    Map<K, V> getAllPresent(Iterable<?> keys);

    /**
     * Sets the given value to given key in the cache
     * @throws NullPointerException if key or value is null
     */
    void put(K key, V value);

    /**
     * Sets the given values to given keys in the cache
     * @throws NullPointerException if map, or any of its keys or values is null
     */
    void putAll(Map<? extends K,? extends V> map);

    /**
     * Removes given key from the cache
     * @throws NullPointerException if key is null
     */
    void invalidate(Object key);

    /**
     * Removes given keys from the cache
     * @throws NullPointerException if keys are or contain null
     */
    void invalidateAll(Iterable<?> keys);

    /**
     * Removes all keys from the cache
     */
    void invalidateAll();

    /**
     * Returns this cache as a ConcurrentMap, modifiable
     */
    ConcurrentMap<K, V> asMap();

    /**
     * Finishes some pending operations, depends on implementation
     */
    void cleanUp();

}
