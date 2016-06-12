package eu.goodlike.neat.impl.nullcheck;

import eu.goodlike.neat.Null;

/**
 * Null implementation for four element check; should be quicker than straight up varargs
 */
public final class FourNull extends Null {

    @Override
    protected int indexOfNull() {
        return one == null
                ? 0
                : two == null
                ? 1
                : three == null
                ? 2
                : four == null
                ? 3
                : -1;
    }

    @Override
    protected String contentToString() {
        return "{" + one + ", " + two + ", " + three + ", " + four + "}";
    }

    // CONSTRUCTORS

    public FourNull(Object one, Object two, Object three, Object four) {
        this.one = one;
        this.two = two;
        this.three = three;
        this.four = four;
    }

    // PRIVATE

    private final Object one;
    private final Object two;
    private final Object three;
    private final Object four;

}
