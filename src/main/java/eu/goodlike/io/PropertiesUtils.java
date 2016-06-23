package eu.goodlike.io;

import com.google.common.collect.ImmutableMap;
import eu.goodlike.neat.Null;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * Utility methods to work with Properties
 */
public final class PropertiesUtils {

    /**
     * @return properties parsed from given file, Optional::empty if file is not readable or the reading failed
     * @throws NullPointerException if filename is null
     */
    public static Optional<Properties> fileToProperties(String filename) {
        Null.check(filename).ifAny("Filename cannot be null");
        return fileToProperties(Paths.get(filename));
    }

    /**
     * @return properties parsed from given file, Optional::empty if file is not readable or the reading failed
     * @throws NullPointerException if path is null
     */
    public static Optional<Properties> fileToProperties(Path path) {
        Null.check(path).ifAny("Path cannot be null");
        if (!Files.isReadable(path))
            return Optional.empty();

        FileReader fileReader;
        try {
            fileReader = new FileReader(path.toFile());
        } catch (FileNotFoundException e) {
            return Optional.empty();
        }

        Properties properties = new Properties();
        try {
            properties.load(fileReader);
        } catch (IOException e) {
            return Optional.empty();
        }
        return Optional.of(properties);
    }

    /**
     * @return properties parsed from given file, Optional::empty if file is not readable or the reading failed
     * @throws NullPointerException if file is null
     */
    public static Optional<Properties> fileToProperties(File file) {
        Null.check(file).ifAny("File cannot be null");
        return fileToProperties(file.toPath());
    }

    /**
     * @return immutable map containing all the properties from given properties
     * @throws NullPointerException if properties is null
     */
    public static Map<String, String> propertiesToMap(Properties properties) {
        Null.check(properties).ifAny("Properties cannot be null");
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        properties.stringPropertyNames()
                .forEach(key -> builder.put(key, properties.getProperty(key)));
        return builder.build();
    }

    // PRIVATE

    private PropertiesUtils() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
