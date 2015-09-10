package eu.goodlike.functional.some;

import eu.goodlike.neat.Null;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <pre>
 * Object version of IntSampler/LongSampler; in general, using
 *      Sequence.of(valuesOfT).map(function).toList();
 *
 * or
 *      Seq.seq(collectionOfT).map(function).toList();
 *
 * makes more sense than
 *      Some.valuesOf(function).with(valuesOfT);
 *      Some.valuesOf(function).with(collectionOfT);
 *
 * because it is easier to modify it; at least there are no Seq for primitives (yet) where this helps;
 * regardless, since it is easy to implement, I made it anyway
 * </pre>
 * @param <T> type taken by given function
 * @param <U> type returned by given function
 */
public final class ObjectSampler<T, U> {

    /**
     * @return single long function result, evaluated for value
     */
    public U get(T value) {
        return anyFunction.apply(value);
    }

    /**
     * @return list of long function results, evaluated for all values
     */
    @SafeVarargs
    public final List<U> with(T... values) {
        return toList(stream(values));
    }

    /**
     * @return list of long function results, evaluated for all values
     */
    public List<U> with(Collection<T> values) {
        return toList(stream(values));
    }

    /**
     * @return stream of long function results, evaluated for all values
     */
    @SafeVarargs
    public final Stream<U> stream(T... values) {
        Null.checkAlone(values).ifAny("Value array cannot be null");
        return map(Stream.of(values));
    }

    /**
     * @return stream of long function results, evaluated for all values
     */
    public Stream<U> stream(Collection<T> values) {
        Null.checkAlone(values).ifAny("Value collection cannot be null");
        return map(values.stream());
    }

    // CONSTRUCTORS

    public ObjectSampler(Function<T, U> anyFunction) {
        this.anyFunction = anyFunction;
    }

    // PRIVATE

    private final Function<T, U> anyFunction;

    private Stream<U> map(Stream<T> stream) {
        return stream
                .map(anyFunction);
    }

    private List<U> toList(Stream<U> stream) {
        return stream.collect(Collectors.toList());
    }

}
