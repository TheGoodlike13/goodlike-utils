package eu.goodlike.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * <pre>
 * Synchronized appended for files
 *
 * In most cases a properly configured logger is preferred, but for simple file operations, this should suffice
 * </pre>
 */
public final class FileAppender implements AutoCloseable {

    /**
     * Appends given string to the file of this appender
     */
    public synchronized void append(String string) throws IOException {
        bufferedWriter.append(string);
    }

    /**
     * Appends given string and a new line symbol to the file of this appender
     */
    public void appendLine(String string) throws IOException {
        append(string + System.lineSeparator());
    }

    @Override
    public void close() throws Exception {
        bufferedWriter.close();
    }

    // CONSTRUCTORS

    public static Optional<FileAppender> ofFile(String filename) {
        Path path = Paths.get(filename);
        if (!Files.isWritable(path))
            return Optional.empty();

        BufferedWriter bufferedWriter;
        try {
            bufferedWriter = Files.newBufferedWriter(path, UTF_8, CREATE_NEW);
        } catch (IOException e) {
            return Optional.empty();
        }
        return Optional.of(new FileAppender(bufferedWriter));
    }

    private FileAppender(BufferedWriter bufferedWriter) {
        this.bufferedWriter = bufferedWriter;
    }

    // PRIVATE

    private final BufferedWriter bufferedWriter;

}
