package eu.goodlike.libraries.jooq;

import java.util.Collection;
import java.util.Collections;

/**
 * Constructs commands for many-to-many tables, where left and right are identifiers for the two connected
 * relationships
 */
public interface CommandsMany<Left, Right> {

    /**
     * @return result of inserting the cartesian product of leftValues and rightValues in the table
     * @throws NullPointerException if either collection is or contains null
     * @throws IllegalArgumentException if either collection is empty
     */
    int create(Collection<Left> leftValues, Collection<Right> rightValues);

    /**
     * @return result of inserting the cartesian product of leftValue and rightValues in the table
     * @throws NullPointerException if either leftValue is null or rightValues is or contains null
     * @throws IllegalArgumentException if rightValues is empty
     */
    default int create(Left leftValue, Collection<Right> rightValues) {
        return create(Collections.singleton(leftValue), rightValues);
    }

    /**
     * @return result of inserting the cartesian product of leftValues and rightValue in the table
     * @throws NullPointerException if either rightValue is null or leftValues is or contains null
     * @throws IllegalArgumentException if leftValues is empty
     */
    default int create(Collection<Left> leftValues, Right rightValue) {
        return create(leftValues, Collections.singleton(rightValue));
    }

    /**
     * @return result of deleting records with given leftValues from the table
     * @throws NullPointerException if leftValues is or contains null
     * @throws IllegalArgumentException if leftValues is empty
     */
    int deleteLeft(Collection<Left> leftValues);

    /**
     * @return result of deleting records with given leftValue from the table
     * @throws NullPointerException if leftValue is null
     */
    default int deleteLeft(Left leftValue) {
        return deleteLeft(Collections.singleton(leftValue));
    }

    /**
     * @return result of deleting records with given rightValues from the table
     * @throws NullPointerException if rightValues is or contains null
     * @throws IllegalArgumentException if rightValues is empty
     */
    int deleteRight(Collection<Right> rightValues);

    /**
     * @return result of deleting records with given rightValue from the table
     * @throws NullPointerException if rightValue is null
     */
    default int deleteRight(Right rightValue) {
        return deleteRight(Collections.singleton(rightValue));
    }

    /**
     * Deletes all record with leftValue, then creates new records for leftValue and rightValues
     * @throws NullPointerException if either leftValue is null or rightValues is or contains null
     * @throws IllegalArgumentException if rightValues is empty
     */
    default int update(Left leftValue, Collection<Right> rightValues) {
        deleteLeft(leftValue);
        return create(leftValue, rightValues);
    }

    /**
     * Deletes all record with rightValue, then creates new records for rightValue and leftValues
     * @throws NullPointerException if either rightValue is null or leftValues is or contains null
     * @throws IllegalArgumentException if leftValues is empty
     */
    default int update(Collection<Left> leftValues, Right rightValue) {
        deleteRight(rightValue);
        return create(leftValues, rightValue);
    }

}
