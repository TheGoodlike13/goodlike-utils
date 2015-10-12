package eu.goodlike.functional;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

public class EitherTest {

    private final String o1 = "test";
    private final int o2 = 1;
    private final Either<String, Integer> firstKind = Either.ofFirstKind(o1);
    private final Either<String, Integer> secondKind = Either.ofSecondKind(o2);
    private final Either<String, Integer> neitherKind = Either.ofNeitherKind();
    private final Predicate<Object> truePredicate = any -> true;
    private final Predicate<Object> falsePredicate = any -> false;

    private List<Object> actionTester;
    private final String actionString = "action";
    private final Action simpleAction = () -> actionTester.add(actionString);

    @Before
    public void setup() {
        actionTester = new ArrayList<>();
    }

    @Test
    public void tryMakingEitherOfFirstKind_shouldBeOfFirstKind() {
        assertThat(Either.of(o1, null)).isEqualTo(Either.ofFirstKind(o1));
    }

    @Test
    public void tryMakingEitherOfSecondKind_shouldBeOfSecondKind() {
        assertThat(Either.of(null, o2)).isEqualTo(Either.ofSecondKind(o2));
    }

    @Test
    public void tryMakingEitherOfNeitherKind_shouldBeOfNeitherKind() {
        assertThat(Either.of(null, null)).isEqualTo(Either.ofNeitherKind());
    }

    @Test
    public void testNeitherSynonym_shouldBeSame() {
        assertThat(Either.neither()).isEqualTo(Either.ofNeitherKind());
    }

    @Test
    public void testFirstKindWithAndWithoutOptional_shouldBeSame() {
        assertThat(Either.ofFirstKind(Optional.of(o1))).isEqualTo(Either.ofFirstKind(o1));
    }

    @Test
    public void testSecondKindWithAndWithoutOptional_shouldBeSame() {
        assertThat(Either.ofSecondKind(Optional.of(o2))).isEqualTo(Either.ofSecondKind(o2));
    }

    @Test
    public void testEitherKindFirstWithAndWithoutOptional_shouldBeSame() {
        assertThat(Either.of(Optional.of(o1), Optional.empty())).isEqualTo(Either.of(o1, null));
    }

