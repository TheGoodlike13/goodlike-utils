package eu.goodlike.libraries.jooq;

import eu.goodlike.neat.Null;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.TableField;

import java.util.Collection;

public final class QueriesManyImpl<R extends Record, Left, Right>
        extends SQL implements QueriesMany<Left, Right> {

    @Override
    public final boolean existsLeft(Left value) {
        Null.check(value).ifAny("Left value cannot be null");
        return sql.fetchExists(table, leftField.eq(value));
    }

    @Override
    public boolean existsLeft(Collection<Left> values) {
        Null.checkCollection(values).ifAny("Left values cannot be null");
        if (values.isEmpty()) throw new IllegalArgumentException("At least one value should be provided");
        return distinctCount(values, leftField) == values.size();
    }

    @Override
    public final boolean existsRight(Right value) {
        Null.check(value).ifAny("Right value cannot be null");
        return sql.fetchExists(table, rightField.eq(value));
    }

    @Override
    public boolean existsRight(Collection<Right> values) {
        Null.checkCollection(values).ifAny("Right values cannot be null");
        if (values.isEmpty()) throw new IllegalArgumentException("At least one value should be provided");
        return distinctCount(values, rightField) == values.size();
    }

    // CONSTRUCTORS

    public QueriesManyImpl(DSLContext sql, Table<R> table, TableField<R, Left> leftField, TableField<R, Right> rightField) {
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

    private <T> int distinctCount(Collection<T> values, TableField<?, T> field) {
        return sql.fetchCount(sql.selectDistinct(field).from(table).where(condition(values, field)));
    }

}
