package eu.goodlike.functional.some;

import eu.goodlike.neat.Null;

import java.util.Collection;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * <pre>
 * An alternative to sequential loops:
 *      List list = new ArrayList();
 *      for (int i = 0; i < limit; i++)
 *          lost.add(getSomethingForInt(i));
 *
 * and primitive streams:
 *      List list = IntStream.range(0, limit)
 *              .mapToObj(this::getSomethingForInt)
 *              .collect(toList());
 *
 * which turns into:
 *      List list = Some.of(this::getSomethingForInt).zeroTo(limit);
 * </pre>
 * @param <T> type returned by given integer function
 */
public final class IntSampler<T> {

    /**
     * @return single int function result, evaluated for index
     */
    public T sample(int index) {
        return anyFunction.apply(index);
    }

    /**
     * @return list of int function results, evaluated for all i in {0, amount-1}
     */
    public List<T> zeroTo(int amount) {
        return evaluate(IntStream.range(0, amount));
    }

    /**
     * @return list of int function results, evaluated for all i in {1, amount}
     */
    public List<T> oneUpTo(int amount) {
        return evaluate(IntStream.rangeClosed(1, amount));
    }

    /**
     * @return list of int function results, evaluated for all i in {startInclusive, endExclusive-1}
     */
    public List<T> range(int startInclusive, int endExclusive) {
        return evaluate(IntStream.range(startInclusive, endExclusive));
    }

    /**
     * @return list of int function results, evaluated for all i in {startInclusive, endExclusive}
     */
    public List<T> rangeClosed(int startInclusive, int endInclusive) {
        return evaluate(IntStream.rangeClosed(startInclusive, endInclusive));
    }

    /**
     * @return list of int function results, evaluated for all i in indexes
     */
    public List<T> with(int... indexes) {
        return toList(stream(indexes));
    }

    /**
     * @return list of int function results, evaluated for all i in indexes
     */
    public List<T> with(Collection<Integer> indexes) {
        return toList(stream(indexes));
    }

    /**
     * @return stream of int function results, evaluated for all i in {0, amount-1}
     */
    public Stream<T> zeroToStream(int amount) {
        return map(IntStream.range(0, amount));
    }

    /**
     * @return stream of int function results, evaluated for all i in {1, amount}
     */
    public Stream<T> oneUpToStream(int amount) {
        return map(IntStream.rangeClosed(1, amount));
    }

    /**
     * @return stream of int function results, evaluated for all i in {startInclusive, endExclusive-1}
     */
    public Stream<T> rangeStream(int startInclusive, int endExclusive) {
        return map(IntStream.range(startInclusive, endExclusive));
    }

    /**
     * @return stream of int function results, evaluated for all i in {startInclusive, endExclusive}
     */
    public Stream<T> rangeStreamClosed(int startInclusive, int endInclusive) {
        return map(IntStream.rangeClosed(startInclusive, endInclusive));
    }

    /**
     * @return stream of int function results, evaluated for all i in indexes
     */
    public Stream<T> stream(int... indexes) {
        Null.checkAlone(indexes).ifAny("Index array cannot be null");
        return map(IntStream.of(indexes));
    }

    /**
     * @return stream of int function results, evaluated for all i in indexes
     */
    public Stream<T> stream(Collection<Integer> indexes) {
        Null.checkCollection(indexes).ifAny("Index collection cannot be null");
        return map(indexes.stream().mapToInt(Integer::intValue));
    }

    // CONSTRUCTORS

    public IntSampler(IntFunction<T> anyFunction) {
        Null.check(anyFunction).ifAny("Integer function cannot be null");
        this.anyFunction = anyFunction;
    }

    // PRIVATE

    private final IntFunction<T> anyFunction;

    private List<T> evaluate(IntStream stream) {
        return toList(map(stream));
    }

    private Stream<T> map(IntStream stream) {
        return stream.mapToObj(anyFunction);
    }

    private List<T> toList(Stream<T> stream) {
        return stream.collect(Collectors.toList());
    }

}
