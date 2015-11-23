package eu.goodlike.libraries;

public final class DependencyCheck {

    public static boolean isCaffeineAvailable() {
        return isAvailable("com.github.benmanes.caffeine.cache.LoadingCache");
    }

    public static boolean isGuavaAvailable() {
        return isAvailable("com.google.common.cache.LoadingCache");
    }

    public static boolean isAvailable(String fullClassName) {
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
