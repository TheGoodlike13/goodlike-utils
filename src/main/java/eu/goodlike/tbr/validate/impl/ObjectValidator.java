package eu.goodlike.tbr.validate.impl;

import eu.goodlike.tbr.validate.Validate;

import java.util.function.Predicate;

/**
 * Object validator implementation; prefer to use other implementations because they have more methods
 */
public final class ObjectValidator<T> extends Validate<T, ObjectValidator<T>> {

    // CONSTRUCTORS

    public ObjectValidator() {
        this(null, null, null, false);
    }

    protected ObjectValidator(ObjectValidator<T> outerValidator, Predicate<T> mainCondition, Predicate<T> accumulatedCondition, boolean notCondition) {
        super(outerValidator, mainCondition, accumulatedCondition, notCondition);
    }

    // PROTECTED

    @Override
    protected ObjectValidator<T> thisValidator() {
        return this;
    }

    @Override
    protected ObjectValidator<T> newValidator(ObjectValidator<T> outerValidator, Predicate<T> mainCondition, Predicate<T> accumulatedCondition, boolean notCondition) {
        return new ObjectValidator<>(outerValidator, mainCondition, accumulatedCondition, notCondition);
    }

}
