package eu.goodlike.resty.misc;

import com.google.common.base.MoreObjects;
import eu.goodlike.misc.SpecialUtils;
import eu.goodlike.neat.Null;

import java.util.Objects;

import static eu.goodlike.misc.Constants.NOT_NULL_NOT_BLANK;

/**
 * Represents a path variable and its value; used in varargs methods
 */
public final class PathVar {

    /**
     * @return the name of this path variable; for example ":id"
     */
    public String name() {
        return name;
    }

    /**
     * @return the value of this path variable; for example "1"
     */
    public String value() {
        return value;
    }

    /**
     * @throws IllegalArgumentException if given name is null or blank, both of which are invalid names
     */
    public static void validateName(String name) {
        NOT_NULL_NOT_BLANK.ifInvalidThrow(name, PathVar::emptyNameMessage);
    }

    /**
     * @throws IllegalArgumentException if given value is null or blank, both of which are invalid values
     */
    public static void validateValue(String value) {
        NOT_NULL_NOT_BLANK.ifInvalidThrow(value, PathVar::emptyValueMessage);
    }

    // CONSTRUCTORS

    /**
     * <pre>
     * Static import version: pathVar(name, value);
     *
     * Prepends ":" to the start of the name if it is missing
     *
     * Immediately encodes the value using String.valueOf() and then URLEncoder.encode() with "UTF-8"
     * </pre>
     * @throws NullPointerException if name or value is null
     * @throws IllegalArgumentException if name or encoded value is blank
     */
    public static PathVar pathVar(String name, Object value) {
        return of(name, value);
    }

    /**
     * <pre>
     * No static import version: PathVar.of(name, value);
     *
     * Prepends ":" to the start of the name if it is missing
     *
     * Immediately encodes the value using String.valueOf() and then URLEncoder.encode() with "UTF-8"
     * </pre>
     * @throws NullPointerException if name or value is null
     * @throws IllegalArgumentException if name or encoded value is blank
     */
    public static PathVar of(String name, Object value) {
        return new PathVar(name, value);
    }

    /**
     * <pre>
     * Constructor version: new PathVar(name, value);
     *
     * Prepends ":" to the start of the name if it is missing
     *
     * Immediately encodes the value using String.valueOf() and then URLEncoder.encode() with "UTF-8"
     * </pre>
     * @throws NullPointerException if name or value is null
     * @throws IllegalArgumentException if name or encoded value is blank
     */
    public PathVar(String name, Object value) {
        Null.check(name, value).ifAny("Name and value cannot be null");
        validateName(name);
        this.name = name.startsWith(":") ? name : ":" + name;
        this.value = SpecialUtils.urlEncode(value);
        validateValue(this.value());
    }

    // PRIVATE

    private final String name;
    private final String value;

    private static IllegalArgumentException emptyNameMessage() {
        return new IllegalArgumentException("Path variable name cannot be null or blank");
    }

    private static IllegalArgumentException emptyValueMessage() {
        return new IllegalArgumentException("Path variable value cannot be null or blank");
    }

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("value", value)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PathVar)) return false;
        PathVar pathVar = (PathVar) o;
        return Objects.equals(name, pathVar.name) &&
                Objects.equals(value, pathVar.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

}
