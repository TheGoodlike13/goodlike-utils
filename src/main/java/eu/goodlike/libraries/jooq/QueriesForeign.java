package eu.goodlike.libraries.jooq;

/**
 * Constructs queries which check foreign key constraints
 * @param <ID> type of foreign key
 */
public interface QueriesForeign<ID> extends UniversalSql {

    /**
     * @return true if a given value is used as a foreign key; false otherwise
     * @throws NullPointerException if id is null
     */
    boolean isUsed(ID id);

}
