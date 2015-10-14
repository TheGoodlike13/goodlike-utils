package eu.goodlike.v2.validate.impl;

import eu.goodlike.v2.validate.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Object validator implementation; prefer to use other implementations because they have more methods
 */
public final class ObjectValidator<T> extends Validate<T, ObjectValidator<T>> {

    // CONSTRUCTORS

    public ObjectValidator() {
        this(null, null, new ArrayList<>(), false);
    }

    protected ObjectValidator(ObjectValidator<T> outerValidator, Predicate<T> condition, List<Predicate<T>> subConditions, boolean notCondition) {
        super(outerValidator, condition, subConditions, notCondition);
    }

    // PROTECTED

    @Override
    protected ObjectValidator<T> thisValidator() {
        return this;
    }

    @Override
    protected ObjectValidator<T> newValidator(ObjectValidator<T> outerValidator, Predicate<T> condition, List<Predicate<T>> subConditions, boolean notCondition) {
        return new ObjectValidator<>(outerValidator, condition, subConditions, notCondition);
    }

}
