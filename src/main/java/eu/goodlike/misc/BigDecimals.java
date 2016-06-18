package eu.goodlike.misc;

import java.math.BigDecimal;

/**
 * Contains utility methods to deal with BigDecimal class
 */
public final class BigDecimals {

    /**
     * <pre>
     * This method is useful when BigDecimals can come from different sources, which may choose different scale
     * for their BigDecimals; using normal equals() method would imply that 10.0000 != 10 != 10.00, etc
     * In this method, however, 10.0000 == 10 == 10.00, etc
     *
     * It is important to note, that if you use this method to compare two BigDecimals in an equals() method
     * of some class, you need to also use a customized hashCode() implementation; consider using BigDecimals.hashCode()
     * </pre>
     * @return true if the given BigDecimals are equal, ignoring scale; false otherwise
     */
    public static boolean equalsIgnoreScale(BigDecimal big1, BigDecimal big2) {
        return big1 == null ? big2 == null : big1.compareTo(big2) == 0;
    }

    /**
     * Custom hashCode() implementation, intended for use with BigDecimals.equalsIgnoreScale()
     * @return scale agnostic hashCode value for given bigDecimal
     */
    public static int hashCode(BigDecimal bigDecimal) {
        return bigDecimal == null ? 0 : Double.valueOf(bigDecimal.doubleValue()).hashCode();
    }

    /**
     * Custom hashCode() implementation, intended for use with BigDecimals.equalsIgnoreScale()
     * @return scale agnostic hashCode value for given bigDecimals
     */
    public static int hashCode(BigDecimal... bigDecimals) {
        if (bigDecimals == null)
            return 0;

        int result = 1;

        for (BigDecimal bigDecimal : bigDecimals)
            result = 31 * result + BigDecimals.hashCode(bigDecimal);

        return result;
    }

    // PRIVATE

    private BigDecimals() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
