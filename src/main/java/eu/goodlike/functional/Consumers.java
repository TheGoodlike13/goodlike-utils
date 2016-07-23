package eu.goodlike.functional;

import java.util.function.Consumer;

/**
 * Contains special consumers
 */
public final class Consumers {

    /**
     * @return consumer which does nothing
     */
    public static <T> Consumer<T> doNothing() {
        @SuppressWarnings("unchecked")
        Consumer<T> doNothing = (Consumer<T>) DO_NOTHING;
        return doNothing;
    }

    // PRIVATE

    private Consumers() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

    private static final Consumer<?> DO_NOTHING = any -> {};

}
