package eu.goodlike.validate.impl;

import eu.goodlike.validate.Validator;

import java.util.function.Predicate;

/**
 * Validator implementation for any Object
 */
public final class ObjectValidator<T> extends Validator<T, ObjectValidator<T>> {

    // CONSTRUCTORS

    public ObjectValidator() {
        super();
    }

    protected ObjectValidator(Predicate<T> mainCondition, Predicate<T> accumulatedCondition, boolean negateNext) {
        super(mainCondition, accumulatedCondition, negateNext);
    }

    // PROTECTED

    @Override
    protected ObjectValidator<T> thisValidator() {
        return this;
    }

    @Override
    protected ObjectValidator<T> newValidator(Predicate<T> mainCondition, Predicate<T> accumulatedCondition, boolean negateNext) {
        return new ObjectValidator<>(mainCondition, accumulatedCondition, negateNext);
    }

}
