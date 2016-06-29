package eu.goodlike.io.log;

import eu.goodlike.io.FileAppender;
import eu.goodlike.misc.SpecialUtils;
import eu.goodlike.neat.Null;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

/**
 * CustomizedLogger for a file; in the case where file appender returns an error, logging is stopped
 */
public final class FileCustomizedLogger implements CustomizedLogger {

    @Override
    public void logMessage(String line) {
        if (fileAppender != null)
            synchronized (lock) {
                if (fileAppender != null)
                    try {
                        fileAppender.appendLine(line);
                    } catch (IOException e) {
                        System.out.println("Error occurred while logging to file");
                        e.printStackTrace();
                        fileAppender = null;
                    }
            }
    }

    // CONSTRUCTORS

    public static Optional<FileCustomizedLogger> forFile(String filename) {
        return FileAppender.ofFile(filename).map(FileCustomizedLogger::new);
    }

    public static Optional<FileCustomizedLogger> forFile(Path path) {
        return FileAppender.ofFile(path).map(FileCustomizedLogger::new);
    }

    public static Optional<FileCustomizedLogger> forFile(File file) {
        return FileAppender.ofFile(file).map(FileCustomizedLogger::new);
    }

    public FileCustomizedLogger(FileAppender fileAppender) {
        Null.check(fileAppender).ifAny("File appender cannot be null");

        this.fileAppender = fileAppender;
        SpecialUtils.runOnExit(this::cleanup);
    }

    // PRIVATE

    private volatile FileAppender fileAppender;

    private final Object lock = new Object();

    private void cleanup() {
        if (fileAppender != null)
            synchronized (lock) {
                if (fileAppender != null)
                    try {
                        fileAppender.close();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
            }
    }

}
