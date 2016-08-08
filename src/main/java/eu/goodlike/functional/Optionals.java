package eu.goodlike.functional;

import eu.goodlike.neat.Null;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Utility methods to work with Optionals
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class Optionals {

    /**
     * @return Stream of only non empty optionals of the given ones
     * @throws NullPointerException if optionals is null, or any of the contained optionals are null (not empty,
     * but null themselves)
     */
    @SafeVarargs
    public static <T> Stream<T> asStream(Optional<T>... optionals) {
        Null.checkArray(optionals).ifAny("Cannot be or contain null: optionals");
        return Stream.of(optionals)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    /**
     * This is optimized version of Optionals::asStream for a single optional
     * @return Stream that contains an element from singleOptional, if it contains any
     * @throws NullPointerException if singleOptional is null
     */
    public static <T> Stream<T> asStream(Optional<T> singleOptional) {
        Null.check(singleOptional).ifAny("Cannot be null: singleOptional");
        return singleOptional.isPresent()
                ? Stream.of(singleOptional.get())
                : Stream.empty();
    }

    /**
     * @return first non empty optional of the given ones
     * @throws NullPointerException if optionals is null, or any of the contained optionals are null (not empty,
     * but null themselves)
     */
    @SafeVarargs
    public static <T> Optional<T> firstNotEmpty(Optional<T>... optionals) {
        return asStream(optionals).findFirst();
    }

    /**
     * This is optimized version of Optionals::firstNotEmpty for a single optional
     * @return singleOptional
     * @throws NullPointerException if optionals is null, or any of the contained optionals are null (not empty,
     * but null themselves)
     */
    public static <T> Optional<T> firstNotEmpty(Optional<T> singleOptional) {
        Null.check(singleOptional).ifAny("Cannot be null: singleOptional");
        return singleOptional;
    }

    // PRIVATE

    private Optionals() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
