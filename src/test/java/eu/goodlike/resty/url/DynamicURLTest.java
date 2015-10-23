package eu.goodlike.resty.url;

import org.junit.Before;
import org.junit.Test;

import static eu.goodlike.resty.url.Protocol.http;
import static eu.goodlike.resty.url.Protocol.https;
import static org.assertj.core.api.Assertions.assertThat;

public class DynamicURLTest {

    private final Protocol twitchProtocol = https;
    private final String twitchIP = "api.twitch.tv";
    private final String twitchPath = "kraken";
    private final String twitchURL = twitchProtocol + "://" + twitchIP + "/" + twitchPath;

    private DynamicURL dynamicURL;

    @Before
    public void setup() {
        dynamicURL = DynamicURL.builder().protocol(twitchProtocol).IP(twitchIP).withPath(twitchPath);
    }

    @Test
    public void tryUrlWithParams_shouldTrimParams() {
        assertThat(dynamicURL).isEqualTo(DynamicURL.of(twitchURL + "?fake_param=fake"));
    }

    @Test
    public void tryGettingUrl_shouldReturnSameAsTrimmedSeed() {
        assertThat(dynamicURL.getUrl()).isEqualTo(twitchURL);
    }

    @Test
    public void tryWithProtocol_shouldChangeProtocol() {
        assertThat(dynamicURL.withProtocol(http)).isEqualTo(DynamicURL.builder()
                .protocol(http)
                .IP(twitchIP)
                .withPath(twitchPath));
    }

    @Test
    public void tryWithIP_shouldChangeHostname() {
        assertThat(dynamicURL.withIP("localhost")).isEqualTo(DynamicURL.builder()
                .protocol(twitchProtocol)
                .IP("localhost")
                .withPath(twitchPath));
    }

    @Test
    public void tryWithPort_shouldChangePort() {
        assertThat(dynamicURL.withPort(8080)).isEqualTo(DynamicURL.builder()
                .protocol(twitchProtocol)
                .IP(twitchIP)
                .withPort(8080)
                .withPath(twitchPath));
    }

    @Test
    public void tryWithPath_shouldChangePath() {
        assertThat(dynamicURL.withPath("different")).isEqualTo(DynamicURL.builder()
                .protocol(twitchProtocol)
                .IP(twitchIP)
                .withPath("different"));
    }

    @Test
    public void tryAppendPath_shouldAppendPath() {
        assertThat(dynamicURL.appendPath("more")).isEqualTo(DynamicURL.builder()
                .protocol(twitchProtocol)
                .IP(twitchIP)
                .withPath(twitchPath)
                .appendPath("more"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void tryBlankIp_shouldThrowException() {
        dynamicURL.withIP("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void tryNegativePort_shouldThrowException() {
        dynamicURL.withPort(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void tryHugePort_shouldThrowException() {
        dynamicURL.withPort(Integer.MAX_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void tryBlankPath_shouldThrowException() {
        dynamicURL.withPath("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void tryPathOfSingleSlash_shouldThrowException() {
        dynamicURL.withPath("/");
    }

    @Test(expected = IllegalArgumentException.class)
    public void tryPathWithDoubleSlash_shouldThrowException() {
        dynamicURL.withPath("double//slash");
    }

    @Test
    public void tryAppendingVariable_shouldHaveVariables() {
        assertThat(dynamicURL.appendPath(":var").hasVariables()).isTrue();
    }

    @Test
    public void tryAppendingVariable_shouldHaveThatVariable() {
        assertThat(dynamicURL.appendPath(":var").hasVariable("var")).isTrue();
    }

    @Test
    public void tryAppendingVariable_shouldNotHaveDifferentVariable() {
        assertThat(dynamicURL.appendPath(":var").hasVariable("notVar")).isFalse();
    }

    @Test(expected = IllegalStateException.class)
    public void tryGETWithVariable_shouldThrowException() {
        dynamicURL.GET(":var");
    }

    @Test(expected = IllegalStateException.class)
    public void tryWithValuesWithoutVariables_shouldThrowException() {
        dynamicURL.withNextValue("test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void tryWithTooManyValuesWithVariables_shouldThrowException() {
        dynamicURL.appendPath(":var").withNextValues("test", "whereDoesThisGo");
    }

    @Test
    public void tryWithValueWithVariable_shouldReplaceVariableWithValue() {
        assertThat(dynamicURL.appendPath(":var").withNextValue("test")).isEqualTo(DynamicURL.builder()
                .protocol(twitchProtocol)
                .IP(twitchIP)
                .withPath(twitchPath)
                .appendPath("test"));
    }

}
