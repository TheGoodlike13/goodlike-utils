package eu.goodlike.neat.impl.nullcheck;

import eu.goodlike.neat.Null;

public final class TwoNull extends Null {

    @Override
    protected int indexOfNull() {
        return one == null
                ? 0
                : two == null
                ? 1
                : -1;
    }

    @Override
    protected String contentToString() {
        return "{" + one + ", " + two + "}";
    }

    // CONSTRUCTORS

    public TwoNull(Object one, Object two) {
        this.one = one;
        this.two = two;
    }

    // PRIVATE

    private final Object one;
    private final Object two;

}
