package eu.goodlike.functional;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import eu.goodlike.misc.ReflectUtils;
import eu.goodlike.neat.Null;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * <pre>
 * Rudimentary case class implementation
 *
 * The matcher can create a builder, which requires that you explicitly provide a consumer for every case class defined
 * in this CaseMatcher. This builder is not synchronized
 *
 * The following limitations apply when using CaseMatcher:
 *      Cannot match interfaces or abstract classes
 *          In general, if you cannot have an instance of that exact class, you should not be able to match it
 *      Cannot have multiple matchers for the same case
 *      Cannot match extending classes which are not pre-defined in the CaseMatcher
 *      Cannot match objects of extending classes which are not pre-defined in the CaseMatcher
 *      There must always be at least one case to match
 * </pre>
 */
public final class CaseMatcher<CaseClass> {

    /**
     * @return builder for case classes, with the initial case set
     * @throws NullPointerException if caseClass or onMatchConsumer is null
     * @throws IllegalArgumentException if caseClass does not belong to this CaseMatcher
     */
    public <T extends CaseClass> Builder<CaseClass> onCase(Class<T> caseClass, Consumer<? super T> onMatchConsumer) {
        return new Builder<>(matchableClasses).onCase(caseClass, onMatchConsumer);
    }

    /**
     * @return builder for case classes, with the initial case set to ignore
     * @throws NullPointerException if caseClass is null
     * @throws IllegalArgumentException if caseClass does not belong to this CaseMatcher
     */
    public <T extends CaseClass> Builder<CaseClass> ignoreCase(Class<T> caseClass) {
        return new Builder<>(matchableClasses).ignoreCase(caseClass);
    }

    // CONSTRUCTORS

    /**
     * Constructor for CaseMatcher
     * @throws NullPointerException if caseClasses is or contains null
     * @throws IllegalArgumentException if caseClasses is empty
     * @throws IllegalStateException if any of the caseClasses if an interface or abstract class
     */
    @SafeVarargs
    public CaseMatcher(Class<? extends CaseClass>... caseClasses) {
        Null.checkArray(caseClasses).ifAny("Cannot be or contain null: caseClasses");

        if (caseClasses.length < 1)
            throw new IllegalArgumentException("At least one case must be provided");

        ensureClassesAreImplemented(caseClasses);

        this.matchableClasses = ImmutableSet.copyOf(caseClasses);
    }

    // PRIVATE

    private final Set<Class<? extends CaseClass>> matchableClasses;

    private void ensureClassesAreImplemented(Class<?>[] caseClasses) {
        Set<Class<?>> classes = Arrays.stream(caseClasses)
                .filter(clazz -> !ReflectUtils.isImplemented(clazz))
                .collect(ImmutableCollectors.toSet());

        if (!classes.isEmpty())
            throw new IllegalStateException("Cannot match not implemented classes: " + classes);
    }

    public static final class Builder<CaseClass> {
        /**
         * Sets handler for given caseClass, which will be executed upon a match
         * @return this builder
         * @throws NullPointerException if caseClass or onMatchConsumer is null
         * @throws IllegalArgumentException if caseClass does not belong to the CaseMatcher that spawned this builder
         * @throws IllegalStateException if caseClass already has a defined consumer in this builder
         */
        public <T extends CaseClass> Builder<CaseClass> onCase(Class<T> caseClass, Consumer<? super T> onMatchConsumer) {
            Null.check(caseClass, onMatchConsumer).ifAny("Cannot be null: caseClass, onMatchConsumer");

            if (!matchableClasses.contains(caseClass))
                throw new IllegalArgumentException("Class " + caseClass + " is not among matchable cases: " +
                        matchableClasses);

            if (matchers.containsKey(caseClass))
                throw new IllegalStateException("Multiple definitions found for case: " + caseClass);

            @SuppressWarnings("unchecked")
            Consumer<? super CaseClass> castedConsumer = (Consumer<? super CaseClass>) onMatchConsumer;
            matchers.put(caseClass, castedConsumer);

            return this;
        }

        /**
         * Sets handler for given caseClass to ignore it even if it is matched
         * @return this builder
         * @throws NullPointerException if caseClass is null
         * @throws IllegalArgumentException if caseClass does not belong to the CaseMatcher that spawned this builder
         * @throws IllegalStateException if caseClass already has a defined consumer in this builder
         */
        public <T extends CaseClass> Builder<CaseClass> ignoreCase(Class<T> caseClass) {
            return onCase(caseClass, Consumers.doNothing());
        }

        /**
         * Matches all given objects and executes appropriate handlers
         * @return this builder
         * @throws NullPointerException if values is or contains null
         * @throws IllegalStateException if not all cases have been explicitly given handlers/ignored in this builder
         * @throws IllegalArgumentException if any of the values are of class that is not defined in the CaseMatcher
         * which spawned this builder
         */
        @SafeVarargs
        public final Builder<CaseClass> match(CaseClass... values) {
            Null.checkArray(values).ifAny("Cannot be or contain null: values");

            if (!matchableClasses.equals(matchers.keySet()))
                throw new IllegalStateException("Consumers not defined for matchable cases: " +
                        Sets.difference(matchableClasses, matchers.keySet()));

            Arrays.stream(values).forEach(this::consumeCorrectValue);
            return this;
        }

        // CONSTRUCTORS

        private Builder(Set<Class<? extends CaseClass>> matchableClasses) {
            this.matchableClasses = matchableClasses;
            this.matchers = new HashMap<>();
        }

        // PRIVATE

        private final Set<Class<? extends CaseClass>> matchableClasses;
        private final Map<Class<? extends CaseClass>, Consumer<? super CaseClass>> matchers;

        private void consumeCorrectValue(CaseClass caseObject) {
            @SuppressWarnings("unchecked")
            Class<? extends CaseClass> caseClass = (Class<? extends CaseClass>) caseObject.getClass();

            Consumer<? super CaseClass> consumer = matchers.get(caseClass);
            if (consumer == null)
                throw new IllegalArgumentException("Class " + caseClass + " is not defined as matchable in CaseMatcher, " +
                        "only these are allowed: " + matchableClasses);

            consumer.accept(caseObject);
        }
    }

}
