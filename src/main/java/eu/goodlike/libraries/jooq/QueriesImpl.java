package eu.goodlike.libraries.jooq;

import eu.goodlike.neat.Null;
import org.jooq.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.jooq.impl.DSL.trueCondition;

public final class QueriesImpl<R extends Record, ID> extends SQL implements Queries<R, ID> {

    @Override
    public <T> Optional<T> read(ID id, Function<? super R, T> mapper) {
        Null.check(id, mapper).ifAny("Primary key and mapper cannot be null");
        return sql.selectFrom(table)
                .where(getUniversalCondition().orElse(trueCondition()).and(primary(id)))
                .fetch().stream().findAny()
                .map(mapper);
    }

    @Override
    public <T> List<T> read(int page, int amount, Condition condition, RecordMapper<? super R, T> mapper) {
        Null.check(condition, mapper).ifAny("Condition and mapper cannot be null");
        return sql.selectFrom(table)
                .where(getUniversalCondition().orElse(trueCondition()).and(condition))
                .orderBy(keyField)
                .limit(amount)
                .offset(page * amount)
                .fetch()
                .map(mapper);
    }

    @Override
    public <T> List<T> readJoin(int page, int amount, Condition condition, RecordMapper<Record, T> mapper,
                                Function<SelectJoinStep<Record>, SelectWhereStep<Record>> join) {
        Null.check(condition, mapper, join).ifAny("Condition, mapper and join cannot be null");
        return join.apply(sql.select(table.fields()).from(table))
                .where(getUniversalCondition().orElse(trueCondition()).and(condition))
                .groupBy(keyField)
                .orderBy(keyField)
                .limit(amount)
                .offset(page * amount)
                .fetch()
                .map(mapper);
    }

    @Override
    public <T> Optional<T> readField(TableField<R, T> field, Condition condition) {
        Null.check(field, condition).ifAny("Field and condition cannot be null");
        return sql.select(field)
                .from(table)
                .where(getUniversalCondition().orElse(trueCondition()).and(condition))
                .limit(1)
                .fetch().stream().findAny()
                .map(record -> record.getValue(field));
    }

    @Override
    public Result<Record> readFields(Collection<TableField<R, ?>> tableFields, Condition condition) {
        Null.check(condition).ifAny("Condition cannot be null");
        Null.checkCollection(tableFields).ifAny("Table fields cannot be null");
        if (tableFields.isEmpty()) throw new IllegalArgumentException("At least one table field should be provided");
        return sql.select(tableFields)
                .from(table)
                .where(getUniversalCondition().orElse(trueCondition()).and(condition))
                .limit(1)
                .fetch();
    }

    @Override
    public int count(Condition condition) {
        Null.check(condition).ifAny("Condition cannot be null");
        return sql.fetchCount(table, getUniversalCondition().orElse(trueCondition()).and(condition));
    }

    @Override
    public boolean exists(Condition condition) {
        Null.check(condition).ifAny("Condition cannot be null");
        return sql.fetchExists(table, getUniversalCondition().orElse(trueCondition()).and(condition));
    }

    @Override
    public Condition primary(ID id) {
        Null.check(id).ifAny("Primary key value cannot be null");
        return keyField.eq(id);
    }

    // CONSTRUCTORS

    public QueriesImpl(DSLContext sql, Table<R> table, TableField<R, ID> keyField) {
        Null.check(sql, table, keyField).ifAny("DSLContext, table and keyField cannot be null");
        this.sql = sql;
        this.table = table;
        this.keyField = keyField;
    }

    // PRIVATE

    private final DSLContext sql;
    private final Table<R> table;
    private final TableField<R, ID> keyField;

}
