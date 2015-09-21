package eu.goodlike.misc;

import java.math.BigDecimal;

/**
 * This class is intended solely for equals() and hashCode() BigDecimal wrappers
 */
public final class Scaleless {

    @Override
    public boolean equals(Object o) {
        return this == o
                || o instanceof Scaleless
                && SpecialUtils.equalsJavaMathBigDecimal(this.bigDecimal, ((Scaleless) o).bigDecimal);
    }

    @Override
    public int hashCode() {
        return SpecialUtils.bigDecimalCustomHashCode(bigDecimal);
    }

    // CONSTRUCTORS

    public static Scaleless bigDecimal(BigDecimal bigDecimal) {
        return new Scaleless(bigDecimal);
    }

    private Scaleless(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    // PRIVATE

    private final BigDecimal bigDecimal;


}
