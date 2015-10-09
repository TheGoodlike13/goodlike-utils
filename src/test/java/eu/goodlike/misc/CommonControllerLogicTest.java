package eu.goodlike.misc;

import eu.goodlike.time.TimeResolver;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class CommonControllerLogicTest {

    @Test
    public void tryPositiveId_shouldPass() {
        testLogic.validateId(source, 1, exceptionSupplier);
    }

    @Test(expected = RuntimeException.class)
    public void tryZeroId_shouldThrowGivenException() {
        testLogic.validateId(source, 0, exceptionSupplier);
    }

    @Test(expected = RuntimeException.class)
    public void tryNegativeId_shouldThrowGivenException() {
        testLogic.validateId(source, -1, exceptionSupplier);
    }

    @Test
    public void tryPositivePage_shouldReturnOneLessPage() {
        int page = 1;
        assertThat(testLogic.validatePage(page, exceptionSupplier)).isEqualTo(page - 1);
    }

    @Test
    public void tryNullPage_shouldReturnDefaultPage() {
        assertThat(testLogic.validatePage(null, exceptionSupplier)).isEqualTo(Constants.DEFAULT_PAGE);
    }

    @Test(expected = RuntimeException.class)
    public void tryZeroPage_shouldThrowGivenException() {
        testLogic.validatePage(0, exceptionSupplier);
    }

    @Test(expected = RuntimeException.class)
    public void tryNegativePage_shouldThrowGivenException() {
        testLogic.validatePage(-1, exceptionSupplier);
    }

    @Test
    public void tryPositivePerPage_shouldReturnSamePerPage() {
        int perPage = 1;
        assertThat(testLogic.validatePerPage(perPage, exceptionSupplier)).isEqualTo(perPage);
    }

    @Test
    public void tryNullPerPage_shouldReturnDefaultPerPage() {
        assertThat(testLogic.validatePerPage(null, exceptionSupplier)).isEqualTo(Constants.DEFAULT_PER_PAGE);
    }

    @Test(expected = RuntimeException.class)
    public void tryZeroPerPage_shouldThrowGivenException() {
        testLogic.validatePerPage(0, exceptionSupplier);
    }

    @Test(expected = RuntimeException.class)
    public void tryNegativePerPage_shouldThrowGivenException() {
        testLogic.validatePerPage(-1, exceptionSupplier);
    }

    @Test
    public void tryNonNegativeStartThenEndTime_shouldResolveToStartAndEndTime() {
        long start = 0;
        long end = 1;
        TimeResolver timeFromLogic = testLogic.validateTime(null, null, null, start, end, exceptionSupplier);
        TimeResolver timeToTest = TimeResolver.from(start, end);
        assertThat(timeFromLogic).isEqualTo(timeToTest);
    }

    @Test(expected = RuntimeException.class)
    public void tryNonNegativeEndThenStartTime_shouldThrowGivenException() {
        long start = 1;
        long end = 0;
        testLogic.validateTime(null, null, null, start, end, exceptionSupplier);
    }

    @Test(expected = RuntimeException.class)
    public void tryNegativeStartNonNegativeEndTime_shouldThrowGivenException() {
        long start = -1;
        long end = 0;
        testLogic.validateTime(null, null, null, start, end, exceptionSupplier);
    }

    @Test(expected = RuntimeException.class)
    public void tryNegativeStartNegativeEndTime_shouldThrowGivenException() {
        long start = -2;
        long end = -1;
        testLogic.validateTime(null, null, null, start, end, exceptionSupplier);
    }

    // PRIVATE

    @Before
    public void setup() {
        testLogic = new CommonControllerLogic() {};
        source = "test";
        exceptionSupplier = RuntimeException::new;
    }

    private CommonControllerLogic testLogic;
    private String source;
    private Function<String, RuntimeException> exceptionSupplier;

}
