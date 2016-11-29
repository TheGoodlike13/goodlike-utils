package eu.goodlike.io;

/**
 * Defines how to read an input
 */
public interface InputReader extends AutoCloseable {

    /**
     * Reads a single line of input
     *
     * @return next line of input
     */
    String readLine();

}
