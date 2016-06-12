package eu.goodlike.validate;

import eu.goodlike.neat.Null;

import java.util.function.Predicate;

/**
 * Validator extension for Objects that implement Comparable interface
 */
public abstract class ComparableValidator<T extends Comparable<T>, V extends ComparableValidator<T, V>> extends Validator<T, V> {

    /**
     * Adds a predicate which tests if the comparable being validated is equal based on its comparison
     */
    public final V isEqualComparably(T other) {
        Null.check(other).ifAny("Other comparable cannot be null");
        return registerCondition(t -> t.compareTo(other) == 0);
    }

    /**
     * Adds a predicate which tests if the comparable being validated is less than given comparable
     * based on their comparison
     */
    public final V isLessThan(T other) {
        Null.check(other).ifAny("Other comparable cannot be null");
        return registerCondition(t -> t.compareTo(other) < 0);
    }

    /**
     * Adds a predicate which tests if the comparable being validated is more than given comparable
     * based on their comparison
     */
    public final V isMoreThan(T other) {
        Null.check(other).ifAny("Other comparable cannot be null");
        return registerCondition(t -> t.compareTo(other) > 0);
    }

    /**
     * Adds a predicate which tests if the comparable being validated is at least as big given comparable
     * based on their comparison
     */
    public final V isAtLeast(T other) {
        Null.check(other).ifAny("Other comparable cannot be null");
        return registerCondition(t -> t.compareTo(other) >= 0);
    }

    /**
     * Adds a predicate which tests if the comparable being validated is at most as big given comparable
     * based on their comparison
     */
    public final V isAtMost(T other) {
        Null.check(other).ifAny("Other comparable cannot be null");
        return registerCondition(t -> t.compareTo(other) <= 0);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the comparable being validated is between given comparables
     * based on their comparison; the comparison is inclusive for both comparables
     *
     * This will always fail if right is less than left
     * </pre>
     */
    public final V isBetween(T left, T right) {
        Null.check(left, right).ifAny("Left and right comparable cannot be null");
        return registerConditions(t -> t.compareTo(left) >= 0, t -> t.compareTo(right) <= 0);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the comparable being validated is between given comparables
     * based on their comparison; the comparison is exclusive for both comparables
     *
     * This will always fail if right is less than left
     * </pre>
     */
    public final V isBetweenExclusive(T left, T right) {
        Null.check(left, right).ifAny("Left and right comparable cannot be null");
        return registerConditions(t -> t.compareTo(left) > 0, t -> t.compareTo(right) < 0);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the comparable being validated is between given comparables
     * based on their comparison; the comparison is exclusive for left and inclusive for right comparable
     *
     * This will always fail if right is less than left
     * </pre>
     */
    public final V isBetweenExclusiveLeft(T left, T right) {
        Null.check(left, right).ifAny("Left and right comparable cannot be null");
        return registerConditions(t -> t.compareTo(left) > 0, t -> t.compareTo(right) <= 0);
    }

    /**
     * <pre>
     * Adds a predicate which tests if the comparable being validated is between given comparables
     * based on their comparison; the comparison is inclusive for left and exclusive for right comparable
     *
     * This will always fail if right is less than left
     * </pre>
     */
    public final V isBetweenExclusiveRight(T left, T right) {
        Null.check(left, right).ifAny("Left and right comparable cannot be null");
        return registerConditions(t -> t.compareTo(left) >= 0, t -> t.compareTo(right) < 0);
    }

    // CONSTRUCTORS

    protected ComparableValidator() {
        super();
    }

    protected ComparableValidator(Predicate<T> mainCondition, Predicate<T> accumulatedCondition, boolean negateNext) {
        super(mainCondition, accumulatedCondition, negateNext);
    }

}
