package eu.goodlike.io;

import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static eu.goodlike.misc.Constants.DEFAULT_CHARSET;
import static org.assertj.core.api.Assertions.assertThat;

public class StreamReaderTest {

    @Before
    public void setup() throws UnsupportedEncodingException {
        testString = "test_string";
        testStringBytes = testString.getBytes(DEFAULT_CHARSET);
    }

    @Test
    public void tryReadingInputStream_shouldReadItsContents() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(testStringBytes);
        String result;
        try (StreamReader reader = StreamReader.of(inputStream)) {
            result = reader.read();
        }
        assertThat(result).isEqualTo(testString);
    }

    @Test
    public void tryReadingReader_shouldReadItsContents() throws IOException {
        Reader stringReader = new StringReader(testString);
        String result;
        try (StreamReader reader = StreamReader.of(stringReader)) {
            result = reader.read();
        }
        assertThat(result).isEqualTo(testString);
    }

    // PRIVATE

    private String testString;
    private byte[] testStringBytes;

}
