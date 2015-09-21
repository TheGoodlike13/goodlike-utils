package eu.goodlike.libraries.jooq;

import eu.goodlike.neat.Null;
import org.jooq.*;

import java.util.Collection;
import java.util.function.Supplier;

public final class CommandsManyImpl<R extends Record, Left, Right>
        extends SQL implements CommandsMany<Left, Right> {

    @Override
    public void setUniversalCondition(Supplier<Condition> universalCondition) {
        throw new UnsupportedOperationException("Many-to-many tables are intended to be simple, without " +
                "any overhead; write your own code for complex scenarios!");
    }

    @Override
    public int create(Collection<Left> leftValues, Collection<Right> rightValues) {
        Null.checkCollection(leftValues).ifAny("Left values cannot be null");
        if (leftValues.isEmpty()) throw new IllegalArgumentException("At least one value should be provided");
        Null.checkCollection(rightValues).ifAny("Right values cannot be null");
        if (rightValues.isEmpty()) throw new IllegalArgumentException("At least one value should be provided");

        InsertValuesStep2<R, Left, Right> step = sql.insertInto(table, leftField, rightField);
        for (Left left : leftValues)
            for (Right right : rightValues)
                step = step.values(left, right);

        return step.execute();
    }

    @Override
    public int deleteLeft(Collection<Left> leftValues) {
        Null.checkCollection(leftValues).ifAny("Left values cannot be null");
        if (leftValues.isEmpty()) throw new IllegalArgumentException("At least one value should be provided");
        return sql.deleteFrom(table)
                .where(condition(leftValues, leftField))
                .execute();
    }

    @Override
    public int deleteRight(Collection<Right> rightValues) {
        Null.checkCollection(rightValues).ifAny("Right values cannot be null");
        if (rightValues.isEmpty()) throw new IllegalArgumentException("At least one value should be provided");
        return sql.deleteFrom(table)
                .where(condition(rightValues, rightField))
                .execute();
    }

    // CONSTRUCTORS

    public CommandsManyImpl(DSLContext sql, Table<R> table, TableField<R, Left> leftField, TableField<R, Right> rightField) {
        Null.check(sql, table, leftField, rightField).ifAny("DSLContext, table, leftField and rightField cannot be null");
        this.sql = sql;
        this.table = table;
        this.leftField = leftField;
        this.rightField = rightField;
    }

    // PRIVATE

    private final DSLContext sql;
    private final Table<R> table;
    private final TableField<R, Left> leftField;
    private final TableField<R, Right> rightField;

}
