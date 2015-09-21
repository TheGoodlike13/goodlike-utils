package eu.goodlike.libraries.jooq;

import org.jooq.*;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

import static org.jooq.impl.DSL.falseCondition;

/**
 * <pre>
 * Contains factory methods to create Queries class instances
 *
 * These should be used with dependency injection, i.e. Spring @Config classes
 * </pre>
 */
public abstract class SQL {

    public void setUniversalCondition(Supplier<Condition> universalCondition) {
        this.universalCondition = universalCondition;
    }

    public Optional<Condition> getUniversalCondition() {
        return Optional.ofNullable(universalCondition).map(Supplier::get);
    }

    // CONSTRUCTORS

    /**
     * @return query maker for given table
     * @throws NullPointerException if any of the arguments are null
     */
    public static <R extends Record, ID> Queries<R, ID> queriesFor(DSLContext sql, Table<R> table,
                                                                   TableField<R, ID> keyField) {
        return new QueriesImpl<>(sql, table, keyField);
    }

    /**
     * @return many-to-many command maker for given fields
     * @throws NullPointerException if any of the arguments are null
     */
    public static <R extends Record, Left, Right> CommandsMany<Left, Right> commandsFor(DSLContext sql, Table<R> table,
                                                                                      TableField<R, Left> leftField,
                                                                                      TableField<R, Right> rightField) {
        return new CommandsManyImpl<>(sql, table, leftField, rightField);
    }

    /**
     * @return many-to-many query maker for given fields
     * @throws NullPointerException if any of the arguments are null
     */
    public static <R extends Record, Left, Right> QueriesMany<Left, Right> queriesFor(DSLContext sql, Table<R> table,
                                                                                      TableField<R, Left> leftField,
                                                                                      TableField<R, Right> rightField) {
        return new QueriesManyImpl<>(sql, table, leftField, rightField);
    }

    /**
     * @return foreign key query maker for given foreign keys
     * @throws NullPointerException if any of the arguments are null
     * @throws IllegalArgumentException if foreignKeys is empty
     */
    @SafeVarargs
    public static <ID> QueriesForeign<ID> queriesFor(DSLContext sql, TableField<?, ID>... foreignKeys) {
        return new QueriesForeignImpl<>(sql, foreignKeys);
    }

    // PROTECTED

    protected  <T> Condition condition(Collection<T> values, TableField<?, T> field) {
        return values.stream()
                .map(field::eq)
                .reduce(getUniversalCondition().orElse(falseCondition()), Condition::or);
    }

    // PRIVATE

    private volatile Supplier<Condition> universalCondition;

}
