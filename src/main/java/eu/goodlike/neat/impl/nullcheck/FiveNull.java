package eu.goodlike.neat.impl.nullcheck;

import eu.goodlike.neat.Null;

/**
 * Null implementation for five element check; should be quicker than straight up varargs
 */
public final class FiveNull extends Null {

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
                : five == null
                ? 4
                : -1;
    }

    @Override
    protected String contentToString() {
        return "{" + one + ", " + two + ", " + three + ", " + four + ", " + five + "}";
    }

    // CONSTRUCTORS

    public FiveNull(Object one, Object two, Object three, Object four, Object five) {
        this.one = one;
        this.two = two;
        this.three = three;
        this.four = four;
        this.five = five;
    }

    // PRIVATE

    private final Object one;
    private final Object two;
    private final Object three;
    private final Object four;
    private final Object five;

}
