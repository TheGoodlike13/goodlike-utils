package eu.goodlike.libraries.jooq;

import eu.goodlike.neat.Null;
import org.jooq.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.jooq.impl.DSL.trueCondition;

/**
 * Constructs queries for a table; all queries automatically use universal condition if it exists
 * @param <ID> type of primary key
 */
public interface Queries<R extends Record, ID> extends UniversalSql {

    /**
     * @return record with given primary key value, mapped using mapper
     * @throws NullPointerException if id or mapper is null
     */
    <T> Optional<T> read(ID id, Function<? super R, T> mapper);

    /**
     * @return all records for a given condition, mapped using mapper;
     * the records are returned in pages, that is, starting from (page * amount), an "amount" of them is returned
     * @throws NullPointerException if condition or mapper is null
     */
    <T> List<T> read(int page, int amount, Condition condition, RecordMapper<? super R, T> mapper);

    /**
     * @return all records, mapped using mapper;
     * the records are returned in pages, that is, starting from (page * amount), an "amount" of them is returned
     * @throws NullPointerException if mapper is null
     */
    default <T> List<T> readAll(int page, int amount, RecordMapper<? super R, T> mapper) {
        return read(page, amount, trueCondition(), mapper);
    }

    /**
     * @return all records for which given field matches given value (null value allowed), mapped using mapper;
     * the records are returned in pages, that is, starting from (page * amount), an "amount" of them is returned
     * @throws NullPointerException if field or mapper is null
     */
    default <T, F> List<T> read(int page, int amount, TableField<R, F> field, F fieldValue, RecordMapper<? super R, T> mapper) {
        return read(page, amount, eq(field, fieldValue), mapper);
    }

    /**
     * @return all records from a custom join statement, mapped using mapper; only this table's fields are returned;
     * the records are returned in pages, that is, starting from (page * amount), an "amount" of them is returned
     * @throws NullPointerException if condition, field or mapper or join is null
     */
    <T> List<T> readJoin(int page, int amount, Condition condition, RecordMapper<Record, T> mapper,
                         Function<SelectJoinStep<Record>, SelectWhereStep<Record>> join);

    /**
     * @return field value for any record that matches the condition; use unique keys in the condition
     * for the result to be deterministic!
     * @throws NullPointerException if field or condition is null
     */
    <T> Optional<T> readField(TableField<R, T> field, Condition condition);

    /**
     * @return field value for a record with given primary key value
     * @throws NullPointerException if field is null
     */
    default <T> Optional<T> readField(TableField<R, T> field, ID id) {
        return readField(field, primary(id));
    }

    /**
     * @return result of querying the database for multiple fields for any record that matches the condition;
     * only a single record is returned; the result will always fetch values from the same record;
     * use unique keys in the condition for the result to be deterministic!
     * @throws NullPointerException if condition is null or fields is or contains null
     * @throws IllegalArgumentException if fields is empty
     */
    Result<Record> readFields(Collection<TableField<R, ?>> fields, Condition condition);

    /**
     * @return result of querying the database for multiple fields for a record with given primary key value
     * @throws NullPointerException if id is null or fields is or contains null
     * @throws IllegalArgumentException if fields is empty
     */
    default Result<Record> readFields(Collection<TableField<R, ?>> fields, ID id) {
        return readFields(fields, primary(id));
    }

    /**
     * @return amount of records for a given condition
     * @throws NullPointerException if condition is null
     */
    int count(Condition condition);

    /**
     * @return total amount of all records
     */
    default int countAll() {
        return count(trueCondition());
    }

    /**
     * @return amount of records for which given field matches given value (null value allowed)
     * @throws NullPointerException if field is null
     */
    default <F> int count(TableField<R, F> field, F fieldValue) {
        return count(eq(field, fieldValue));
    }

    /**
     * @return true if any record exists for given condition, false otherwise
     * @throws NullPointerException if condition is null
     */
    boolean exists(Condition condition);

    /**
     * @return true if any record exists with given primary key value, false otherwise
     * @throws NullPointerException if id is null
     */
    default boolean exists(ID id) {
        return exists(primary(id));
    }

    /**
     * @return true if any record exists for which given field matches given value (null value allowed), false otherwise
     * @throws NullPointerException if field is null
     */
    default <F> boolean exists(TableField<R, F> field, F fieldValue) {
        return exists(eq(field, fieldValue));
    }

    /**
     * @return condition which asserts that primary key field is equal to id
     * @throws NullPointerException if id is null
     */
    Condition primary(ID id);

    /**
     * @return condition which asserts that given field is equal to given value (null value allowed)
     * @throws NullPointerException if field is null
     */
    default <F> Condition eq(TableField<R, F> field, F fieldValue) {
        Null.check(field).ifAny("Field cannot be null");
        return fieldValue == null ? field.isNull() : field.eq(fieldValue);
    }

}
