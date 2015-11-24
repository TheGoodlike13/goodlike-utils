package eu.goodlike.cache.factory;

import eu.goodlike.cache.CacheFactory;
import eu.goodlike.cache.CacheWrapper;
import eu.goodlike.cache.impl.GuavaCacheWrapper;

import java.util.function.Function;

/**
 * Constructs Guava caches
 */
public final class GuavaCacheFactory implements CacheFactory {

    @Override
    public <K, V> CacheWrapper<K, V> makeSoftCache(Function<? super K, ? extends V> cacheLoader) {
        return new GuavaCacheWrapper<>(cacheLoader);
    }

}
