package eu.goodlike.test;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;
import eu.goodlike.neat.Null;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * <pre>
 * Consumer which counts how many times it has accepted values (including specific ones)
 *
 * You should verify that all threads that need to access this runnable have finished before asserting using its methods,
 * otherwise the results will not be consistent
 * </pre>
 */
public final class TestableConsumer<T> implements Consumer<T> {

    /**
     * @return true if this Consumer has accepted at least one value, false otherwise
     */
    public boolean hasAcceptedAny() {
        return !acceptedValues.isEmpty();
    }

    /**
     * @return true if this Consumer has accepted the specific value at least once, false otherwise
     */
    public boolean hasAccepted(T specificValue) {
        return acceptedValues.contains(specificValue);
    }

    /**
     * @return total amount of values accepted by this Consumer
     */
    public int totalTimesAccepted() {
        return acceptedValues.size();
    }

    /**
     * @return amount of times this Consumer has accepted the specific value
     */
    public int timesAccepted(T specificValue) {
        return acceptedValues.count(specificValue);
    }

    @Override
    public void accept(T value) {
        acceptedValues.add(value);
    }

    // CONSTRUCTORS

    public TestableConsumer() {
        this(Collections.emptyList());
    }

    @SafeVarargs
    public TestableConsumer(T... alreadyAcceptedValues) {
        Null.checkAlone(alreadyAcceptedValues).ifAny("Cannot be null: alreadyAcceptedValues");
        this.acceptedValues = ConcurrentHashMultiset.create(Arrays.asList(alreadyAcceptedValues));
    }

    public TestableConsumer(List<T> alreadyAcceptedValues) {
        Null.checkList(alreadyAcceptedValues).ifAny("Cannot be or contain null: alreadyAcceptedValues");
        this.acceptedValues = ConcurrentHashMultiset.create(alreadyAcceptedValues);
    }

    // PRIVATE

    private final Multiset<T> acceptedValues;

}
