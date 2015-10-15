package eu.goodlike.misc;

import org.junit.Test;

import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;

public class ConstantsTest {

    @Test
    public void confirmConstantApplicationJsonUtf8() {
        assertThat(Constants.APPLICATION_JSON_UTF8).isEqualTo("application/json;charset=UTF-8");
    }

    @Test
    public void confirmConstantMySQLGroupConcatSeparator() {
        assertThat(Constants.MYSQL_GROUP_CONCAT_SEPARATOR).isEqualTo(",");
    }

    @Test
    public void confirmConstantDefaultVarcharFieldSize() {
        assertThat(Constants.DEFAULT_VARCHAR_FIELD_SIZE).isEqualTo(180);
    }

    @Test
    public void confirmConstantDefaultPage() {
        assertThat(Constants.DEFAULT_PAGE).isEqualTo(0);
    }

    @Test
    public void confirmConstantDefaultPerPage() {
        assertThat(Constants.DEFAULT_PER_PAGE).isEqualTo(25);
    }

    @Test
    public void confirmConstantDefaultCharsetString() {
        assertThat(Constants.DEFAULT_CHARSET).isEqualTo("UTF-8");
    }

    @Test
    public void confirmConstantDefaultCharset() {
        assertThat(Constants.DEF_CHARSET).isEqualTo(Charset.forName(Constants.DEFAULT_CHARSET));
    }

}
