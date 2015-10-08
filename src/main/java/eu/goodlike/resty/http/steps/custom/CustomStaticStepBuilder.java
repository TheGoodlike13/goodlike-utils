package eu.goodlike.resty.http.steps.custom;

import eu.goodlike.resty.url.StaticURL;

/**
 * <pre>
 * Custom builder enabler
 *
 * Using this, you can do:
 *      staticURL.custom(implementationOfCustomStaticStepBuilder)
 * which will allow chaining custom interfaces in a step builder pattern
 *
 * AbstractStaticStepBuilder is an implementation you can use, but make sure that the implementing class also
 * implements FirstStep
 * </pre>
 * @param <FirstStep> the interface which will be returned as a chain of the method call
 */
public interface CustomStaticStepBuilder<FirstStep> {

    /**
     * @return next step of the custom step builder
     */
    FirstStep setup(StaticURL staticURL);

}
