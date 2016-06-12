package eu.goodlike.validate.impl;

import eu.goodlike.validate.Validator;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Validator implementation for Map
 */
public final class MapValidator<K, V> extends Validator<Map<K, V>, MapValidator<K, V>> {

    /**
     * Adds a predicate which tests if the map's keys pass the given predicate
     * @throws NullPointerException if predicate is null
     */
    public MapValidator<K, V> passesAsKeys(Predicate<? super Set<K>> predicate) {
        return passesAs(Map::keySet, predicate);
    }

    /**
     * Adds a predicate which tests if the map's values pass the given predicate
     * @throws NullPointerException if predicate is null
     */
    public MapValidator<K, V> passesAsValues(Predicate<? super Collection<V>> predicate) {
        return passesAs(Map::values, predicate);
    }

    /**
     * Adds a predicate which tests if the map's entries pass the given predicate
     * @throws NullPointerException if predicate is null
     */
    public MapValidator<K, V> passesAsEntries(Predicate<? super Set<Map.Entry<K, V>>> predicate) {
        return passesAs(Map::entrySet, predicate);
    }

    // CONSTRUCTORS

    public MapValidator() {
        super();
    }

    protected MapValidator(Predicate<Map<K, V>> mainCondition, Predicate<Map<K, V>> accumulatedCondition, boolean negateNext) {
        super(mainCondition, accumulatedCondition, negateNext);
    }

    // PROTECTED

    @Override
    protected MapValidator<K, V> thisValidator() {
        return this;
    }

    @Override
    protected MapValidator<K, V> newValidator(Predicate<Map<K, V>> mainCondition, Predicate<Map<K, V>> accumulatedCondition, boolean negateNext) {
        return new MapValidator<>(mainCondition, accumulatedCondition, negateNext);
    }

}
