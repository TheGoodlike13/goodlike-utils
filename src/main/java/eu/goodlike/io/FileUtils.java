package eu.goodlike.io;

import eu.goodlike.neat.Null;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static eu.goodlike.misc.Constants.WORKING_DIRECTORY;

/**
 * Contains utility methods to deal with files
 */
public final class FileUtils {

    /**
     * <pre>
     * Modifies the filename, if needed, based on existing files already present in working directory.
     *
     * Adds (1), (2), etc to the filename, before the extension, until the filename is usable.
     * </pre>
     * @return a filename that is not taken in current working directory
     * @throws NullPointerException if possiblyTakenName is null
     */
    public static String findAvailableName(String possiblyTakenName) {
        return findAvailableName(WORKING_DIRECTORY, possiblyTakenName);
    }

    /**
     * <pre>
     * Modifies the filename, if needed, based on existing files already present in given directory.
     *
     * Adds (1), (2), etc to the filename, before the extension, until the filename is usable.
     * </pre>
     * @return a filename that is not taken in given directory
     * @throws NullPointerException if directory or possiblyTakenName are null
     */
    public static String findAvailableName(String directory, String possiblyTakenName) {
        Null.check(directory, possiblyTakenName).ifAny("Directory and possibly taken name cannot be null");

        String justFilename = com.google.common.io.Files.getNameWithoutExtension(possiblyTakenName);
        String extension = com.google.common.io.Files.getFileExtension(possiblyTakenName);
        String extensionSuffix = "." + extension;

        String workingFilename = possiblyTakenName;
        int copyCount = 0;
        Path path = Paths.get(directory, workingFilename);
        while (Files.exists(path)) {
            if (copyCount == -1)
                throw new AssertionError("All possible file names exhausted");

            workingFilename = justFilename + " (" + ++copyCount + ")";
            if (!extension.isEmpty())
                workingFilename += extensionSuffix;

            path = Paths.get(workingFilename);
        }
        return workingFilename;
    }

    // PRIVATE

    private FileUtils() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
