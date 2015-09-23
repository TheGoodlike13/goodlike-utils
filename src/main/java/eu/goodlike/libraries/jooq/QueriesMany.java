package eu.goodlike.libraries.jooq;

import java.util.Collection;

/**
 * Constructs queries for many-to-many tables, where left and right are identifiers for the two connected
 * relationships
 */
public interface QueriesMany<Left, Right> {

    /**
     * @return true if a record with left value exists; false otherwise
     * @throws NullPointerException if value is null
     */
    boolean existsLeft(Left value);

    /**
     * @return true if a record for every left value exists; false otherwise
     * @throws NullPointerException if values is null or contains a null
     * @throws IllegalArgumentException if values is empty
     */
    boolean existsLeft(Collection<Left> values);

    /**
     * @return true if a record with right value exists; false otherwise
     * @throws NullPointerException if value is null
     */
    boolean existsRight(Right value);

    /**
     * @return true if a record for every right value exists; false otherwise
     * @throws NullPointerException if values is null or contains a null
     * @throws IllegalArgumentException if values is empty
     */
    boolean existsRight(Collection<Right> values);

}
