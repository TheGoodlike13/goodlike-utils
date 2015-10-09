package eu.goodlike.misc;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;

/**
 * <pre>
 * Contains various utility methods, without association to any particular subject
 *
 * It was created to avoid multiple Utils classes just holding a single method, which doesn't easily translate
 * into a helper class
 * </pre>
 */
public final class SpecialUtils {

    /**
     * <pre>
     * This method is useful when BigDecimals can come from different sources, which may choose to trim the scale
     * of their BigDecimals; using normal equals() method would imply that 10.0000 != 10 != 10.00, etc
     * In this method, however, 10.0000 == 10 == 10.00, etc
     *
     * It is important to note, that if you use this method to compare two BigDecimals in an equals() method
     * of some class, you need to also use a customized hashCode() implementation; someone suggested to use
     * Double.valueOf(bigDecimal.doubleValue()).hashCode();
     * </pre>
     * @return true if the given BigDecimals are equal, ignoring scale; false otherwise
     */
    public static boolean equalsJavaMathBigDecimal(BigDecimal bigDecimal1, BigDecimal bigDecimal2) {
        return bigDecimal1 == null ? bigDecimal2 == null : bigDecimal1.compareTo(bigDecimal2) == 0;
    }

    /**
     * Custom hashCode() implementation, intended for use with equalsJavaMathBigDecimal
     * @return scale agnostic hashCode value for given bigDecimal
     */
    public static int bigDecimalCustomHashCode(BigDecimal bigDecimal) {
        return bigDecimal == null ? 0 : Double.valueOf(bigDecimal.doubleValue()).hashCode();
    }

    /**
     * @return -1 if i1 < i2; 0 if i1 == i2; 1 if i1 > i2; null is considered to be the LARGEST number in this case
     * which will cause any collection, which is sorted using this comparator to put null to the very end
     */
    public static int compareNullableIntegers(Integer i1, Integer i2) {
        return i1 == null
                        ? i2 == null ? 0 : 1
                        : i2 == null ? -1 : Integer.compare(i1, i2);
    }

    /**
     * @return URL encoded string representation of an object; String.valueOf() is used to convert
     * @throws IllegalStateException if UTF-8 is not supported somehow
     */
    public static String urlEncode(Object value) {
        try {
            return URLEncoder.encode(String.valueOf(value), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Please add support for UTF-8!", e);
        }
    }

    // PRIVATE

    private SpecialUtils() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
