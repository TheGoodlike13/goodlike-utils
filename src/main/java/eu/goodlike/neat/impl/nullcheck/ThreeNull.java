package eu.goodlike.neat.impl.nullcheck;

import eu.goodlike.neat.Null;

/**
 * Null implementation for three element check; should be quicker than straight up varargs
 */
public final class ThreeNull extends Null {

    @Override
    protected int indexOfNull() {
        return one == null
                ? 0
                : two == null
                ? 1
                : three == null
                ? 2
                : -1;
    }

    @Override
    protected String contentToString() {
        return "[" + one + ", " + two + ", " + three + "]";
    }

    // CONSTRUCTORS

    public ThreeNull(Object one, Object two, Object three) {
        this.one = one;
        this.two = two;
        this.three = three;
    }

    // PRIVATE

    private final Object one;
    private final Object two;
    private final Object three;

}
