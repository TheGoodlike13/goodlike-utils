package eu.goodlike.cache;

import eu.goodlike.cache.factory.CaffeineCacheFactory;
import eu.goodlike.cache.factory.GuavaCacheFactory;
import eu.goodlike.libraries.DependencyCheck;

import java.util.function.Function;

/**
 * Constructs new caches
 */
public interface CacheFactory {

    /**
     * @return a loading cache which wraps its values softly
     * @throws NullPointerException if cacheLoader is null
     */
    <K, V> CacheWrapper<K, V> makeSoftCache(Function<? super K, ? extends V> cacheLoader);

    /**
     * @return a loading cache factory, which constructs caches from available classes
     * @throws IllegalStateException if neither Caffeine, not Guava caches are available
     */
    static CacheFactory getAvailableCacheFactory() {
        if (DependencyCheck.isCaffeineAvailable())
            return new CaffeineCacheFactory();

        if (DependencyCheck.isGuavaAvailable())
            return new GuavaCacheFactory();

        throw new IllegalStateException("Please add Caffeine or Guava dependency when using Caches!");
    }

}
