package eu.goodlike.functional.some;

import eu.goodlike.neat.Null;

import java.util.Collection;
import java.util.List;
import java.util.function.LongFunction;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

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
 *      List list = Some.Of(this::getSomethingForLong).zeroTo(limit);
 * </pre>
 * @param <T> type returned by given long function
 */
public final class LongSampler<T> {

    /**
     * @return single long function result, evaluated for index
     */
    public T sample(long index) {
        return anyFunction.apply(index);
    }

    /**
     * @return list of long function results, evaluated for all i in {0, amount-1}
     */
    public List<T> zeroTo(long amount) {
        return evaluate(LongStream.range(0, amount));
    }

    /**
     * @return list of long function results, evaluated for all i in {1, amount}
     */
    public List<T> oneUpTo(long amount) {
        return evaluate(LongStream.rangeClosed(1, amount));
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
        return evaluate(LongStream.rangeClosed(startInclusive, endInclusive));
    }

    /**
     * @return list of long function results, evaluated for all i in indexes
     */
    public List<T> with(long... indexes) {
        return toList(stream(indexes));
    }

    /**
     * @return list of long function results, evaluated for all i in indexes
     */
    public List<T> with(Collection<Long> indexes) {
        return toList(stream(indexes));
    }

    /**
     * @return stream of long function results, evaluated for all i in {0, amount-1}
     */
    public Stream<T> zeroToStream(long amount) {
        return map(LongStream.range(0, amount));
    }

    /**
     * @return stream of long function results, evaluated for all i in {1, amount}
     */
    public Stream<T> oneUpToStream(long amount) {
        return map(LongStream.rangeClosed(1, amount));
    }

    /**
     * @return stream of long function results, evaluated for all i in {startInclusive, endExclusive-1}
     */
    public Stream<T> rangeStream(long startInclusive, long endExclusive) {
        return map(LongStream.range(startInclusive, endExclusive));
    }

    /**
     * @return stream of long function results, evaluated for all i in {startInclusive, endExclusive}
     */
    public Stream<T> rangeStreamClosed(long startInclusive, long endInclusive) {
        return map(LongStream.rangeClosed(startInclusive, endInclusive));
    }

    /**
     * @return stream of long function results, evaluated for all i in indexes
     */
    public Stream<T> stream(long... indexes) {
        Null.checkAlone(indexes).ifAny("Index array cannot be null");
        return map(LongStream.of(indexes));
    }

    /**
     * @return stream of long function results, evaluated for all i in indexes
     */
    public Stream<T> stream(Collection<Long> indexes) {
        Null.checkCollection(indexes).ifAny("Index collection cannot be null");
        return map(indexes.stream().mapToLong(Long::intValue));
    }

    // CONSTRUCTORS

    public LongSampler(LongFunction<T> anyFunction) {
        Null.check(anyFunction).ifAny("Long function cannot be null");
        this.anyFunction = anyFunction;
    }

    // PRIVATE

    private final LongFunction<T> anyFunction;

    private List<T> evaluate(LongStream stream) {
        return toList(map(stream));
    }

    private Stream<T> map(LongStream stream) {
        return stream.mapToObj(anyFunction);
    }

    private List<T> toList(Stream<T> stream) {
        return stream.collect(Collectors.toList());
    }

}
