package eu.goodlike.cache.factory;

import eu.goodlike.cache.CacheFactory;
import eu.goodlike.cache.CacheWrapper;
import eu.goodlike.cache.impl.CaffeineCacheWrapper;

import java.util.function.Function;

/**
 * Constructs Caffeine caches
 */
public final class CaffeineCacheFactory implements CacheFactory {

    @Override
    public <K, V> CacheWrapper<K, V> makeSoftCache(Function<? super K, ? extends V> cacheLoader) {
        return new CaffeineCacheWrapper<>(cacheLoader);
    }

}
