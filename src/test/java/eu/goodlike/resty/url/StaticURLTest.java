package eu.goodlike.resty.url;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StaticURLTest {

    private final String twitchURL = "https://api.twitch.tv/kraken/";
    private final String fakeParams = "?fake_param=fake";
    private final String fakeURL = twitchURL + fakeParams;

    private StaticURL twitchAPI;

    @Before
    public void setup() {
        twitchAPI = StaticURL.of(fakeURL);
    }

    @Test
    public void tryGettingUrl_shouldBeEqualToSeed() {
        assertThat(twitchAPI.getUrl()).isEqualTo(fakeURL);
    }

    @Test
    public void tryToDynamic_shouldTrimParams() {
        assertThat(twitchAPI.toDynamic()).isEqualTo(DynamicURL.of(twitchURL));
    }

}
