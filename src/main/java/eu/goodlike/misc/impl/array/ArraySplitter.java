package eu.goodlike.misc.impl.array;

import eu.goodlike.misc.ArrayUtils;
import eu.goodlike.neat.Null;

import java.util.Arrays;
import java.util.Optional;

/**
 * Splits arrays into elements/sub-arrays
 */
public final class ArraySplitter<T> {

    /**
     * @return element at index, Optional::empty if the array does not have the element at given index or the element
     * was null
     */
    public Optional<T> getElementAt(int index) {
        return index < 0 || index >= array.length
                ? Optional.empty()
                : Optional.ofNullable(array[index]);
    }

    /**
     * @return first element of array, Optional::empty if the array is empty or the element was null
     */
    public Optional<T> getFirstElement() {
        return array.length > 0
                ? Optional.ofNullable(array[0])
                : Optional.empty();
    }

    /**
     * @return last element of array, Optional::empty if the array is empty or the element was null
     */
    public Optional<T> getLastElement() {
        return array.length > 0
                ? Optional.ofNullable(array[array.length - 1])
                : Optional.empty();
    }

    /**
     * @return array including all elements after given index; will return underlying array if index is too small and
     * empty array if index is too large
     */
    public T[] getElementsAfter(int index) {
        if (index < 0)
            return array;

        int subArraySize = array.length - index - 1;
        if (subArraySize <= 0)
            return ArrayUtils.emptyArray();

        T[] subArray = ArrayUtils.newArrayOfSize(subArraySize);
        System.arraycopy(array, index + 1, subArray, 0, subArraySize);
        return subArray;
    }

    /**
     * @return array including all elements before given index; will return underlying array if index is too large and
     * empty array if index is too small
     */
    public T[] getElementsBefore(int index) {
        if (index >= array.length)
            return array;

        if (index <= 0)
            return ArrayUtils.emptyArray();

        T[] subArray = ArrayUtils.newArrayOfSize(index);
        System.arraycopy(array, 0, subArray, 0, index);
        return subArray;
    }

    /**
     * @return array including all elements between given indexes; if start is too small, will return the same as
     * getElementsBefore(end); if end is too large, will return the same as getElementsAfter(start); if the range
     * between start and end is 0 or negative, empty array will be returned
     */
    public T[] getElementsBetween(int start, int end) {
        if (start < 0)
            return getElementsBefore(end);

        if (end >= array.length)
            return getElementsAfter(start);

        int subArraySize = end - start - 1;
        if (subArraySize <= 0)
            return ArrayUtils.emptyArray();

        T[] subArray = ArrayUtils.newArrayOfSize(subArraySize);
        System.arraycopy(array, start + 1, subArray, 0, subArraySize);
        return subArray;
    }

    /**
     * The given array will be SORTED in ascending order in order to use binarySearch
     * @return array including all elements except the ones in given indexes; duplicate or out of range indexes are
     * ignored
     * @throws NullPointerException if indexes is null
     */
    public T[] getElementsWithout(int... indexes) {
        Null.checkAlone(indexes).ifAny("Indexes cannot be null");
        Arrays.sort(indexes);

        int validIndexCount = (int) Arrays.stream(indexes)
                .filter(index -> index >= 0)
                .filter(index -> index < array.length)
                .distinct()
                .count();

        if (validIndexCount == 0)
            return array;

        int subArraySize = array.length - validIndexCount;
        if (subArraySize <= 0)
            return ArrayUtils.emptyArray();

        T[] subArray = ArrayUtils.newArrayOfSize(subArraySize);
        int subArrayIndex = 0;
        for (int i = 0; i < array.length; i++)
            if (Arrays.binarySearch(indexes, i) < 0)
                subArray[subArrayIndex++] = array[i];

        return subArray;
    }

    /**
     * @return array including all elements after the first; this array is empty if the underlying array is empty or
     * only has one element
     */
    public T[] getElementsAfterFirst() {
        return getElementsAfter(0);
    }

    /**
     * @return array including all elements after given index and the element at index as well; will return underlying
     * array if index is too small and empty array if index is too large
     */
    public T[] getElementAfterInclusive(int index) {
        return getElementsAfter(index - 1);
    }

    /**
     * @return array including all elements before the last; this array is empty if the underlying array is empty or
     * only has one element
     */
    public T[] getElementsBeforeLast() {
        return getElementsBefore(array.length - 1);
    }

    /**
     * @return array including all elements before given index and the element at index as well; will return underlying
     * array if index is too large and empty array if index is too small
     */
    public T[] getElementsBeforeInclusive(int index) {
        return getElementsBefore(index + 1);
    }

    /**
     * @return array including all elements between given indexes and the elements at indexes as well; if start is too
     * small, will return the same as getElementsBefore(end); if end is too large, will return the same as
     * getElementsAfter(start); if the range between start and end is 0 or negative, empty array will be returned
     */
    public T[] getElementsBetweenInclusive(int start, int end) {
        return getElementsBetween(start - 1, end + 1);
    }

    /**
     * @return array including all elements between given indexes and the element at start index as well; if start is
     * too small, will return the same as getElementsBefore(end); if end is too large, will return the same as
     * getElementsAfter(start); if the range between start and end is 0 or negative, empty array will be returned
     */
    public T[] getElementsBetweenLeftInclusive(int start, int end) {
        return getElementsBetween(start - 1, end);
    }

    /**
     * @return array including all elements between given indexes and the element at end index as well; if start is
     * too small, will return the same as getElementsBefore(end); if end is too large, will return the same as
     * getElementsAfter(start); if the range between start and end is 0 or negative, empty array will be returned
     */
    public T[] getElementsBetweenRightInclusive(int start, int end) {
        return getElementsBetween(start, end + 1);
    }

    // CONSTRUCTORS

    public ArraySplitter(T[] array) {
        this.array = array;
    }

    // PRIVATE

    private final T[] array;

}
