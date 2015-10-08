package eu.goodlike.resty.misc;

import eu.goodlike.misc.SpecialUtils;
import eu.goodlike.neat.Null;
import eu.goodlike.validation.Validate;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents a parameter, as part of query string or response body; mostly used in varargs methods
 */
public final class Param {

    /**
     * @return the name of this param; for example "id"
     */
    public String name() {
        return name;
    }

    /**
     * @return optional of the value of this param or empty if it doesn't have one; for example "1"
     */
    public Optional<String> value() {
        return Optional.ofNullable(value);
    }

    /**
     * @return the value of this param or null if it doesn't have one; for example "1"
     */
    public String valueNullable() {
        return value;
    }

    /**
     * @return true if this param has a value, false otherwise
     */
    public boolean hasValue() {
        return value != null;
    }

    // CONSTRUCTORS

    /**
     * <pre>
     * Static import version: param(name, value);
     *
     * Immediately encodes the value using String.valueOf() and then URLEncoder.encode() with "UTF-8"
     * </pre>
     * @throws NullPointerException if name is null
     * @throws IllegalArgumentException if name is blank
     */
    public static Param param(String name, Object value) {
        return of(name, value);
    }

    /**
     * <pre>
     * No static import version: Param.of(name, value);
     *
     * Immediately encodes the value using String.valueOf() and then URLEncoder.encode() with "UTF-8"
     * </pre>
     * @throws NullPointerException if name is null
     * @throws IllegalArgumentException if name is blank
     */
    public static Param of(String name, Object value) {
        return new Param(name, value);
    }

    /**
     * <pre>
     * Constructor version: new Param(name, value);
     *
     * Immediately encodes the value using String.valueOf() and then URLEncoder.encode() with "UTF-8"
     * </pre>
     * @throws NullPointerException if name is null
     * @throws IllegalArgumentException if name is blank
     */
    public Param(String name, Object value) {
        Null.check(name).ifAny("Param name cannot be null");
        Validate.string(name).not().blank().ifInvalid(Param::emptyNameMessage);
        this.name = name;
        this.value = value == null ? null : SpecialUtils.urlEncode(value);
    }

    // PRIVATE

    private final String name;
    private final String value;

    private static IllegalArgumentException emptyNameMessage() {
        return new IllegalArgumentException("Param name cannot be blank");
    }

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return name + "=" + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Param)) return false;
        Param param = (Param) o;
        return Objects.equals(name, param.name) &&
                Objects.equals(value, param.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

}
