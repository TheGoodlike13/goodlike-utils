package eu.goodlike.cache;

import eu.goodlike.misc.Singleton;
import eu.goodlike.neat.Null;

import java.util.function.Function;

/**
 * Utility class to get a wrapped Cache (allows avoiding issues with dependencies)
 */
public final class Caches {

    /**
     * @return a loading cache which wraps its values softly
     * @throws NullPointerException if cacheLoader is null
     * @throws IllegalStateException if neither Caffeine, not Guava caches are available
     */
    public static <K, V> CacheWrapper<K, V> softCache(Function<? super K, ? extends V> cacheLoader) {
        Null.check(cacheLoader).ifAny("Cache loader cannot be null");
        return cacheFactory.get().makeSoftCache(cacheLoader);
    }

    // PRIVATE

    private static final Singleton<CacheFactory> cacheFactory = Singleton.of(CacheFactory::getAvailableCacheFactory);

    private Caches() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
