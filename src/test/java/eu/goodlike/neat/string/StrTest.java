package eu.goodlike.neat.string;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StrTest {

    @Before
    public void setup() {
        o1 = "test";
        o2 = 1;
    }

    @Test
    public void tryNoArgs_shouldBeEqualToNoArgsConstructor() {
        assertThat(Str.of()).isEqualTo(new StringBuilderWrapper());
    }

    @Test
    public void trySomeArgs_shouldBeEqualToStringBuilderWithSameArgsConstructor() {
        StringBuilder builder = new StringBuilder().append(o1).append(o2);
        assertThat(Str.of(o1, o2)).isEqualTo(new StringBuilderWrapper(builder));
    }

    @Test
    public void trySomeArgsWithStringBuilder_shouldBeEqualToSameStringBuilderWithSameArgsConstructor() {
        StringBuilder builder1 = new StringBuilder("this is ");
        StringBuilder builder2 = new StringBuilder(builder1).append(o1).append(o2);
        assertThat(Str.of(builder1, o1, o2)).isEqualTo(new StringBuilderWrapper(builder2));
    }

    // PRIVATE

    private Object o1;
    private Object o2;

}
