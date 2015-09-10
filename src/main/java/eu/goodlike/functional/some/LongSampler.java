package eu.goodlike.functional.some;

import eu.goodlike.neat.Null;

import java.util.Collection;
import java.util.List;
import java.util.function.LongFunction;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * <pre>
 * An alternative to sequential loops:
 *      List list = new ArrayList();
 *      for (long i = 0; i < limit; i++)
 *          lost.add(getSomethingForLong(i));
 *
 * and primitive streams:
 *      List list = LongStream.range(0, limit)
 *              .mapToObj(this::getSomethingForLong)
 *              .collect(toList());
 *
 * which turns into:
 *      List list = Some.ofLong(this::getSomethingForLong).get(limit);
 * </pre>
 * @param <T> type returned by given long function
 */
public final class LongSampler<T> {

    /**
     * @return list of long function results, evaluated for all i in {0, amount-1}
     */
    public List<T> get(long amount) {
        return evaluate(LongStream.range(0, amount));
    }

    /**
     * @return list of long function results, evaluated for all i in {1, amount}
     */
    public List<T> fetch(long amount) {
        return evaluate(LongStream.rangeClosed(1, amount));
    }

    /**
     * @return single long function result, evaluated for index
     */
    public T sample(long index) {
        return anyFunction.apply(index);
    }

    /**
     * @return list of long function results, evaluated for all i in {startInclusive, endExclusive-1}
     */
    public List<T> range(long startInclusive, long endExclusive) {
        return evaluate(LongStream.range(startInclusive, endExclusive));
    }

    /**
     * @return list of long function results, evaluated for all i in {startInclusive, endExclusive}
     */
    public List<T> rangeClosed(long startInclusive, long endInclusive) {
        return evaluate(LongStream.range(startInclusive, endInclusive));
    }

    /**
     * @return list of long function results, evaluated for all i in indexes
     */
    public List<T> with(long... indexes) {
        Null.checkAlone(indexes).ifAny("Index array cannot be null");
        return evaluate(LongStream.of(indexes));
    }

    /**
     * @return list of long function results, evaluated for all i in indexes
     */
    public List<T> with(Collection<Long> indexes) {
        Null.checkCollection(indexes).ifAny("Index collection cannot be null");
        return evaluate(indexes.stream().mapToLong(Long::longValue));
    }

    // CONSTRUCTORS

    public LongSampler(LongFunction<T> anyFunction) {
        this.anyFunction = anyFunction;
    }

    // PRIVATE

    private final LongFunction<T> anyFunction;

    private List<T> evaluate(LongStream stream) {
        return stream
                .mapToObj(anyFunction)
                .collect(Collectors.toList());
    }

}
