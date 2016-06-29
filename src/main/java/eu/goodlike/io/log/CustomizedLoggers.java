package eu.goodlike.io.log;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import eu.goodlike.io.FileAppender;
import eu.goodlike.io.FileUtils;
import eu.goodlike.neat.Null;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

/**
 * Contains basic CustomizedLogger implementations
 */
public final class CustomizedLoggers {

    /**
     * @return logger which logs into all given loggers one after another
     * @throws NullPointerException if manyLoggers is or contains null
     */
    public static CustomizedLogger combine(CustomizedLogger... manyLoggers) {
        Null.checkArray(manyLoggers).ifAny("Many loggers cannot be or contain null");
        return manyLoggers.length == 0
                ? noLogging()
                : combine(ImmutableList.copyOf(manyLoggers));
    }

    /**
     * @return logger which logs into all given loggers one after another
     * @throws NullPointerException if manyLoggers is or contains null
     */
    public static CustomizedLogger combine(List<CustomizedLogger> manyLoggers) {
        Null.checkList(manyLoggers).ifAny("Many loggers cannot be or contain null");
        if (manyLoggers.isEmpty())
            return noLogging();

        List<CustomizedLogger> customizedLoggers = ImmutableList.copyOf(manyLoggers);
        return line -> customizedLoggers.forEach(logger -> logger.logMessage(line));
    }

    /**
     * @return logger which prints output to console
     */
    public static CustomizedLogger forConsole() {
        return CONSOLE_LOGGER;
    }

    /**
     * @return logger which prints output to a file, if this file exists and it can print
     * @throws NullPointerException if filename is null
     */
    public static CustomizedLogger forFile(String filename) {
        Null.check(filename).ifAny("Filename cannot be null");
        return FileUtils.getPath(filename)
                .map(CustomizedLoggers::forFile)
                .orElse(NO_LOGGER);
    }

    /**
     * @return logger which prints output to a file, if this file exists and it can print
     * @throws NullPointerException if path is null
     */
    public static CustomizedLogger forFile(Path path) {
        Null.check(path).ifAny("Path cannot be null");
        return CACHED_FILE_LOGGERS.get(path);
    }

    /**
     * @return logger which prints output to a file, if this file exists and it can print
     * @throws NullPointerException if file is null
     */
    public static CustomizedLogger forFile(File file) {
        Null.check(file).ifAny("File cannot be null");
        return forFile(file.toPath());
    }

    /**
     * @return logger which does not log
     */
    public static CustomizedLogger noLogging() {
        return NO_LOGGER;
    }

    // PRIVATE

    private CustomizedLoggers() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

    private static final CustomizedLogger CONSOLE_LOGGER = System.out::println;
    private static final CustomizedLogger NO_LOGGER = any -> {};

    private static final LoadingCache<Path, CustomizedLogger> CACHED_FILE_LOGGERS = Caffeine.newBuilder()
            .build(path -> FileAppender.ofFile(path).map(CustomizedLoggers::forFileAppender).orElse(NO_LOGGER));

    private static CustomizedLogger forFileAppender(FileAppender fileAppender) {
        return new FileCustomizedLogger(fileAppender);
    }

}
