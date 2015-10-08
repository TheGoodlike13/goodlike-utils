package eu.goodlike.resty.http.steps.custom;

import eu.goodlike.resty.url.DynamicURL;

/**
 * <pre>
 * Custom builder enabler
 *
 * Using this, you can do:
 *      dynamicURL.custom(implementationOfCustomDynamicStepBuilder)
 * which will allow chaining custom interfaces in a step builder pattern
 *
 * AbstractDynamicStepBuilder is an implementation you can use, but make sure that the implementing class also
 * implements FirstStep
 * </pre>
 * @param <FirstStep> the interface which will be returned as a chain of the method call
 */
public interface CustomDynamicStepBuilder<FirstStep> {

    /**
     * @return next step of the custom step builder
     */
    FirstStep setup(DynamicURL dynamicURL);

}
