package eu.goodlike.cmd;

import eu.goodlike.functional.ImmutableCollectors;
import eu.goodlike.neat.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static eu.goodlike.validate.CommonValidators.NOT_BLANK;

/**
 * Executes given commands as if they are passed into cmd or its equivalents
 */
public final class CommandLineRunner implements ProcessRunner {

    @Override
    public Optional<Process> execute(String command, String... args) {
        assertCommandValid(command);
        Null.checkArray(args).as("args");
        return execute(command, Stream.of(args));
    }

    @Override
    public Optional<Process> execute(String command, List<String> args) {
        assertCommandValid(command);
        Null.checkList(args).as("args");
        return execute(command, args.stream());
    }

    @Override
    public void close() {
        // No resources to release
    }

    // PRIVATE

    private void assertCommandValid(String command) {
        Null.check(command).as("command");
        NOT_BLANK.ifInvalid(command)
                .thenThrow(() -> new IllegalArgumentException("Cannot be blank: " + command));
    }

    private Optional<Process> execute(String command, Stream<String> args) {
        List<String> input = Stream.concat(Stream.of(command), args.filter(NOT_BLANK))
                .collect(ImmutableCollectors.toList());

        ProcessBuilder processBuilder = new ProcessBuilder()
                .command(input)
                .redirectErrorStream(true);

        Process process = null;
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            String inputString = input.stream().collect(Collectors.joining(" "));
            LOG.error("Failed to start process for input: {}", inputString, e);
        }

        return Optional.ofNullable(process);
    }

    private static final Logger LOG = LoggerFactory.getLogger(CommandLineRunner.class);

}