    @Test
    public void testEitherKindSecondWithAndWithoutOptional_shouldBeSame() {
        assertThat(Either.of(Optional.empty(), Optional.of(o2))).isEqualTo(Either.of(null, o2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void tryBothKinds_shouldThrowIllegalArgument() {
        Either.of(o1, o2);
    }

    @Test
    public void tryGettingFirstKindFromFirstKind_shouldReturnValue() {
        assertThat(firstKind.getFirstKind()).isEqualTo(o1);
    }

    @Test(expected = NoSuchElementException.class)
    public void tryGettingFirstKindFromSecondKind_shouldThrowNoSuchElement() {
        secondKind.getFirstKind();
    }

    @Test(expected = NoSuchElementException.class)
    public void tryGettingFirstKindFromNeitherKind_shouldThrowNoSuchElement() {
        neitherKind.getFirstKind();
    }

    @Test(expected = NoSuchElementException.class)
    public void tryGettingSecondKindFromFirstKind_shouldThrowNoSuchElement() {
        firstKind.getSecondKind();
    }

    @Test
    public void tryGettingSecondKindFromSecondKind_shouldReturnValue() {
        assertThat(secondKind.getSecondKind()).isEqualTo(o2);
    }

    @Test(expected = NoSuchElementException.class)
    public void tryGettingSecondKindFromNeitherKind_shouldThrowNoSuchElement() {
        neitherKind.getSecondKind();
    }

    @Test
    public void tryGettingFirstKindOptionalFromFirstKind_shouldContainValue() {
        assertThat(firstKind.getFirstOptional()).isEqualTo(Optional.of(o1));
    }

    @Test
    public void tryGettingFirstKindOptionalFromSecondKind_shouldBeEmpty() {
        assertThat(secondKind.getFirstOptional()).isEqualTo(Optional.empty());
    }

    @Test
    public void tryGettingFirstKindOptionalFromNeitherKind_shouldBeEmpty() {
        assertThat(neitherKind.getFirstOptional()).isEqualTo(Optional.empty());
    }

    @Test
    public void tryGettingSecondKindOptionalFromFirstKind_shouldBeEmpty() {
        assertThat(firstKind.getSecondOptional()).isEqualTo(Optional.empty());
    }

    @Test
    public void tryGettingSecondKindOptionalFromSecondKind_shouldContainValue() {
        assertThat(secondKind.getSecondOptional()).isEqualTo(Optional.of(o2));
    }

    @Test
    public void tryGettingSecondKindOptionalFromNeitherKind_shouldBeEmpty() {
        assertThat(neitherKind.getSecondOptional()).isEqualTo(Optional.empty());
    }

    @Test
    public void testIfFirstKindIsFirstKind_shouldBeTrue() {
        assertThat(firstKind.isFirstKind()).isTrue();
    }

    @Test
    public void testIfSecondKindIsFirstKind_shouldBeFalse() {
        assertThat(secondKind.isFirstKind()).isFalse();
    }

    @Test
    public void testIfNeitherKindIsFirstKind_shouldBeFalse() {
        assertThat(neitherKind.isFirstKind()).isFalse();
    }

    @Test
    public void testIfFirstKindIsSecondKind_shouldBeFalse() {
        assertThat(firstKind.isSecondKind()).isFalse();
    }

    @Test
    public void testIfSecondKindIsSecondKind_shouldBeTrue() {
        assertThat(secondKind.isSecondKind()).isTrue();
    }

    @Test
    public void testIfNeitherKindIsSecondKind_shouldBeFalse() {
        assertThat(neitherKind.isSecondKind()).isFalse();
    }

    @Test
    public void testIfFirstKindIsEitherKind_shouldBeTrue() {
        assertThat(firstKind.isEitherKind()).isTrue();
    }

    @Test
    public void testIfSecondKindIsEitherKind_shouldBeTrue() {
        assertThat(secondKind.isEitherKind()).isTrue();
    }

    @Test
    public void testIfNeitherKindIsEitherKind_shouldBeFalse() {
        assertThat(neitherKind.isEitherKind()).isFalse();
    }

    @Test
    public void testIfFirstKindIsNeitherKind_shouldBeFalse() {
        assertThat(firstKind.isNeitherKind()).isFalse();
    }

    @Test
    public void testIfSecondKindIsNeitherKind_shouldBeFalse() {
        assertThat(secondKind.isNeitherKind()).isFalse();
    }

    @Test
    public void testIfNeitherKindIsNeitherKind_shouldBeTrue() {
        assertThat(neitherKind.isNeitherKind()).isTrue();
    }

    @Test
    public void tryDoingSomethingWithFirstKindWhenFirstKind_shouldDoIt() {
        firstKind.ifFirstKind(actionTester::add);
        assertThat(actionTester).contains(o1);
    }

    @Test
    public void tryDoingSomethingWithFirstKindWhenSecondKind_shouldDoNothing() {
        secondKind.ifFirstKind(actionTester::add);
        assertThat(actionTester).isEmpty();
    }

    @Test
    public void tryDoingSomethingWithFirstKindWhenNeitherKind_shouldDoNothing() {
        neitherKind.ifFirstKind(actionTester::add);
        assertThat(actionTester).isEmpty();
    }

    @Test
    public void tryDoingSomethingWithSecondKindWhenFirstKind_shouldDoNothing() {
        firstKind.ifSecondKind(actionTester::add);
        assertThat(actionTester).isEmpty();
    }

    @Test
    public void tryDoingSomethingWithSecondKindWhenSecondKind_shouldDoIt() {
        secondKind.ifSecondKind(actionTester::add);
        assertThat(actionTester).contains(o2);
    }

    @Test
    public void tryDoingSomethingWithSecondKindWhenNeitherKind_shouldDoNothing() {
        neitherKind.ifSecondKind(actionTester::add);
        assertThat(actionTester).isEmpty();
    }

    @Test
    public void tryDoingSomethingWithEitherKindWhenFirstKind_shouldDoFirstAction() {
        firstKind.ifEitherKind(actionTester::add, actionTester::add);
        assertThat(actionTester).contains(o1);
    }

    @Test
    public void tryDoingSomethingWithEitherKindWhenSecondKind_shouldDoSecondAction() {
        secondKind.ifEitherKind(actionTester::add, actionTester::add);
        assertThat(actionTester).contains(o2);
    }

    @Test
    public void tryDoingSomethingWithEitherKindWhenNeitherKind_shouldDoNothing() {
        neitherKind.ifEitherKind(actionTester::add, actionTester::add);
        assertThat(actionTester).isEmpty();
    }

    @Test
    public void tryDoingSomethingWithNeitherKindWhenFirstKind_shouldDoNothing() {
        firstKind.ifNeitherKind(simpleAction);
        assertThat(actionTester).isEmpty();
    }

    @Test
    public void tryDoingSomethingWithNeitherKindWhenSecondKind_shouldDoNothing() {
        secondKind.ifNeitherKind(simpleAction);
        assertThat(actionTester).isEmpty();
    }

    @Test
    public void tryDoingSomethingWithNeitherKindWhenNeitherKind_shouldDoit() {
        neitherKind.ifNeitherKind(simpleAction);
        assertThat(actionTester).contains(actionString);
    }

    @Test
    public void tryFilteringFirstKindWhenFirstKindWithTruePredicate_shouldRemainTheSame() {
        assertThat(firstKind.filterFirstKind(truePredicate)).isEqualTo(firstKind);
    }

    @Test
    public void tryFilteringFirstKindWhenFirstKindWithFalsePredicate_shouldLoseKind() {
        assertThat(firstKind.filterFirstKind(falsePredicate)).isEqualTo(neitherKind);
    }

    @Test
    public void tryFilteringFirstKindWhenSecondKindWithTruePredicate_shouldRemainTheSame() {
        assertThat(secondKind.filterFirstKind(truePredicate)).isEqualTo(secondKind);
    }

    @Test
    public void tryFilteringFirstKindWhenSecondKindWithFalsePredicate_shouldRemainTheSame() {
        assertThat(secondKind.filterFirstKind(falsePredicate)).isEqualTo(secondKind);
    }

    @Test
    public void tryFilteringFirstKindWhenNeitherKindWithTruePredicate_shouldRemainTheSame() {
        assertThat(neitherKind.filterFirstKind(truePredicate)).isEqualTo(neitherKind);
    }

    @Test
    public void tryFilteringFirstKindWhenNeitherKindWithFalsePredicate_shouldRemainTheSame() {
        assertThat(neitherKind.filterFirstKind(falsePredicate)).isEqualTo(neitherKind);
    }

    @Test
    public void tryFilteringSecondKindWhenFirstKindWithTruePredicate_shouldRemainTheSame() {
        assertThat(firstKind.filterSecondKind(truePredicate)).isEqualTo(firstKind);
    }

    @Test
    public void tryFilteringSecondKindWhenFirstKindWithFalsePredicate_shouldRemainTheSame() {
        assertThat(firstKind.filterSecondKind(falsePredicate)).isEqualTo(firstKind);
    }

    @Test
    public void tryFilteringSecondKindWhenSecondKindWithTruePredicate_shouldRemainTheSame() {
        assertThat(secondKind.filterSecondKind(truePredicate)).isEqualTo(secondKind);
    }

    @Test
    public void tryFilteringSecondKindWhenSecondKindWithFalsePredicate_shouldLoseKind() {
        assertThat(secondKind.filterSecondKind(falsePredicate)).isEqualTo(neitherKind);
    }

    @Test
    public void tryFilteringSecondKindWhenNeitherKindWithTruePredicate_shouldRemainTheSame() {
        assertThat(neitherKind.filterSecondKind(truePredicate)).isEqualTo(neitherKind);
    }

    @Test
    public void tryFilteringSecondKindWhenNeitherKindWithFalsePredicate_shouldRemainTheSame() {
        assertThat(neitherKind.filterSecondKind(falsePredicate)).isEqualTo(neitherKind);
    }

    @Test
    public void tryFilteringEitherKindWhenFirstKindWithTruePredicate_shouldRemainTheSame() {
        assertThat(firstKind.filter(truePredicate, truePredicate)).isEqualTo(firstKind);
    }

    @Test
    public void tryFilteringEitherKindWhenFirstKindWithFalsePredicate_shouldLoseKind() {
        assertThat(firstKind.filter(falsePredicate, falsePredicate)).isEqualTo(neitherKind);
    }

    @Test
    public void tryFilteringEitherKindWhenSecondKindWithTruePredicate_shouldRemainTheSame() {
        assertThat(secondKind.filter(truePredicate, truePredicate)).isEqualTo(secondKind);
    }

    @Test
    public void tryFilteringEitherKindWhenSecondKindWithFalsePredicate_shouldLoseKind() {
        assertThat(secondKind.filter(falsePredicate, falsePredicate)).isEqualTo(neitherKind);
    }

    @Test
    public void tryFilteringEitherKindWhenNeitherKindWithTruePredicate_shouldRemainTheSame() {
        assertThat(neitherKind.filter(truePredicate, truePredicate)).isEqualTo(neitherKind);
    }

    @Test
    public void tryFilteringEitherKindWhenNeitherKindWithFalsePredicate_shouldRemainTheSame() {
        assertThat(neitherKind.filter(falsePredicate, falsePredicate)).isEqualTo(neitherKind);
    }

    @Test
    public void tryMappingFirstKindWhenFirstKind_shouldChange() {
        assertThat(firstKind.mapFirstKind(String::length)).isEqualTo(Either.ofFirstKind(o1.length()));
    }

    @Test
    public void tryMappingFirstKindWhenSecondKind_shouldRemainTheSame() {
        assertThat(secondKind.mapFirstKind(String::length)).isEqualTo(secondKind);
    }

    @Test
    public void tryMappingFirstKindWhenNeitherKind_shouldRemainTheSame() {
        assertThat(neitherKind.mapFirstKind(String::length)).isEqualTo(neitherKind);
    }

    @Test
    public void tryMappingSecondKindWhenFirstKind_shouldRemainTheSame() {
        assertThat(firstKind.mapSecondKind(i -> i * 2)).isEqualTo(firstKind);
    }

    @Test
    public void tryMappingSecondKindWhenSecondKind_shouldChange() {
        assertThat(secondKind.mapSecondKind(i -> i * 2)).isEqualTo(Either.ofSecondKind(o2 * 2));
    }

    @Test
    public void tryMappingSecondKindWhenNeitherKind_shouldRemainTheSame() {
        assertThat(neitherKind.mapSecondKind(i -> i * 2)).isEqualTo(neitherKind);
    }

    @Test
    public void tryMappingEitherKindWhenFirstKind_shouldChange() {
        assertThat(firstKind.map(String::length, i -> i * 2)).isEqualTo(Either.ofFirstKind(o1.length()));
    }

    @Test
    public void tryMappingEitherKindWhenSecondKind_shouldChange() {
        assertThat(secondKind.map(String::length, i -> i * 2)).isEqualTo(Either.ofSecondKind(o2 * 2));
    }

    @Test
    public void tryMappingEitherKindWhenNeitherKind_shouldRemainTheSame() {
        assertThat(neitherKind.map(String::length, i -> i * 2)).isEqualTo(neitherKind);
    }

    @Test
    public void tryMappingIntoFirstKindWhenFirstKind_shouldReturnChangedOptional() {
        assertThat(firstKind.mapFirstKindInto(String::length)).isEqualTo(Optional.of(o1.length()));
    }

    @Test
    public void tryMappingIntoFirstKindWhenSecondKind_shouldBeEmpty() {
        assertThat(secondKind.mapFirstKindInto(String::length)).isEqualTo(Optional.empty());
    }

    @Test
    public void tryMappingIntoFirstKindWhenNeitherKind_shouldBeEmpty() {
        assertThat(neitherKind.mapFirstKindInto(String::length)).isEqualTo(Optional.empty());
    }

    @Test
    public void tryMappingIntoSecondKindWhenFirstKind_shouldBeEmpty() {
        assertThat(firstKind.mapSecondKindInto(i -> i * 2)).isEqualTo(Optional.empty());
    }

    @Test
    public void tryMappingIntoSecondKindWhenSecondKind_shouldReturnChangedOptional() {
        assertThat(secondKind.mapSecondKindInto(i -> i * 2)).isEqualTo(Optional.of(o2 * 2));
    }

    @Test
    public void tryMappingIntoSecondKindWhenNeitherKind_shouldBeEmpty() {
        assertThat(neitherKind.mapSecondKindInto(i -> i * 2)).isEqualTo(Optional.empty());
    }

    @Test
    public void tryMappingIntoEitherKindWhenFirstKind_shouldReturnChangedOptional() {
        assertThat(firstKind.mapInto(String::length, i -> i * 2)).isEqualTo(Optional.of(o1.length()));
    }

    @Test
    public void tryMappingIntoEitherKindWhenSecondKind_shouldReturnChangedOptional() {
        assertThat(secondKind.mapInto(String::length, i -> i * 2)).isEqualTo(Optional.of(o2 * 2));
    }

    @Test
    public void tryMappingIntoEitherKindWhenNeitherKind_shouldBeEmpty() {
        assertThat(neitherKind.mapInto(String::length, i -> i * 2)).isEqualTo(Optional.empty());
    }

    @Test
    public void tryMappingIntoEitherKindWhenFirstKindUsingBiFunction_shouldReturnChangedOptional() {
        assertThat(firstKind.mapInto((str, i) -> str != null ? str.length() : i == null ? 0 : i * 2))
                .isEqualTo(Optional.of(o1.length()));
    }

    @Test
    public void tryMappingIntoEitherKindWhenSecondKindUsingBiFunction_shouldReturnChangedOptional() {
        assertThat(secondKind.mapInto((str, i) -> str != null ? str.length() : i == null ? 0 : i * 2))
                .isEqualTo(Optional.of(o2 * 2));
    }

    @Test
    public void tryMappingIntoEitherKindWhenNeitherKindUsingBiFunction_shouldReturnChangedOptional() {
        assertThat(neitherKind.mapInto((str, i) -> str != null ? str.length() : i == null ? 0 : i * 2))
                .isEqualTo(Optional.of(0));
    }

    @Test
    public void tryFlatMappingEitherKindWhenFirstKindUsingBiFunction_shouldReturnChangedEither() {
        assertThat(firstKind.flatMap((str, i) -> Either.of(str, i).map(String::length, j -> j * 2)))
                .isEqualTo(Either.ofFirstKind(o1.length()));
    }

    @Test
    public void tryFlatMappingEitherKindWhenSecondKindUsingBiFunction_shouldReturnChangedEither() {
        assertThat(secondKind.flatMap((str, i) -> Either.of(str, i).map(String::length, j -> j * 2)))
                .isEqualTo(Either.ofSecondKind(o2 * 2));
    }

    @Test
    public void tryFlatMappingEitherKindWhenNeitherKindUsingBiFunction_shouldReturnNeither() {
        assertThat(neitherKind.flatMap((str, i) -> Either.of(str, i).map(String::length, j -> j * 2)))
                .isEqualTo(Either.neither());
    }

    @Test
    public void tryFlatMappingIntoFirstKindWhenFirstKind_shouldReturnChangedOptional() {
        assertThat(firstKind.flatMapFirstKindInto(str -> Optional.of(str).map(String::length)))
                .isEqualTo(Optional.of(o1.length()));
    }

    @Test
    public void tryFlatMappingIntoFirstKindWhenSecondKind_shouldBeEmpty() {
        assertThat(secondKind.flatMapFirstKindInto(str -> Optional.of(str).map(String::length)))
                .isEqualTo(Optional.empty());
    }

    @Test
    public void tryFlatMappingIntoFirstKindWhenNeitherKind_shouldBeEmpty() {
        assertThat(neitherKind.flatMapFirstKindInto(str -> Optional.of(str).map(String::length)))
                .isEqualTo(Optional.empty());
    }

    @Test
    public void tryFlatMappingIntoSecondKindWhenFirstKind_shouldBeEmpty() {
        assertThat(firstKind.flatMapSecondKindInto(i -> Optional.of(i).map(j -> j * 2)))
                .isEqualTo(Optional.empty());
    }

    @Test
    public void tryFlatMappingIntoSecondKindWhenSecondKind_shouldReturnChangedOptional() {
        assertThat(secondKind.flatMapSecondKindInto(i -> Optional.of(i).map(j -> j * 2)))
                .isEqualTo(Optional.of(o2 * 2));
    }

    @Test
    public void tryFlatMappingIntoSecondKindWhenNeitherKind_shouldBeEmpty() {
        assertThat(neitherKind.flatMapSecondKindInto(i -> Optional.of(i).map(j -> j * 2)))
                .isEqualTo(Optional.empty());
    }

    @Test
    public void tryFlatMappingIntoEitherKindWhenFirstKindUsingBiFunction_shouldReturnChangedOptional() {
        assertThat(firstKind.flatMapInto((str, i) -> str != null ? Optional.of(str.length()) : i == null ? Optional.empty() : Optional.of(i * 2)))
                .isEqualTo(Optional.of(o1.length()));
    }

    @Test
    public void tryFlatMappingIntoEitherKindWhenSecondKindUsingBiFunction_shouldReturnChangedOptional() {
        assertThat(secondKind.flatMapInto((str, i) -> str != null ? Optional.of(str.length()) : i == null ? Optional.empty() : Optional.of(i * 2)))
                .isEqualTo(Optional.of(o2 * 2));
    }

    @Test
    public void tryFlatMappingIntoEitherKindWhenNeitherKindUsingBiFunction_shouldReturnEmptyOptional() {
        assertThat(neitherKind.flatMapInto((str, i) -> str != null ? Optional.of(str.length()) : i == null ? Optional.empty() : Optional.of(i * 2)))
                .isEqualTo(Optional.empty());
    }

    @Test
    public void tryFlatMappingIntoEitherKindWhenFirstKind_shouldReturnChangedOptional() {
        assertThat(firstKind.flatMapInto(str -> Optional.of(str).map(String::length), i -> Optional.of(i).map(j -> j * 2)))
                .isEqualTo(Optional.of(o1.length()));
    }

    @Test
    public void tryFlatMappingIntoEitherKindWhenSecondKind_shouldReturnChangedOptional() {
        assertThat(secondKind.flatMapInto(str -> Optional.of(str).map(String::length), i -> Optional.of(i).map(j -> j * 2)))
                .isEqualTo(Optional.of(o2 * 2));
    }

    @Test
    public void tryFlatMappingIntoEitherKindWhenNeitherKind_shouldReturnChangedOptional() {
        assertThat(neitherKind.flatMapInto(str -> Optional.of(str).map(String::length), i -> Optional.of(i).map(j -> j * 2)))
                .isEqualTo(Optional.empty());
    }

    @Test
    public void tryFirstOrElseWhenFirstKind_shouldGetFirst() {
        assertThat(firstKind.firstOrElse("not first")).isEqualTo(o1);
    }

    @Test
    public void tryFirstOrElseWhenSecondKind_shouldGetElse() {
        assertThat(secondKind.firstOrElse("not first")).isEqualTo("not first");
    }

    @Test
    public void tryFirstOrElseWhenNeitherKind_shouldGetElse() {
        assertThat(neitherKind.firstOrElse("not first")).isEqualTo("not first");
    }

    @Test
    public void tryFirstOrSupplyWhenFirstKind_shouldGetFirst() {
        assertThat(firstKind.firstOrElseGet(() -> "not first")).isEqualTo(o1);
    }

    @Test
    public void tryFirstOrSupplyWhenSecondKind_shouldSupply() {
        assertThat(secondKind.firstOrElseGet(() -> "not first")).isEqualTo("not first");
    }

    @Test
    public void tryFirstOrSupplyWhenNeitherKind_shouldSupply() {
        assertThat(neitherKind.firstOrElseGet(() -> "not first")).isEqualTo("not first");
    }

    @Test
    public void tryFirstOrThrowWhenFirstKind_shouldPass() {
        firstKind.firstOrElseThrow(RuntimeException::new);
    }

    @Test(expected = RuntimeException.class)
    public void tryFirstOrThrowWhenSecondKind_shouldThrowGivenException() {
        secondKind.firstOrElseThrow(RuntimeException::new);
    }

    @Test(expected = RuntimeException.class)
    public void tryFirstOrThrowWhenNeitherKind_shouldThrowGivenException() {
        neitherKind.firstOrElseThrow(RuntimeException::new);
    }

    @Test
    public void trySecondOrElseWhenFirstKind_shouldGetElse() {
        assertThat(firstKind.secondOrElse(-o2)).isEqualTo(-o2);
    }

    @Test
    public void trySecondOrElseWhenSecondKind_shouldGetSecond() {
        assertThat(secondKind.secondOrElse(-o2)).isEqualTo(o2);
    }

    @Test
    public void trySecondOrElseWhenNeitherKind_shouldGetElse() {
        assertThat(neitherKind.secondOrElse(-o2)).isEqualTo(-o2);
    }

    @Test
    public void trySecondOrSupplyWhenFirstKind_shouldSupply() {
        assertThat(firstKind.secondOrElseGet(() -> -o2)).isEqualTo(-o2);
    }

    @Test
    public void trySecondOrSupplyWhenSecondKind_shouldGetSecond() {
        assertThat(secondKind.secondOrElseGet(() -> -o2)).isEqualTo(o2);
    }

    @Test
    public void trySecondOrSupplyWhenNeitherKind_shouldSupply() {
        assertThat(neitherKind.secondOrElseGet(() -> -o2)).isEqualTo(-o2);
    }

    @Test(expected = RuntimeException.class)
    public void trySecondOrThrowWhenFirstKind_shouldThrowGivenException() {
        firstKind.secondOrElseThrow(RuntimeException::new);
    }

    @Test
    public void trySecondOrThrowWhenSecondKind_shouldPass() {
        secondKind.secondOrElseThrow(RuntimeException::new);
    }

    @Test(expected = RuntimeException.class)
    public void trySecondOrThrowWhenNeitherKind_shouldThrowGivenException() {
        neitherKind.secondOrElseThrow(RuntimeException::new);
    }

    @Test
    public void tryIfNeitherThrowWhenFirstKind_shouldThrowPass() {
        firstKind.ifNeitherThrow(RuntimeException::new);
    }

    @Test
    public void tryIfNeitherThrowWhenSecondKind_shouldThrowPass() {
        secondKind.ifNeitherThrow(RuntimeException::new);
    }

    @Test(expected = RuntimeException.class)
    public void tryIfNeitherThrowWhenNeitherKind_shouldThrowPass() {
        neitherKind.ifNeitherThrow(RuntimeException::new);
    }

}
