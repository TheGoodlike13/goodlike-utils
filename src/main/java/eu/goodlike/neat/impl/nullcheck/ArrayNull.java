package eu.goodlike.neat.impl.nullcheck;

import eu.goodlike.neat.Null;

import java.util.Arrays;

/**
 * Null implementation for array check; different from {@link VarargsNull} in the sense, that it handles a real array,
 * not a bunch of elements, and only has one name for the entire array
 */
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

    @Override
    protected String genericErrorFormat() {
        return "Cannot contain null: {}";
    }

    // CONSTRUCTORS

    public ArrayNull(Object[] array) {
        this.array = array;
    }

    // PRIVATE

    private final Object[] array;

}
