package eu.goodlike.resty.http.steps.custom;

import eu.goodlike.resty.url.StaticURL;

/**
 * Simple implementation for CustomStaticStepBuilder; the implementation of this class MUST also implement FirstStep
 * @param <FirstStep> the interface which will be returned as a chain of the method call
 */
public abstract class AbstractStaticStepBuilder<FirstStep> implements CustomStaticStepBuilder<FirstStep> {

    @Override
    public FirstStep setup(StaticURL staticURL) {
        this.staticURL = staticURL;
        @SuppressWarnings("unchecked")
        FirstStep firstStep = (FirstStep) this;
        return firstStep;
    }

    // PROTECTED

    protected StaticURL staticURL;

}
