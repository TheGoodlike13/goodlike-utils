package eu.goodlike.resty.http.steps.custom;

import eu.goodlike.resty.url.DynamicURL;

/**
 * Simple implementation for CustomDynamicStepBuilder; the implementation of this class MUST also implement FirstStep
 * @param <FirstStep> the interface which will be returned as a chain of the method call
 */
public abstract class AbstractDynamicStepBuilder<FirstStep> implements CustomDynamicStepBuilder<FirstStep> {

    @Override
    public FirstStep setup(DynamicURL dynamicURL) {
        this.dynamicURL = dynamicURL;
        @SuppressWarnings("unchecked")
        FirstStep firstStep = (FirstStep) this;
        return firstStep;
    }

    // PROTECTED

    protected DynamicURL dynamicURL;

}
