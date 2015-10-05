package eu.goodlike.io;

import eu.goodlike.neat.Null;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Reads an InputStream or any different kind of Reader
 */
public class StreamReader implements Closeable {

    /**
     * @return String concatenated from the reader of this StreamReader, char by char
     */
    public String read() throws IOException {
        StringBuilder builder = new StringBuilder();
        int c;
        while ((c = reader.read()) != -1)
            builder.append((char) c);

        return builder.toString();
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    // CONSTRUCTORS

    public static StreamReader forInputStream(InputStream input) {
        return forInputStream(input, DEFAULT_CHARSET);
    }

    public static StreamReader forInputStream(InputStream input, Charset charset) {
        return forReader(new BufferedReader(new InputStreamReader(input, charset)));
    }

    public static StreamReader forReader(Reader reader) {
        return new StreamReader(reader);
    }

    private StreamReader(Reader reader) {
        Null.check(reader).ifAny("Reader cannot be null");
        this.reader = reader;
    }

    // PRIVATE

    private final Reader reader;

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

}
