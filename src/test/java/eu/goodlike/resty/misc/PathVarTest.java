package eu.goodlike.resty.misc;

import eu.goodlike.misc.SpecialUtils;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PathVarTest {

    private final String pathVarName = "pathVarName";
    private final String pathVarValue = "pathVarValue";
    private final String pathVarValueNeedsEncoding = "\"has?encodable\\chars";

    private PathVar pathVar;
    private PathVar encodedPathVar;

    @Before
    public void setup() {
        pathVar = new PathVar(pathVarName, pathVarValue);
        encodedPathVar = new PathVar("Encoded", pathVarValueNeedsEncoding);
    }

    @Test
    public void tryName_shouldReturnNameWithVarTokenBeforehand() {
        assertThat(pathVar.name()).isEqualTo(PathVar.varToken() + pathVarName);
    }

    @Test
    public void tryValue_shouldReturnEncodedValue() {
        assertThat(pathVar.value()).isEqualTo(SpecialUtils.urlEncode(pathVarValue));
    }

    @Test
    public void tryEncodedValue_shouldBeEqualToEncodedValue() {
        assertThat(encodedPathVar.value()).isNotEqualTo(pathVarValueNeedsEncoding)
                .isEqualTo(SpecialUtils.urlEncode(pathVarValueNeedsEncoding));
    }

    @Test(expected = IllegalArgumentException.class)
    public void tryBlankName_shouldThrowException() {
        new PathVar(" ", pathVarValue);
    }

}
