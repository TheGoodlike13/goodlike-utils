package eu.goodlike.functional;

import eu.goodlike.neat.Null;

import java.util.stream.Stream;

/**
 * Utility methods to work with Either
 */
public final class Eithers {

    /**
     * <pre>
     * Packs the outside {@link Stream} into the {@link Stream} inside of {@link Either} left values, using empty
     * {@link Stream} when not available
     *
     * In the case where any element in the {@link Stream} is not left, it is immediately returned instead
     *
     * Empty {@link Stream} produces left {@link Either} with an empty {@link Stream} inside
     * </pre>
     * @return {@link Either} containing the packed {@link Stream}
     * @throws NullPointerException if steamOfStreams is null
     */
    public static <T, R> Either<Stream<T>, R> packLeft(Stream<Either<Stream<T>, R>> streamOfStreams) {
        Null.check(streamOfStreams).as("streamOfStreams");
        return streamOfStreams.reduce(Either.left(Stream.empty()), Eithers::combineSubStreams);
    }

    /**
     * Right side version of {@link Eithers#packLeft(Stream)}
     *
     * @return {@link Either} containing the packed {@link Stream}
     * @throws NullPointerException if steamOfStreams is null
     */
    public static <T, L> Either<L, Stream<T>> packRight(Stream<Either<L, Stream<T>>> streamOfStreams) {
        Null.check(streamOfStreams).as("streamOfStreams");
        return packLeft(streamOfStreams.map(Either::swap)).swap();
    }

    // PRIVATE

    private Eithers() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

    private static <T, R> Either<Stream<T>, R> combineSubStreams(Either<Stream<T>, R> e1, Either<Stream<T>, R> e2) {
        if (!e1.isLeft())
            return e1;

        if (!e2.isLeft())
            return e2;

        return Either.left(Stream.concat(e1.getLeft(), e2.getLeft()));
    }

}
