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
        @SuppressWarnings("unchecked")
        Predicate<T> alwaysTrue = (Predicate<T>) ALWAYS_TRUE;
        return alwaysTrue;
    }

    /**
     * @return predicate which always evaluates to true
     */
    public static DoublePredicate alwaysTrueForDouble() {
        return ALWAYS_TRUE_DOUBLE;
    }

    /**
     * @return predicate which always evaluates to true
     */
    public static IntPredicate alwaysTrueForInt() {
        return ALWAYS_TRUE_INT;
    }

    /**
     * @return predicate which always evaluates to true
     */
    public static LongPredicate alwaysTrueForLong() {
        return ALWAYS_TRUE_LONG;
    }

    /**
     * @return predicate which always evaluates to false
     */
    public static <T> Predicate<T> alwaysFalse() {
        @SuppressWarnings("unchecked")
        Predicate<T> alwaysFalse = (Predicate<T>) ALWAYS_FALSE;
        return alwaysFalse;
    }

    /**
     * @return predicate which always evaluates to false
     */
    public static DoublePredicate alwaysFalseForDouble() {
        return ALWAYS_FALSE_DOUBLE;
    }

    /**
     * @return predicate which always evaluates to false
     */
    public static IntPredicate alwaysFalseForInt() {
        return ALWAYS_FALSE_INT;
    }

    /**
     * @return predicate which always evaluates to false
     */
    public static LongPredicate alwaysFalseForLong() {
        return ALWAYS_FALSE_LONG;
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

    private static final Predicate<Object> ALWAYS_TRUE = new Predicate<Object>() {
        @Override
        public boolean test(Object o) {
            return true;
        }

        @Override
        public Predicate<Object> and(Predicate<? super Object> other) {
            return other;
        }

        @Override
        public Predicate<Object> negate() {
            return alwaysFalse();
        }

        @Override
        public Predicate<Object> or(Predicate<? super Object> other) {
            return this;
        }
    };

    private static final DoublePredicate ALWAYS_TRUE_DOUBLE = new DoublePredicate() {
        @Override
        public boolean test(double value) {
            return true;
        }

        @Override
        public DoublePredicate and(DoublePredicate other) {
            return other;
        }

        @Override
        public DoublePredicate negate() {
            return alwaysFalseForDouble();
        }

        @Override
        public DoublePredicate or(DoublePredicate other) {
            return this;
        }
    };

    private static final IntPredicate ALWAYS_TRUE_INT = new IntPredicate() {
        @Override
        public boolean test(int value) {
            return true;
        }

        @Override
        public IntPredicate and(IntPredicate other) {
            return other;
        }

        @Override
        public IntPredicate negate() {
            return alwaysFalseForInt();
        }

        @Override
        public IntPredicate or(IntPredicate other) {
            return this;
        }
    };

    private static final LongPredicate ALWAYS_TRUE_LONG = new LongPredicate() {
        @Override
        public boolean test(long value) {
            return true;
        }

        @Override
        public LongPredicate and(LongPredicate other) {
            return other;
        }

        @Override
        public LongPredicate negate() {
            return alwaysFalseForLong();
        }

        @Override
        public LongPredicate or(LongPredicate other) {
            return this;
        }
    };

    private static final Predicate<Object> ALWAYS_FALSE = new Predicate<Object>() {
        @Override
        public boolean test(Object o) {
            return false;
        }

        @Override
        public Predicate<Object> and(Predicate<? super Object> other) {
            return this;
        }

        @Override
        public Predicate<Object> negate() {
            return alwaysTrue();
        }

        @Override
        public Predicate<Object> or(Predicate<? super Object> other) {
            return other;
        }
    };

    private static final DoublePredicate ALWAYS_FALSE_DOUBLE = new DoublePredicate() {
        @Override
        public boolean test(double value) {
            return false;
        }

        @Override
        public DoublePredicate and(DoublePredicate other) {
            return this;
        }

        @Override
        public DoublePredicate negate() {
            return alwaysTrueForDouble();
        }

        @Override
        public DoublePredicate or(DoublePredicate other) {
            return other;
        }
    };

    private static final IntPredicate ALWAYS_FALSE_INT = new IntPredicate() {
        @Override
        public boolean test(int value) {
            return false;
        }

        @Override
        public IntPredicate and(IntPredicate other) {
            return this;
        }

        @Override
        public IntPredicate negate() {
            return alwaysTrueForInt();
        }

        @Override
        public IntPredicate or(IntPredicate other) {
            return other;
        }
    };

    private static final LongPredicate ALWAYS_FALSE_LONG = new LongPredicate() {
        @Override
        public boolean test(long value) {
            return false;
        }

        @Override
        public LongPredicate and(LongPredicate other) {
            return this;
        }

        @Override
        public LongPredicate negate() {
            return alwaysTrueForLong();
        }

        @Override
        public LongPredicate or(LongPredicate other) {
            return other;
        }
    };

}
