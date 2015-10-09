package eu.goodlike.neat;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

public class NullTest {

    @Test
    public void tryNotNull_shouldPass() {
        Null.check(1).ifAny("Should pass");
    }

    @Test
    public void tryNotNullCustom_shouldPass() {
        Null.check(1).ifAny(RuntimeException::new);
    }

    @Test
    public void tryMultipleNotNull_shouldPass() {
        Null.check(1, 2, 3).ifAny("Should pass");
    }

    @Test
    public void tryMultipleNotNullCustom_shouldPass() {
        Null.check(1, 2, 3).ifAny(RuntimeException::new);
    }

    @Test(expected = NullPointerException.class)
    public void tryJustNull_shouldThrowNullPointer() {
        Null.check((Object) null).ifAny("Should throw NullPointerException");
    }

    @Test(expected = RuntimeException.class)
    public void tryJustNullCustom_shouldGivenException() {
        Null.check((Object) null).ifAny(RuntimeException::new);
    }

    @Test(expected = NullPointerException.class)
    public void tryMultipleJustNull_shouldThrowNullPointer() {
        Null.check(null, null, null).ifAny("Should throw NullPointerException");
    }

    @Test(expected = RuntimeException.class)
    public void tryMultipleJustNullCustom_shouldGivenException() {
        Null.check(null, null, null).ifAny(RuntimeException::new);
    }

    @Test(expected = NullPointerException.class)
    public void tryMultipleSomeNull_shouldThrowNullPointer() {
        Null.check(1, null, 3).ifAny("Should throw NullPointerException");
    }

    @Test(expected = RuntimeException.class)
    public void tryMultipleSomeNullCustom_shouldThrowGivenException() {
        Null.check(1, null, 3).ifAny(RuntimeException::new);
    }

    @Test
    public void tryPrimitiveArray_shouldPass() {
        Null.checkAlone(new int[0]).ifAny("Should pass");
    }

    @Test
    public void tryPrimitiveArrayCustom_shouldPass() {
        Null.checkAlone(new int[0]).ifAny(RuntimeException::new);
    }

    @Test(expected = NullPointerException.class)
    public void tryNullAlone_shouldThrowNullPointer() {
        Null.checkAlone(null).ifAny("Should throw NullPointerException");
    }

    @Test(expected = RuntimeException.class)
    public void tryNullAloneCustom_shouldThrowGivenException() {
        Null.checkAlone(null).ifAny(RuntimeException::new);
    }

    @Test
    public void tryArrayWithoutNull_shouldPass() {
        Integer[] ints = {1, 2, 3};
        Null.checkArray(ints).ifAny("Should pass");
    }

    @Test
    public void tryArrayWithoutNullCustom_shouldPass() {
        Integer[] ints = {1, 2, 3};
        Null.checkArray(ints).ifAny(RuntimeException::new);
    }

    @Test(expected = NullPointerException.class)
    public void tryArrayWithNull_shouldThrowNullPointer() {
        Integer[] ints = {1, null, 3};
        Null.checkArray(ints).ifAny("Should throw NullPointerException");
    }

    @Test(expected = RuntimeException.class)
    public void tryArrayWithNullCustom_shouldThrowGivenException() {
        Integer[] ints = {1, null, 3};
        Null.checkArray(ints).ifAny(RuntimeException::new);
    }

    @Test(expected = NullPointerException.class)
    public void tryNullArray_shouldThrowNullPointer() {
        Null.checkArray(null).ifAny("Should throw NullPointerException");
    }

    @Test(expected = RuntimeException.class)
    public void tryNullArrayCustom_shouldThrowGivenException() {
        Null.checkArray(null).ifAny(RuntimeException::new);
    }

    @Test
    public void tryCollectionWithoutNull_shouldPass() {
        Collection<Integer> ints = Arrays.asList(1, 2, 3);
        Null.checkCollection(ints).ifAny("Should pass");
    }

    @Test
    public void tryCollectionWithoutNullCustom_shouldPass() {
        Collection<Integer> ints = Arrays.asList(1, 2, 3);
        Null.checkCollection(ints).ifAny(RuntimeException::new);
    }

    @Test(expected = NullPointerException.class)
    public void tryCollectionWithNull_shouldThrowNullPointer() {
        Collection<Integer> ints = Arrays.asList(1, null, 3);
        Null.checkCollection(ints).ifAny("Should throw NullPointerException");
    }

    @Test(expected = RuntimeException.class)
    public void tryCollectionWithNullCustom_shouldThrowGivenException() {
        Collection<Integer> ints = Arrays.asList(1, null, 3);
        Null.checkCollection(ints).ifAny(RuntimeException::new);
    }

    @Test(expected = NullPointerException.class)
    public void tryCollectionArray_shouldThrowNullPointer() {
        Null.checkCollection(null).ifAny("Should throw NullPointerException");
    }

    @Test(expected = RuntimeException.class)
    public void tryCollectionArrayCustom_shouldThrowGivenException() {
        Null.checkCollection(null).ifAny(RuntimeException::new);
    }

}
