package eu.goodlike.functional;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.List;
import java.util.Set;
import java.util.stream.Collector;

/**
 * Collectors for immutable collections from Guava
 */
public final class ImmutableCollectors {

    /**
     * @return collects Stream into an ImmutableList
     */
    public static <T> Collector<T, ImmutableList.Builder<T>, List<T>> toList() {
        return Collector.of(ImmutableList::builder, ImmutableList.Builder::add, ImmutableCollectors::combine, ImmutableList.Builder::build);
    }

    /**
     * @return collects Stream into an ImmutableSet
     */
    public static <T> Collector<T, ImmutableSet.Builder<T>, Set<T>> toSet() {
        return Collector.of(ImmutableSet::builder, ImmutableSet.Builder::add, ImmutableCollectors::combine, ImmutableSet.Builder::build);
    }

    // PRIVATE

    private ImmutableCollectors() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

    private static <T, B extends ImmutableCollection.Builder<T>> B combine(B left, B right) {
        @SuppressWarnings("unchecked")
        B builder = (B) left.addAll(right.build());
        return builder;
    }

}
