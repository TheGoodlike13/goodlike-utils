package eu.goodlike.functional;

/**
 * Contains special runnables
 */
public final class Runnables {

    /**
     * @return runnable which does nothing
     */
    public static Runnable doNothing() {
        return DO_NOTHING;
    }

    // PRIVATE

    private Runnables() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

    private static final Runnable DO_NOTHING = () -> {};

}
