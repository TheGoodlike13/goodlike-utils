package eu.goodlike.cmd;

import org.junit.After;
import org.junit.Test;

public class CommandLineRunnerTest {

    private final ProcessRunner runner = new CommandLineRunner();

    @After
    public void tearDown() throws Exception {
        runner.close();
    }

    @Test
    public void processIsLaunched() {
        runner.execute("ping", "localhost")
                .orElseThrow(() -> new AssertionError("Ping process could not be launched"));
    }

}
