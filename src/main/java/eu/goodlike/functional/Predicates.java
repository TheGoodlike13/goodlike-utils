package eu.goodlike.functional;

import eu.goodlike.neat.Null;

import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

/**
 * Utility methods to work with Predicates
 */
public final class Predicates {

    /**
     * @return predicate which always evaluates to true
     */
    public static <T> Predicate<T> alwaysTrue() {
        return any -> true;
    }

    /**
     * @return predicate which always evaluates to true
     */
    public static DoublePredicate alwaysTrueForDouble() {
        return any -> true;
    }

    /**
     * @return predicate which always evaluates to true
     */
    public static IntPredicate alwaysTrueForInt() {
        return any -> true;
    }

    /**
     * @return predicate which always evaluates to true
     */
    public static LongPredicate alwaysTrueForLong() {
        return any -> true;
    }

    /**
     * @return predicate which always evaluates to false
     */
    public static <T> Predicate<T> alwaysFalse() {
        return any -> false;
    }

    /**
     * @return predicate which always evaluates to false
     */
    public static DoublePredicate alwaysFalseForDouble() {
        return any -> false;
    }

    /**
     * @return predicate which always evaluates to false
     */
    public static IntPredicate alwaysFalseForInt() {
        return any -> false;
    }

    /**
     * @return predicate which always evaluates to false
     */
    public static LongPredicate alwaysFalseForLong() {
        return any -> false;
    }

    /**
     * @return all predicates, folded (left) using Predicate.and(); if the array is empty, returns alwaysTrue()
     * @throws NullPointerException if predicate array is or contains null
     */
    @SafeVarargs
    public static <T> Predicate<T> conjunction(Predicate<? super T>... predicates) {
        Null.checkArray(predicates).ifAny("Predicates cannot be null");

        Predicate<T> totalCondition = alwaysTrue();
        for (Predicate<? super T> nextCondition : predicates)
            totalCondition = totalCondition.and(nextCondition);

        return totalCondition;
    }

    /**
     * @return all predicates, folded (left) using Predicate.and(); if the array is empty, returns alwaysTrueForDouble()
     * @throws NullPointerException if predicate array is or contains null
     */
    public static DoublePredicate conjunction(DoublePredicate... predicates) {
        Null.checkArray(predicates).ifAny("Predicates cannot be null");

        DoublePredicate totalCondition = alwaysTrueForDouble();
        for (DoublePredicate nextCondition : predicates)
            totalCondition = totalCondition.and(nextCondition);

        return totalCondition;
    }

    /**
     * @return all predicates, folded (left) using Predicate.and(); if the array is empty, returns alwaysTrueForInt()
     * @throws NullPointerException if predicate array is or contains null
     */
    public static IntPredicate conjunction(IntPredicate... predicates) {
        Null.checkArray(predicates).ifAny("Predicates cannot be null");

        IntPredicate totalCondition = alwaysTrueForInt();
        for (IntPredicate nextCondition : predicates)
            totalCondition = totalCondition.and(nextCondition);

        return totalCondition;
    }

    /**
     * @return all predicates, folded (left) using Predicate.and(); if the array is empty, returns alwaysTrueForLong()
     * @throws NullPointerException if predicate array is or contains null
     */
    public static LongPredicate conjunction(LongPredicate... predicates) {
        Null.checkArray(predicates).ifAny("Predicates cannot be null");

        LongPredicate totalCondition = alwaysTrueForLong();
        for (LongPredicate nextCondition : predicates)
            totalCondition = totalCondition.and(nextCondition);

        return totalCondition;
    }

    /**
     * @return all predicates, folded (left) using Predicate.or(); if the array is empty, returns alwaysFalse()
     * @throws NullPointerException if predicate array is or contains null
     */
    @SafeVarargs
    public static <T> Predicate<T> disjunction(Predicate<? super T>... predicates) {
        Null.checkArray(predicates).ifAny("Predicates cannot be null");

        Predicate<T> totalCondition = alwaysFalse();
        for (Predicate<? super T> nextCondition : predicates)
            totalCondition = totalCondition.or(nextCondition);

        return totalCondition;
    }

    /**
     * @return all predicates, folded (left) using Predicate.or(); if the array is empty, returns alwaysFalseForDouble()
     * @throws NullPointerException if predicate array is or contains null
     */
    public static DoublePredicate disjunction(DoublePredicate... predicates) {
        Null.checkArray(predicates).ifAny("Predicates cannot be null");

        DoublePredicate totalCondition = alwaysFalseForDouble();
        for (DoublePredicate nextCondition : predicates)
            totalCondition = totalCondition.or(nextCondition);

        return totalCondition;
    }

    /**
     * @return all predicates, folded (left) using Predicate.or(); if the array is empty, returns alwaysFalseForInt()
     * @throws NullPointerException if predicate array is or contains null
     */
    public static IntPredicate disjunction(IntPredicate... predicates) {
        Null.checkArray(predicates).ifAny("Predicates cannot be null");

        IntPredicate totalCondition = alwaysFalseForInt();
        for (IntPredicate nextCondition : predicates)
            totalCondition = totalCondition.or(nextCondition);

        return totalCondition;
    }

    /**
     * @return all predicates, folded (left) using Predicate.or(); if the array is empty, returns alwaysFalseForLong()
     * @throws NullPointerException if predicate array is or contains null
     */
    public static LongPredicate disjunction(LongPredicate... predicates) {
        Null.checkArray(predicates).ifAny("Predicates cannot be null");

        LongPredicate totalCondition = alwaysFalseForLong();
        for (LongPredicate nextCondition : predicates)
            totalCondition = totalCondition.or(nextCondition);

        return totalCondition;
    }

    /**
     * @return IntPredicate of a Integer predicate
     * @throws NullPointerException if predicate is null
     */
    public static IntPredicate forInts(Predicate<? super Integer> predicate) {
        Null.check(predicate).ifAny("Predicate cannot be null");
        return predicate::test;
    }

    /**
     * @return LongPredicate of a Long predicate
     * @throws NullPointerException if predicate is null
     */
    public static LongPredicate forLongs(Predicate<? super Long> predicate) {
        Null.check(predicate).ifAny("Predicate cannot be null");
        return predicate::test;
    }

    /**
     * @return DoublePredicate of a Double predicate
     * @throws NullPointerException if predicate is null
     */
    public static DoublePredicate forDoubles(Predicate<? super Double> predicate) {
        Null.check(predicate).ifAny("Predicate cannot be null");
        return predicate::test;
    }

    // PRIVATE

    private Predicates() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
