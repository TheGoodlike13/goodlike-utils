package eu.goodlike.test;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestableSupplierTest {

    private static final String TEST_STRING = "test_string";

    @Test
    public void supplierReturnsValueFromConstructor() {
        TestableSupplier<String> supplier = new TestableSupplier<>(TEST_STRING);

        assertThat(supplier.get())
                .isEqualTo(TEST_STRING);
    }

    @Test
    public void supplierCanTellIfItWasNotQueried() {
        TestableSupplier<String> supplier = new TestableSupplier<>(TEST_STRING);

        assertThat(supplier.hasBeenQueried())
                .isFalse();
    }

    @Test
    public void supplierCanTellIfItWasQueried() {
        TestableSupplier<String> supplier = new TestableSupplier<>(TEST_STRING);
        supplier.get();

        assertThat(supplier.hasBeenQueried())
                .isTrue();
    }

    @Test
    public void supplierCanTellAmountOfTimesItWasQueried() {
        TestableSupplier<String> supplier = new TestableSupplier<>(TEST_STRING);
        supplier.get();
        supplier.get();

        assertThat(supplier.totalTimesQueried())
                .isEqualTo(2);
    }

    @Test
    public void supplierCanPretendToHaveBeenQueriedCertainAmountOfTimes() {
        TestableSupplier<String> supplier = new TestableSupplier<>(TEST_STRING, 10);

        assertThat(supplier.totalTimesQueried())
                .isEqualTo(10);
    }

    @Test
    public void supplierWithPretendTimesQueriedCorrectlyAddsAdditionalRuns() {
        TestableSupplier<String> supplier = new TestableSupplier<>(TEST_STRING, 10);
        supplier.get();

        assertThat(supplier.totalTimesQueried())
                .isEqualTo(11);
    }

    @Test
    public void supplierWrapping() {
        TestableSupplier<String> supplier = new TestableSupplier<>(() -> TEST_STRING);

        assertThat(supplier.get()).isEqualTo(TEST_STRING);
        assertThat(supplier.totalTimesQueried())
                .isEqualTo(1);
    }

}