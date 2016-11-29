package eu.goodlike.io;

import java.util.Scanner;

/**
 * Reads next line of input from the user
 */
public final class UserInputReader implements InputReader {

    @Override
    public String readLine() {
        return scanner.nextLine();
    }

    @Override
    public void close() throws Exception {
        // We do not close the scanner because it will close System.in, which is not desirable
    }

    // CONSTRUCTORS

    public UserInputReader() {
        this.scanner = new Scanner(System.in);
    }

    // PRIVATE

    private final Scanner scanner;

}
