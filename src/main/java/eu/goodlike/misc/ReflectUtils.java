package eu.goodlike.misc;

import java.lang.reflect.Modifier;

/**
 * <pre>
 * Contains methods that involve reflection
 *
 * Should be limited to simple checks, use jOOR for complex reflection
 * </pre>
 */
public final class ReflectUtils {

    /**
     * @return true if class is abstract, false otherwise
     */
    public static boolean isAbstract(Class<?> possiblyAbstractClass) {
        return Modifier.isAbstract(possiblyAbstractClass.getModifiers());
    }

    /**
     * @return true if class is has no abstract methods, false otherwise
     */
    public static boolean isImplemented(Class<?> possiblyAbstractClass) {
        return !possiblyAbstractClass.isInterface() && !isAbstract(possiblyAbstractClass);
    }

    // PRIVATE

    private ReflectUtils() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
