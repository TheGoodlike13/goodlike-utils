package eu.goodlike.resty.misc;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HeaderTest {

    private final String headerName = "headerName";
    private final String headerValue = "headerValue";

    private Header header;

    @Before
    public void setup() {
        header = new Header(headerName, headerValue);
    }

    @Test
    public void tryHeaderName_shouldReturnName() {
        assertThat(header.name()).isEqualTo(headerName);
    }

    @Test
    public void tryHeaderValue_shouldReturnValue() {
        assertThat(header.value()).isEqualTo(headerValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void tryBlankName_shouldThrowException() {
        new Header(" ", "blankNameValue");
    }

}
