package eu.goodlike.resty.misc;

import eu.goodlike.neat.Null;

import java.util.Objects;

import static eu.goodlike.misc.Constants.NOT_NULL_NOT_BLANK;

/**
 * Represents an HTTP header (for both response and request); used in varargs methods
 */
public final class Header {

    /**
     * @return the name of this header; for example "Authorization"
     */
    public String name() {
        return name;
    }

    /**
     * @return the value of this header; for example "Basic bmFrYSBuYWthIHlhcnUgamEgbmFpIGRlc3Uga2E="
     */
    public String value() {
        return value;
    }

    // CONSTRUCTORS

    /**
     * Static import version: header(name, value);
     * @throws NullPointerException if name or value is null
     * @throws IllegalArgumentException if name is blank
     */
    public static Header header(String name, String value) {
        return of(name, value);
    }

    /**
     * No static import version: Header.of(name, value);
     * @throws NullPointerException if name or value is null
     * @throws IllegalArgumentException if name is blank
     */
    public static Header of(String name, String value) {
        return new Header(name, value);
    }

    /**
     * Constructor version: new Header(name, value);
     * @throws NullPointerException if name or value is null
     * @throws IllegalArgumentException if name is blank
     */
    public Header(String name, String value) {
        Null.check(name, value).ifAny("Header name and value cannot be null");
        NOT_NULL_NOT_BLANK.ifInvalid(name, Header::emptyNameMessage);
        this.name = name;
        this.value = value;
    }

    // PRIVATE

    private final String name;
    private final String value;

    private static IllegalArgumentException emptyNameMessage() {
        return new IllegalArgumentException("Header name cannot be blank");
    }

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return name() + ": " + value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Header)) return false;
        Header header = (Header) o;
        return Objects.equals(name, header.name) &&
                Objects.equals(value, header.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

}
