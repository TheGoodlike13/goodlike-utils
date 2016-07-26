package eu.goodlike.misc;

import eu.goodlike.neat.Null;

import java.lang.reflect.Modifier;
import java.util.Optional;

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
     * @throws NullPointerException if possiblyAbstractClass is null
     */
    public static boolean isAbstract(Class<?> possiblyAbstractClass) {
        Null.check(possiblyAbstractClass).ifAny("Cannot be null: possiblyAbstractClass");
        return Modifier.isAbstract(possiblyAbstractClass.getModifiers());
    }

    /**
     * @return true if class is has no abstract methods, false otherwise
     * @throws NullPointerException if possiblyAbstractClass is null
     */
    public static boolean isImplemented(Class<?> possiblyAbstractClass) {
        Null.check(possiblyAbstractClass).ifAny("Cannot be null: possiblyAbstractClass");
        return !possiblyAbstractClass.isInterface() && !isAbstract(possiblyAbstractClass);
    }

    /**
     * @return object cast as specific class, Optional::empty if object is not an instance of expectedClass
     * @throws NullPointerException if object or expectedClass is null
     */
    public static <BaseClass, ExpectedClass extends BaseClass> Optional<ExpectedClass> cast(BaseClass object,
                                                                                            Class<ExpectedClass> expectedClass) {
        Null.check(object, expectedClass).ifAny("Cannot be null: object, expectedClass");
        return expectedClass.isInstance(object)
                ? Optional.of(expectedClass.cast(object))
                : Optional.empty();
    }

    // PRIVATE

    private ReflectUtils() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
