package eu.goodlike.functional;

import eu.goodlike.neat.Null;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;
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
     * @return Stream of only non empty optionals of the given ones, fetching their values lazily
     * @throws NullPointerException if lazyOptionals is or contains null
     */
    @SafeVarargs
    public static <T> Stream<T> lazyStream(Supplier<Optional<T>>... lazyOptionals) {
        Null.checkArray(lazyOptionals).ifAny("Cannot be or contain null: lazyOptionals");
        return Arrays.stream(lazyOptionals)
                .map(Supplier::get)
                .flatMap(Optionals::asStream);
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
     * @return first non empty optional of the given ones, fetching their values lazily
     * @throws NullPointerException if lazyOptionals is or contains null
     */
    @SafeVarargs
    public static <T> Optional<T> lazyFirstNotEmpty(Supplier<Optional<T>>... lazyOptionals) {
        Null.checkArray(lazyOptionals).ifAny("Cannot be or contain null: lazyOptionals");
        return lazyStream(lazyOptionals).findFirst();
    }

    // PRIVATE

    private Optionals() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
