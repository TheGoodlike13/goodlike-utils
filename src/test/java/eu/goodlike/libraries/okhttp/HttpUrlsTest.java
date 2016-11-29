package eu.goodlike.libraries.okhttp;

import com.google.common.collect.ImmutableMap;
import okhttp3.HttpUrl;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpUrlsTest {

    @Test
    public void removingParametersLeavesOtherPartsUntouched() {
        HttpUrl urlWithParams = HttpUrl.parse("https://localhost:8080/some/path?param=value&param2=value2#some_fragment_that_looks_like_param?fake_param=fake_value");
        HttpUrl urlWithoutParams = HttpUrl.parse("https://localhost:8080/some/path#some_fragment_that_looks_like_param?fake_param=fake_value");

        assertThat(HttpUrls.withoutParams(urlWithParams))
                .isEqualTo(urlWithoutParams);
    }

    @Test
    public void httpUrlWithoutPathVariablesReturnedUnchanged() {
        HttpUrl simpleUrl = HttpUrl.parse("https://google.com/");

        assertThat(HttpUrls.insertPathVariables(simpleUrl, ImmutableMap.of()))
                .isEqualTo(simpleUrl);
    }

    @Test
    public void gettingLastPathPart() {
        assertThat(HttpUrls.getLastPathPart(HttpUrl.parse("https://localhost/part")))
                .isEqualTo("part");

        assertThat(HttpUrls.getLastPathPart(HttpUrl.parse("https://localhost/part/")))
                .isEmpty();

        assertThat(HttpUrls.getLastPathPart(HttpUrl.parse("https://localhost/part/file.txt")))
                .isEqualTo("file.txt");

        assertThat(HttpUrls.getLastPathPart(HttpUrl.parse("https://localhost/part?p=v#f")))
                .isEqualTo("part");

        assertThat(HttpUrls.getLastPathPart(HttpUrl.parse("https://localhost/")))
                .isEmpty();
    }

    @Test
    public void gettingLocationOfLastPathPart() {
        assertThat(HttpUrls.getLocationOfLastPathPart(HttpUrl.parse("https://localhost/part")))
                .isEqualTo(HttpUrl.parse("https://localhost/"));

        assertThat(HttpUrls.getLocationOfLastPathPart(HttpUrl.parse("https://localhost/part/")))
                .isEqualTo(HttpUrl.parse("https://localhost/part/"));

        assertThat(HttpUrls.getLocationOfLastPathPart(HttpUrl.parse("https://localhost/part/file.txt")))
                .isEqualTo(HttpUrl.parse("https://localhost/part/"));

        assertThat(HttpUrls.getLocationOfLastPathPart(HttpUrl.parse("https://localhost/part?p=v#f")))
                .isEqualTo(HttpUrl.parse("https://localhost/"));

        assertThat(HttpUrls.getLocationOfLastPathPart(HttpUrl.parse("https://localhost/")))
                .isEqualTo(HttpUrl.parse("https://localhost/"));
    }

}