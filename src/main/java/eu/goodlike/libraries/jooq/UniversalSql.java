package eu.goodlike.libraries.jooq;

import org.jooq.Condition;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * <pre>
 * Allows defining a universal condition, constructed on a per-execution basis
 *
 * This is useful when some field(s) need to be checked in every query, but the values are dependant on the execution
 * </pre>
 */
public interface UniversalSql {

    /**
     * Sets the method to retrieve the value of an universal condition, when needed
     */
    void setUniversalCondition(Supplier<Condition> universalCondition);

    /**
     * @return universal condition if it was set, empty otherwise
     */
    Optional<Condition> getUniversalCondition();

}
