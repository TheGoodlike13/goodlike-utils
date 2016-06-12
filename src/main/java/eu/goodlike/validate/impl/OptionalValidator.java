package eu.goodlike.validate.impl;

import eu.goodlike.validate.Validator;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Validator implementation for Optional
 */
public final class OptionalValidator<T> extends Validator<Optional<T>, OptionalValidator<T>> {

    /**
     * Adds a predicate which tests if the optional being validated has a value
     */
    public final OptionalValidator<T> isPresent() {
        return registerCondition(Optional::isPresent);
    }

    /**
     * Adds a predicate which tests if the optional being validated passes the given predicate
     * @throws NullPointerException if predicate is null
     */
    public final OptionalValidator<T> passesAsValue(Predicate<? super T> predicate) {
        return passesAs(Optional::get, predicate);
    }

    // CONSTRUCTORS

    public OptionalValidator() {
        super();
    }

    protected OptionalValidator(Predicate<Optional<T>> mainCondition, Predicate<Optional<T>> accumulatedCondition, boolean negateNext) {
        super(mainCondition, accumulatedCondition, negateNext);
    }

    // PROTECTED

    @Override
    protected OptionalValidator<T> thisValidator() {
        return this;
    }

    @Override
    protected OptionalValidator<T> newValidator(Predicate<Optional<T>> mainCondition, Predicate<Optional<T>> accumulatedCondition, boolean negateNext) {
        return new OptionalValidator<>(mainCondition, accumulatedCondition, negateNext);
    }

}
