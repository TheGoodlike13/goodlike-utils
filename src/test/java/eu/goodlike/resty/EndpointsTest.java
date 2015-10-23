package eu.goodlike.resty;

import eu.goodlike.resty.url.DynamicURL;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EndpointsTest {

    private final String twitchURL = "https://api.twitch.tv/kraken/";
    private final DynamicURL twitchURLDynamic = DynamicURL.of(twitchURL);

    private Endpoints twitchAPI;

    @Before
    public void setup() {
        twitchAPI = Endpoints.of(twitchURL);
    }

    @Test
    public void tryAtTwice_shouldBeSameInstance() {
        assertThat(twitchAPI.at()).isSameAs(twitchAPI.at());
    }

    @Test
    public void tryAtTwiceWithPath_shouldBeSameInstance() {
        assertThat(twitchAPI.at("channel/")).isSameAs(twitchAPI.at("channel/"));
    }

    @Test
    public void tryAt_shouldBeEqualToSeedDynamicURL() {
        assertThat(twitchAPI.at()).isEqualTo(twitchURLDynamic);
    }

    @Test
    public void tryAtWithPath_shouldBeEqualToSeedAppendedPath() {
        assertThat(twitchAPI.at("channel/")).isEqualTo(twitchURLDynamic.appendPath("channel/"));
    }

}
