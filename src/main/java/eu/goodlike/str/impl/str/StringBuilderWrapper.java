package eu.goodlike.str.impl.str;

import eu.goodlike.misc.SpecialUtils;
import eu.goodlike.neat.Null;

import java.util.Collection;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * <pre>
 * Wraps StringBuilder in a convenient way
 *
 * Use Str::of to create instances
 *
 * In general, if you feel that you need to explicitly define a variable of StringBuilderWrapper, you might as well
 * just use StringBuilder in the first place; the wrapper is intended to take care of simple cases only
 * </pre>
 */
public final class StringBuilderWrapper {

    /**
     * Appends all the objects, one after another, to the wrapped StringBuilder
     * @throws NullPointerException if object array is null (NOT if it contains null, that is allowed)
     */
    public StringBuilderWrapper and(Object... objects) {
        Null.checkAlone(objects).ifAny("Object array cannot be null");
        for (Object o : objects)
            builder.append(o);
        return this;
    }

    /**
     * Appends all the objects in the collection, one after another, to the wrapped StringBuilder
     * @throws NullPointerException if collection is null (NOT if it contains null, that is allowed)
     */
    public StringBuilderWrapper andSome(Collection<?> collection) {
        Null.check(collection).ifAny("Collection cannot be null");
        collection.forEach(builder::append);
        return this;
    }

    /**
     * <pre>
     * Appends all the objects in the collection to the wrapped StringBuilder
     * Every object will have a prefix appended before it (can be null)
     * </pre>
     * @throws NullPointerException if collection is null (NOT if it contains null, that is allowed)
     */
    public StringBuilderWrapper andSome(Object prefix, Collection<?> collection) {
        Null.check(collection).ifAny("Collection cannot be null");
        collection.forEach(o -> builder.append(prefix).append(o));
        return this;
    }

    /**
     * <pre>
     * Appends all the objects in the collection to the wrapped StringBuilder
     * Every object will have a suffix appended after it (can be null)
     * </pre>
     * @throws NullPointerException if collection is null (NOT if it contains null, that is allowed)
     */
    public StringBuilderWrapper andSome(Collection<?> collection, Object suffix) {
        Null.check(collection).ifAny("Collection cannot be null");
        collection.forEach(o -> builder.append(o).append(suffix));
        return this;
    }

    /**
     * <pre>
     * Appends all the objects in the collection to the wrapped StringBuilder
     * Every object will have a prefix and suffix appended before and after it (can be null)
     * </pre>
     * @throws NullPointerException if collection is null (NOT if it contains null, that is allowed)
     */
    public StringBuilderWrapper andSome(Object prefix, Collection<?> collection, Object suffix) {
        Null.check(collection).ifAny("Collection cannot be null");
        collection.forEach(o -> builder.append(prefix).append(o).append(suffix));
        return this;
    }

    /**
     * <pre>
     * Appends all the objects in the collection to the wrapped StringBuilder
     * Uses a custom consumer to specify how to append the elements of the collection
     * </pre>
     * @throws NullPointerException if customAppender or collection is null (NOT if it contains null, that is allowed)
     */
    public <T> StringBuilderWrapper andSome(Collection<T> collection, BiConsumer<StringBuilder, T> customAppender) {
        Null.check(collection, customAppender).ifAny("Collection and customAppender cannot be null");
        for (T t : collection)
            customAppender.accept(builder, t);
        return this;
    }

    /**
     * Appends all the objects, one after another, to the wrapped StringBuilder, but only if condition is true
     * @throws NullPointerException if object array is null (NOT if it contains null, that is allowed); ONLY IF CONDITION IS TRUE
     */
    public StringBuilderWrapper andIf(boolean condition, Object... objects) {
        return condition ? and(objects) : this;
    }

    /**
     * Appends all the objects in the collection, one after another, to the wrapped StringBuilder,
     * but only if condition is true
     * @throws NullPointerException if collection is null (NOT if it contains null, that is allowed); ONLY IF CONDITION IS TRUE
     */
    public StringBuilderWrapper andSomeIf(boolean condition, Collection<?> collection) {
        return condition ? andSome(collection) : this;
    }

    /**
     * <pre>
     * Appends all the objects in the collection to the wrapped StringBuilder, but only if condition is true
     * Every object will have a prefix appended before it (can be null)
     * </pre>
     * @throws NullPointerException if collection is null (NOT if it contains null, that is allowed); ONLY IF CONDITION IS TRUE
     */
    public StringBuilderWrapper andSomeIf(boolean condition, Object prefix, Collection<?> collection) {
        return condition ? andSome(prefix, collection) : this;
    }

    /**
     * <pre>
     * Appends all the objects in the collection to the wrapped StringBuilder, but only if condition is true
     * Every object will have a suffix appended after it (can be null)
     * </pre>
     * @throws NullPointerException if collection is null (NOT if it contains null, that is allowed); ONLY IF CONDITION IS TRUE
     */
    public StringBuilderWrapper andSomeIf(boolean condition, Collection<?> collection, Object suffix) {
        return condition ? andSome(collection, suffix) : this;
    }

    /**
     * <pre>
     * Appends all the objects in the collection to the wrapped StringBuilder, but only if condition is true
     * Every object will have a prefix and suffix appended before and after it (can be null)
     * </pre>
     * @throws NullPointerException if collection is null (NOT if it contains null, that is allowed); ONLY IF CONDITION IS TRUE
     */
    public StringBuilderWrapper andSomeIf(boolean condition, Object prefix, Collection<?> collection, Object suffix) {
        return condition ? andSome(prefix, collection, suffix) : this;
    }

    /**
     * <pre>
     * Appends all the objects in the collection to the wrapped StringBuilder, but only if condition is true
     * Every object will have a prefix and suffix appended before and after it (can be null)
     * </pre>
     * @throws NullPointerException if collection is null (NOT if it contains null, that is allowed); ONLY IF CONDITION IS TRUE
     */
    public <T> StringBuilderWrapper andSomeIf(boolean condition, Collection<T> collection, BiConsumer<StringBuilder, T> customAppender) {
        return condition ? andSome(collection, customAppender) : this;
    }

    /**
     * This is a terminal operation, in case you want to append additional objects in a custom way
     * @return the wrapped builder
     */
    public StringBuilder toBuilder() {
        return builder;
    }

    /**
     * This is a terminal operation, for when you are done appending
     * @return string of the wrapped builder
     */
    @Override
    public String toString() {
        return builder.toString();
    }

    // CONSTRUCTORS

    public StringBuilderWrapper() {
        this(new StringBuilder());
    }

    public StringBuilderWrapper(StringBuilder builder) {
        this.builder = builder;
    }

    // PRIVATE

    private final StringBuilder builder;

    // OBJECT OVERRIDES

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StringBuilderWrapper)) return false;
        StringBuilderWrapper that = (StringBuilderWrapper) o;
        return SpecialUtils.equals(builder, that.builder, StringBuilder::toString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(builder == null ? null : builder.toString());
    }

}
