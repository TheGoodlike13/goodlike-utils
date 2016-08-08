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
import java.util.function.Function;

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
    public <T extends CaseClass> MatcherBuilder<CaseClass> onCase(Class<T> caseClass, Consumer<? super T> onMatchConsumer) {
        return new MatcherBuilder<>(matchableClasses).onCase(caseClass, onMatchConsumer);
    }

    /**
     * @return builder for case classes, with the initial case set to ignore
     * @throws NullPointerException if caseClass is null
     * @throws IllegalArgumentException if caseClass does not belong to this CaseMatcher
     */
    public <T extends CaseClass> MatcherBuilder<CaseClass> ignoreCase(Class<T> caseClass) {
        return new MatcherBuilder<>(matchableClasses).ignoreCase(caseClass);
    }

    /**
     * @return builder for mapping cases into a specific class
     * @throws NullPointerException if resultClass is null
     */
    public <ResultClass> MappingBuilder<CaseClass, ResultClass> mapInto(Class<ResultClass> resultClass) {
        Null.check(resultClass).ifAny("Cannot be null: resultClass");
        return new MappingBuilder<>(matchableClasses, resultClass);
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

    private static abstract class AbstractBuilder<CaseClass> {
        // CONSTRUCTORS

        private AbstractBuilder(Set<Class<? extends CaseClass>> matchableClasses) {
            this.matchableClasses = matchableClasses;
        }

        // PRIVATE

        private final Set<Class<? extends CaseClass>> matchableClasses;

        protected final void assertCaseIsMatchable(Class<? extends CaseClass> caseClass) {
            if (!matchableClasses.contains(caseClass))
                throw new IllegalArgumentException("Class " + caseClass + " is not among matchable cases: " +
                        matchableClasses);
        }

        protected final void assertAllConsumersAreDefined(Set<Class<? extends CaseClass>> matcherKeySet) {
            if (!matchableClasses.equals(matcherKeySet))
                throw new IllegalStateException("Consumers not defined for matchable cases: " +
                        Sets.difference(matchableClasses, matcherKeySet));
        }

        protected final void throwInsteadOfConsuming(CaseClass caseObject) {
            throw new IllegalArgumentException("Class " + caseObject.getClass() +
                    " is not defined as matchable in CaseMatcher, only these are allowed: " +
                    matchableClasses);
        }

        protected final Class<? extends CaseClass> getExactClass(CaseClass object) {
            @SuppressWarnings("unchecked")
            Class<? extends CaseClass> caseClass = (Class<? extends CaseClass>) object.getClass();
            return caseClass;
        }
    }

    public static final class MatcherBuilder<CaseClass> extends AbstractBuilder<CaseClass> {
        /**
         * Sets handler for given caseClass, which will be executed upon a match
         * @return this builder
         * @throws NullPointerException if caseClass or onMatchConsumer is null
         * @throws IllegalArgumentException if caseClass does not belong to the CaseMatcher that spawned this builder
         * @throws IllegalStateException if caseClass already has a defined consumer in this builder
         */
        public <T extends CaseClass> MatcherBuilder<CaseClass> onCase(Class<T> caseClass, Consumer<? super T> onMatchConsumer) {
            Null.check(caseClass, onMatchConsumer).ifAny("Cannot be null: caseClass, onMatchConsumer");
            assertCaseIsMatchable(caseClass);

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
        public <T extends CaseClass> MatcherBuilder<CaseClass> ignoreCase(Class<T> caseClass) {
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
        public final MatcherBuilder<CaseClass> match(CaseClass... values) {
            Null.checkArray(values).ifAny("Cannot be or contain null: values");
            assertAllConsumersAreDefined(matchers.keySet());

            Arrays.stream(values).forEach(this::consumeCorrectValue);
            return this;
        }

        // CONSTRUCTORS

        private MatcherBuilder(Set<Class<? extends CaseClass>> matchableClasses) {
            super(matchableClasses);
            this.matchers = new HashMap<>();
        }

        // PRIVATE

        private final Map<Class<? extends CaseClass>, Consumer<? super CaseClass>> matchers;

        private void consumeCorrectValue(CaseClass caseObject) {
            getMatchingConsumer(getExactClass(caseObject)).accept(caseObject);
        }

        private Consumer<? super CaseClass> getMatchingConsumer(Class<? extends CaseClass> caseClass) {
            return matchers.getOrDefault(caseClass, this::throwInsteadOfConsuming);
        }
    }

    public static final class MappingBuilder<CaseClass, ResultClass> extends AbstractBuilder<CaseClass> {
        /**
         * Sets mapper for given caseClass; it will be used on the mapped object if it is exactly of given class
         * @return this builder
         * @throws NullPointerException if caseClass or mapper is null
         * @throws IllegalArgumentException if caseClass does not belong to the CaseMatcher that spawned this builder
         * @throws IllegalStateException if caseClass already has a defined mapper in this builder
         */
        public <T extends CaseClass> MappingBuilder<CaseClass, ResultClass> onCase(Class<T> caseClass,
                                                                                   Function<? super T, ? extends ResultClass> mapper) {
            Null.check(caseClass, mapper).ifAny("Cannot be null: caseClass, mapper");
            assertCaseIsMatchable(caseClass);

            if (matchers.containsKey(caseClass))
                throw new IllegalStateException("Multiple definitions found for case: " + caseClass);

            @SuppressWarnings("unchecked")
            Function<? super CaseClass, ? extends ResultClass> castedFunction = (Function<? super CaseClass, ? extends ResultClass>) mapper;
            matchers.put(caseClass, castedFunction);

            return this;
        }

        /**
         * Sets instance for given caseClass; it will be used to replace the mapped object if it is exactly of given class
         * @return this builder
         * @throws NullPointerException if caseClass is null
         * @throws IllegalArgumentException if caseClass does not belong to the CaseMatcher that spawned this builder
         * @throws IllegalStateException if caseClass already has a defined mapper in this builder
         */
        public <T extends CaseClass, R extends ResultClass> MappingBuilder<CaseClass, ResultClass> onCase(Class<T> caseClass,
                                                                                                          R instance) {
            Function<? super T, ? extends ResultClass> instanceFunction = any -> instance;
            return onCase(caseClass, instanceFunction);
        }

        /**
         * @return matches the class of given value and maps using appropriate mapper
         * @throws NullPointerException if value is null
         * @throws IllegalStateException if not all cases have been explicitly given mappers/instances in this builder
         * @throws IllegalArgumentException if value is of class that is not defined in the CaseMatcher which spawned
         * this builder
         */
        public ResultClass map(CaseClass value) {
            Null.check(value).ifAny("Cannot be null: value");
            assertAllConsumersAreDefined(matchers.keySet());

            return mapIntoCorrectValue(value);
        }

        // CONSTRUCTORS

        private MappingBuilder(Set<Class<? extends CaseClass>> matchableClasses, Class<ResultClass> resultClass) {
            super(matchableClasses);
            this.resultClass = resultClass;
            this.matchers = new HashMap<>();
        }

        // PRIVATE

        private final Class<ResultClass> resultClass;
        private final Map<Class<? extends CaseClass>, Function<? super CaseClass, ? extends ResultClass>> matchers;

        private ResultClass mapIntoCorrectValue(CaseClass caseObject) {
            return getMatchingFunction(getExactClass(caseObject)).apply(caseObject);
        }

        private Function<? super CaseClass, ? extends ResultClass> getMatchingFunction(Class<? extends CaseClass> caseClass) {
            return matchers.getOrDefault(caseClass, this::throwInsteadOfReturning);
        }

        private ResultClass throwInsteadOfReturning(CaseClass caseClass) {
            throwInsteadOfConsuming(caseClass);
            throw new AssertionError("The above method call will always throw an exception");
        }
    }

}
