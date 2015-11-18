package eu.goodlike.functional;

import eu.goodlike.neat.Null;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Utility methods to work with Optionals
 */
public final class Optionals {

    /**
     * @return Stream of only non empty optionals of the given ones
     * @throws NullPointerException if optionals is null, or any of the contained optionals are null (not empty,
     * but null themselves)
     */
    @SafeVarargs
    public static <T> Stream<T> asStream(Optional<T>... optionals) {
        Null.checkArray(optionals).ifAny("Optionals cannot be null!");
        return Stream.of(optionals)
                .filter(Optional::isPresent)
                .map(Optional::get);
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

    // PRIVATE

    private Optionals() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
