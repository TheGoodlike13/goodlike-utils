package eu.goodlike.neat;

import eu.goodlike.neat.impl.str.StringBuilderWrapper;

/**
 * <pre>
 * Creates StringBuilderWrapper instances
 *
 * StringBuilderWrapper allows simple but neat way to construct a String using chained methods
 *
 * In general, if you feel that you need to explicitly define a variable of StringBuilderWrapper, you might as well
 * just use StringBuilder in the first place; the wrapper is intended to take care of simple cases only
 * </pre>
 */
public final class Str {

    /**
     * @return empty StringBuilderWrapper
     */
    public static StringBuilderWrapper of() {
        return new StringBuilderWrapper();
    }

    /**
     * @return StringBuilderWrapper containing all the objects, appended together
     * @throws NullPointerException if object array is null (NOT if it contains null, that is allowed)
     */
    public static StringBuilderWrapper of(Object... objects) {
        return new StringBuilderWrapper().and(objects);
    }

    /**
     * @return StringBuilderWrapper containing all the objects, appended together, using the provided StringBuilder
     * @throws NullPointerException if customBuilder or object array is null (NOT if it contains null, that is allowed)
     */
    public static StringBuilderWrapper of(StringBuilder customBuilder, Object... objects) {
        Null.check(customBuilder).ifAny("Custom builder cannot be null");
        return new StringBuilderWrapper(customBuilder).and(objects);
    }

    // PRIVATE

    private Str() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
