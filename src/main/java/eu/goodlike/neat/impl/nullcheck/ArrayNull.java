package eu.goodlike.neat.impl.nullcheck;

import eu.goodlike.neat.Null;

import java.util.Arrays;

public final class ArrayNull extends Null {

    @Override
    protected int indexOfNull() {
        for (int i = 0; i < array.length; i++)
            if (array[i] == null)
                return i;

        return -1;
    }

    @Override
    protected String contentToString() {
        return Arrays.toString(array);
    }

    // CONSTRUCTORS

    public ArrayNull(Object... array) {
        this.array = array;
    }

    // PRIVATE

    private final Object[] array;

}
