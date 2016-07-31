package eu.goodlike.libraries.okhttp;

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

}