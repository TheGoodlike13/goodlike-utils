package eu.goodlike.neat.impl.nullcheck;

import eu.goodlike.neat.Null;

import java.util.Arrays;

/**
 * Null implementation for varargs check; different from {@link ArrayNull} in the sense, that it handles not a real
 * array, but instead a bunch of elements which then will have their names passed separately to the check
 */
public final class VarargsNull extends Null {

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

    public VarargsNull(Object... array) {
        this.array = array;
    }

    // PRIVATE

    private final Object[] array;

}
