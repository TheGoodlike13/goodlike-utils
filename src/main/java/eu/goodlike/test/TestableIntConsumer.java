package eu.goodlike.test;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.primitives.Ints;
import eu.goodlike.neat.Null;

import java.util.Collections;
import java.util.List;
import java.util.function.IntConsumer;

/**
 * <pre>
 * IntConsumer which counts how many times it has accepted values (including specific ones)
 *
 * You should verify that all threads that need to access this runnable have finished before asserting using its methods,
 * otherwise the results will not be consistent
 * </pre>
 */
public final class TestableIntConsumer implements IntConsumer {

    /**
     * @return true if this IntConsumer has accepted at least one value, false otherwise
     */
    public boolean hasAcceptedAny() {
        return !acceptedValues.isEmpty();
    }

    /**
     * @return true if this IntConsumer has accepted the specific value at least once, false otherwise
     */
    public boolean hasAccepted(int specificValue) {
        return acceptedValues.contains(specificValue);
    }

    /**
     * @return total amount of values accepted by this IntConsumer
     */
    public int totalTimesAccepted() {
        return acceptedValues.size();
    }

    /**
     * @return amount of times this IntConsumer has accepted the specific value
     */
    public int timesAccepted(int specificValue) {
        return acceptedValues.count(specificValue);
    }

    @Override
    public void accept(int value) {
        acceptedValues.add(value);
    }

    // CONSTRUCTORS

    public TestableIntConsumer() {
        this(Collections.emptyList());
    }

    public TestableIntConsumer(int... alreadyAcceptedValues) {
        Null.checkAlone(alreadyAcceptedValues).ifAny("Cannot be null: alreadyAcceptedValues");
        this.acceptedValues = ConcurrentHashMultiset.create(Ints.asList(alreadyAcceptedValues));
    }

    public TestableIntConsumer(List<Integer> alreadyAcceptedValues) {
        Null.checkList(alreadyAcceptedValues).ifAny("Cannot be or contain null: alreadyAcceptedValues");
        this.acceptedValues = ConcurrentHashMultiset.create(alreadyAcceptedValues);
    }

    // PRIVATE

    private final Multiset<Integer> acceptedValues;

}
