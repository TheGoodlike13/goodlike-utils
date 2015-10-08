package eu.goodlike.resty;

import eu.goodlike.resty.http.HttpResponse;
import eu.goodlike.resty.url.DynamicURL;
import eu.goodlike.resty.url.StaticURL;
import org.junit.Test;

import java.io.IOException;

public class RestyTest {

    @Test
    public void randomWebsiteTest() throws IOException {
        HttpResponse response = DynamicURL.builder()
                .HTTPS().IP("api.twitch.tv")
                .GET("/kraken")
                .header("Accept", "application/vnd.twitchtv.v3+json")
                .perform();
        System.out.println(response);
    }

    @Test
    public void conversionTest_dynamicToStatic() {
        DynamicURL dynamicURL = DynamicURL.builder()
                .HTTPS().IP("api.twitch.tv").withPath("/kraken");
        System.out.println(dynamicURL);
        StaticURL staticURL = dynamicURL.toStatic();
        System.out.println(staticURL);
    }

    @Test
    public void conversionTest_staticToDynamic() {
        StaticURL staticURL = StaticURL.of("https://api.twitch.tv/kraken");
        System.out.println(staticURL);
        DynamicURL dynamicURL = staticURL.toDynamic();
        System.out.println(dynamicURL);
    }

}
