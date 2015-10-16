package eu.goodlike.libraries.jool;

import org.jooq.lambda.Seq;

import java.util.stream.Stream;

/**
 * Replaces some of the methods of JOOL Seq class, because they have a tendency to not work (I am not sure if it's
 * intellij idea's fault though)
 */
public final class Sequence {

    /**
     * This method replaces Seq.of(T[]), which intellij idea dislikes for some reason
     */
    @SafeVarargs
    public static <T> Seq<T> of(T... array) {
        return Seq.seq(Stream.of(array));
    }

    /**
     * This method replaces Seq.of(T[]), which intellij idea dislikes for some reason; static import version
     */
    @SafeVarargs
    public static <T> Seq<T> seq(T... array) {
        return Seq.seq(Stream.of(array));
    }

    // PRIVATE

    private Sequence() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
