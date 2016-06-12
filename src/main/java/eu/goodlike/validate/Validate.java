package eu.goodlike.validate;

import eu.goodlike.validate.impl.*;
import eu.goodlike.validate.primitive.PrimitiveDoubleValidator;
import eu.goodlike.validate.primitive.PrimitiveIntValidator;
import eu.goodlike.validate.primitive.PrimitiveLongValidator;

/**
 * <pre>
 * Static methods which can be used with static imports to simplify creation of Validator objects on the fly
 *
 * It is recommended to cache/pre-load all Validators!
 * </pre>
 */
public final class Validate {

    public static BigDecimalValidator bigDecimal() {
        return new BigDecimalValidator();
    }

    public static BooleanValidator aBoolean() {
        return new BooleanValidator();
    }

    public static DoubleValidator aDouble() {
        return new DoubleValidator();
    }

    public static IntValidator anInt() {
        return new IntValidator();
    }

    public static LongValidator aLong() {
        return new LongValidator();
    }

    public static PrimitiveDoubleValidator aPrimDouble() {
        return new PrimitiveDoubleValidator();
    }

    public static PrimitiveIntValidator aPrimInt() {
        return new PrimitiveIntValidator();
    }

    public static PrimitiveLongValidator aPrimLong() {
        return new PrimitiveLongValidator();
    }

    public static PrimitiveIntValidator codePoint() {
        return aPrimInt();
    }

    public static StringValidator string() {
        return new StringValidator();
    }

    public static <T> ObjectValidator<T> a(Class<T> clazz) {
        return new ObjectValidator<>();
    }

    public static <T> OptionalValidator<T> optional(Class<T> clazz) {
        return new OptionalValidator<>();
    }

    public static <T> CollectionValidator<T> collection(Class<T> clazz) {
        return new CollectionValidator<>();
    }

    public static <K, V> MapValidator<K, V> map(Class<K> keyClass, Class<V> valueClass) {
        return new MapValidator<>();
    }

    public static final class Collections {
        public static CollectionValidator<Character> chars() {
            return collection(Character.class);
        }

        public static CollectionValidator<Integer> ints() {
            return collection(Integer.class);
        }

        public static CollectionValidator<Integer> codePoints() {
            return ints();
        }

        public static CollectionValidator<Long> longs() {
            return collection(Long.class);
        }

        public static CollectionValidator<String> strings() {
            return collection(String.class);
        }

        // PRIVATE

        private Collections() {
            throw new AssertionError("Do not instantiate, use static methods!");
        }
    }

    public static final class Maps {
        public static MapValidator<String, String> stringToString() {
            return map(String.class, String.class);
        }

        // PRIVATE

        private Maps() {
            throw new AssertionError("Do not instantiate, use static methods!");
        }
    }

    // PRIVATE

    private Validate() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
