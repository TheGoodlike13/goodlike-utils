package eu.goodlike.neat;

/**
 * <pre>
 * An interface that allows simple comparisons using some comparable class
 *
 * In general, intended to be used in scenarios where a class has a Comparable field which can be used to
 * describe that class; i.e. an enum field describing some property;
 *
 * Particular use case:
 *      User user has a UserStatus, which is an ordered enum;
 *      this allows the following statements:
 *          if (user.isAtLeast(ADMIN))
 *          if (user.isExactly(BANNED))
 *      etc;
 * </pre>
 */
public interface ComparableUsing<T extends Comparable<T>> extends Comparable<T> {

    /**
     * @return this.t == other
     */
    default boolean isExactly(T other) {
        return this.compareTo(other) == 0;
    }

    /**
     * @return this.t >= other
     */
    default boolean isAtLeast(T other) {
        return this.compareTo(other) >= 0;
    }

    /**
     * @return this.t <= other
     */
    default boolean isAtMost(T other) {
        return this.compareTo(other) <= 0;
    }

    /**
     * @return this.t < other
     */
    default boolean isLessThan(T other) {
        return this.compareTo(other) < 0;
    }

    /**
     * @return this.t > other
     */
    default boolean isMoreThan(T other) {
        return this.compareTo(other) > 0;
    }

}
