package eu.goodlike.misc;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Global thread pools, used by various classes of this library
 */
public final class ThreadPools {

    /**
     * @return cached thread pool, which is best for many short tasks
     */
    public static Executor forShortTasks() {
        return CACHED_EX;
    }

    /**
     * @return fixed thread pool with size equal to available processors, which is best for longer tasks
     */
    public static Executor forLongTasks() {
        return FIXED_EX;
    }

    // PRIVATE

    private ThreadPools() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

    private static final int CORE_COUNT = Runtime.getRuntime().availableProcessors();

    private static final Executor CACHED_EX = Executors.newCachedThreadPool();
    private static final Executor FIXED_EX = Executors.newFixedThreadPool(CORE_COUNT);

}
