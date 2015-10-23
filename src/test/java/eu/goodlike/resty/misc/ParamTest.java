package eu.goodlike.resty.misc;

import eu.goodlike.misc.SpecialUtils;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ParamTest {

    private final String paramName = "paramName";
    private final String paramValue = "paramValue";
    private final String paramValueNeedsEncoding = "\"has?encodable\\chars";

    private Param param;
    private Param emptyParam;
    private Param encodedParam;

    @Before
    public void setup() {
        param = new Param(paramName, paramValue);
        emptyParam = new Param("Empty", null);
        encodedParam = new Param("Encoded", paramValueNeedsEncoding);
    }

    @Test
    public void tryName_shouldReturnName() {
        assertThat(param.name()).isEqualTo(paramName);
    }

    @Test
    public void tryValue_shouldReturnEncodedValue() {
        assertThat(param.value()).isPresent().contains(SpecialUtils.urlEncode(paramValue));
    }

    @Test
    public void tryEmptyValue_shouldReturnEmpty() {
        assertThat(emptyParam.value()).isEmpty();
    }

    @Test
    public void tryValueNullable_shouldReturnEncodedValue() {
        assertThat(param.valueNullable()).isEqualTo(SpecialUtils.urlEncode(paramValue));
    }

    @Test
    public void tryEmptyValueNullable_shouldReturnNull() {
        assertThat(emptyParam.valueNullable()).isNull();
    }

    @Test
    public void tryEncodedValue_shouldBeEqualToEncodedValue() {
        assertThat(encodedParam.valueNullable()).isNotEqualTo(paramValueNeedsEncoding)
                .isEqualTo(SpecialUtils.urlEncode(paramValueNeedsEncoding));
    }

    @Test(expected = IllegalArgumentException.class)
    public void tryBlankParam_shouldThrowException() {
        new Param(" ", paramValue);
    }

}
