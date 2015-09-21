package eu.goodlike.libraries.jooq;

import eu.goodlike.neat.Null;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.TableField;

import java.util.Arrays;
import java.util.List;

import static org.jooq.impl.DSL.falseCondition;

public final class QueriesForeignImpl<ID> extends SQL implements QueriesForeign<ID> {

    @Override
    public boolean isUsed(ID id) {
        Null.check(id).ifAny("Foreign key value cannot be null");
        return sql.fetchExists(
                sql.selectOne()
                        .from(tables())
                        .where(condition(id)));
    }

    // CONSTRUCTORS

    @SafeVarargs
    public QueriesForeignImpl(DSLContext sql, TableField<?, ID>... foreignKeys) {
        Null.check(sql).ifAny("DSLContext cannot be null");
        if (foreignKeys.length <= 0) throw new IllegalArgumentException("At least one foreign key should be provided");

        this.sql = sql;
        this.foreignKeys = Arrays.asList(foreignKeys);
    }

    // PRIVATE

    private final DSLContext sql;
    private final List<TableField<?, ID>> foreignKeys;

    private Table[] tables() {
        return foreignKeys.stream()
                .map(TableField::getTable)
                .toArray(Table[]::new);
    }

    private Condition condition(ID id) {
        return foreignKeys.stream()
                .map(key -> key.eq(id))
                .reduce(getUniversalCondition().orElse(falseCondition()), Condition::or);
    }

}
