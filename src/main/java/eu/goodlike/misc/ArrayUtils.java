package eu.goodlike.misc;

import eu.goodlike.misc.impl.array.ArraySplitter;
import eu.goodlike.neat.Null;

/**
 * Utilities to deal with arrays
 */
public final class ArrayUtils {

    /**
     * @return array splitter, which can return arbitrary elements or sub-arrays
     * @throws NullPointerException if array is null
     */
    public static <T> ArraySplitter<T> split(T[] array) {
        Null.checkAlone(array).ifAny("Array cannot be null");
        return new ArraySplitter<>(array);
    }

    /**
     * The returned array is an Object array cast to T[], but since the underlying array is not used anywhere else,
     * it should be safe
     * @return new generic array
     */
    public static <T> T[] newArrayOfSize(int size) {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) new Object[size];
        return array;
    }

    /**
     * The returned array is an Object array cast to T[], but since the underlying array is not used anywhere else,
     * it should be safe
     * @return empty array
     */
    public static <T> T[] emptyArray() {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) EMPTY_ARRAY;
        return array;
    }

    /**
     * @return empty int array, effectively equivalent to new int[0]
     */
    public static int[] emptyIntArray() {
        return EMPTY_INT_ARRAY;
    }

    /**
     * @return array containing ints from start to end, inclusive; empty array is returned for empty/invalid ranges
     */
    public static int[] range(int start, int end) {
        int arraySize = end - start + 1;
        if (arraySize <= 0)
            return ArrayUtils.emptyIntArray();

        int[] array = new int[arraySize];
        for (int i = 0; i < arraySize; i++)
            array[i] = start + i;

        return array;
    }

    /**
     * @return array containing ints from start to end, exclusive; empty array is returned for empty/invalid ranges
     */
    public static int[] rangeExclusive(int start, int end) {
        return range(start + 1, end - 1);
    }

    /**
     * @return array containing ints from start to end, excluding start; empty array is returned for empty/invalid
     * ranges
     */
    public static int[] rangeLeftExclusive(int start, int end) {
        return range(start + 1, end);
    }

    /**
     * @return array containing ints from start to end, excluding end; empty array is returned for empty/invalid ranges
     */
    public static int[] rangeRightExclusive(int start, int end) {
        return range(start, end - 1);
    }

    // PRIVATE

    private ArrayUtils() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

    private static final Object[] EMPTY_ARRAY = {};
    private static final int[] EMPTY_INT_ARRAY = {};

}
