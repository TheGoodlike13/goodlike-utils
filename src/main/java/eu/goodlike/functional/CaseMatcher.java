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
 * It does not provide any compile time guarantees, only runtime
 * </pre>
 */
public final class CaseMatcher<CaseClass> {

    public <T extends CaseClass> Builder<CaseClass> onCase(Class<T> caseClass, Consumer<? super T> onMatchConsumer) {
        return new Builder<>(matchableClasses).onCase(caseClass, onMatchConsumer);
    }

    // CONSTRUCTORS

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
