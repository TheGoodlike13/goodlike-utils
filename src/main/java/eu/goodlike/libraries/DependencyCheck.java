package eu.goodlike.libraries;

import eu.goodlike.neat.Null;

/**
 * Allows basic checking if certain dependencies are available; no guarantee as to version is made!
 */
public final class DependencyCheck {

    /**
     * @return true if Caffeine classes are available, false otherwise
     */
    public static boolean isCaffeineAvailable() {
        return isAvailable("com.github.benmanes.caffeine.cache.LoadingCache");
    }

    /**
     * @return true if Guava classes are available, false otherwise
     */
    public static boolean isGuavaAvailable() {
        return isAvailable("com.google.common.cache.LoadingCache");
    }

    /**
     * fullClassName should include the package, i.e. for this class: 'eu.goodlike.libraries.DependencyCheck'
     * @return true if given class is available, false otherwise
     * @throws NullPointerException if fullClassName is null
     */
    public static boolean isAvailable(String fullClassName) {
        Null.check(fullClassName).ifAny("Class name cannot be null");
        try {
            return Class.forName(fullClassName) != null;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    // PRIVATE

    private DependencyCheck() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
