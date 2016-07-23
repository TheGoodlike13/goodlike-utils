package eu.goodlike.misc;

import eu.goodlike.validate.ComparableValidator;
import eu.goodlike.validate.impl.StringValidator;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectUtilsTest {

    @Test
    public void canTellIfClassIsAbstract() {
        assertThat(ReflectUtils.isAbstract(ComparableValidator.class))
                .as("check if ComparableValidator is abstract")
                .isTrue();
    }

    @Test
    public void canTellIfClassIsNotAbstract() {
        assertThat(ReflectUtils.isAbstract(Object.class))
                .as("check if Object is abstract")
                .isFalse();
    }

    @Test
    public void canTellIfClassIsImplemented() {
        assertThat(ReflectUtils.isImplemented(StringValidator.class))
                .as("check if StringValidator is not an interface or abstract class")
                .isTrue();
    }

    @Test
    public void canTellIfClassIsAbstractOrInterface() {
        assertThat(ReflectUtils.isImplemented(Runnable.class))
                .as("check if Runnable is not an interface or abstract class")
                .isFalse();
    }

}