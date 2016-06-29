package eu.goodlike.io;

import eu.goodlike.neat.Null;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;
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
    public synchronized void close() throws Exception {
        bufferedWriter.close();
    }

    // CONSTRUCTORS

    public static Optional<FileAppender> ofFile(String filename) {
        Null.check(filename).ifAny("Filename cannot be null");
        return FileUtils.getPath(filename).flatMap(FileAppender::ofFile);
    }

    public static Optional<FileAppender> ofFile(Path path) {
        Null.check(path).ifAny("Path cannot be null");

        BufferedWriter bufferedWriter;
        try {
            bufferedWriter = Files.exists(path)
                    ? Files.newBufferedWriter(path, UTF_8, APPEND)
                    : Files.newBufferedWriter(path, UTF_8, CREATE_NEW);
        } catch (IOException e) {
            return Optional.empty();
        }
        return Optional.of(new FileAppender(bufferedWriter));
    }

    public static Optional<FileAppender> ofFile(File file) {
        Null.check(file).ifAny("File cannot be null");
        return ofFile(file.toPath());
    }

    private FileAppender(BufferedWriter bufferedWriter) {
        this.bufferedWriter = bufferedWriter;
    }

    // PRIVATE

    private final BufferedWriter bufferedWriter;

}
