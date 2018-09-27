package io.github.dhsavell.matcha.matcher;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * A collection of Predicates focused around Objects.
 */
public final class ObjectMatchers {
    private ObjectMatchers() {
    }

    /**
     * Determines whether or not a given object is not null.
     *
     * @param <I> Type to use in the returned Predicate.
     * @return Predicate determining whether or not its input is not null.
     */
    public static <I> Predicate<I> nonNullValue() {
        return i -> !Objects.isNull(i);
    }

    /**
     * Determines whether or not a given object is null.
     *
     * @param <I> Type to use in the returned Predicate.
     * @return Predicate determining whether or not its input is null.
     */
    public static <I> Predicate<I> nullValue() {
        return Objects::isNull;
    }

    /**
     * Transformer predicate determining whether or not a given object is an instance of a specified class.
     *
     * @param expectedType Type to check for.
     * @param <I>          Input type of the returned Predicate.
     * @param <T>          Intermediate type used in the {@link TransformerPredicate}.
     * @return TransformerPredicate determining whether or not its input is of a certain type, with the transformer
     * being a cast to that type.
     */
    public static <I, T> TransformerPredicate<I, T> instance(Class<T> expectedType) {
        return new TransformerPredicate<>(expectedType::isInstance, expectedType::cast);
    }

    /**
     * Determines whether or not two objects are equal.
     *
     * @param other Object to check equality of.
     * @param <I>   Input type.
     * @return Predicate wrapping Objects::equals.
     */
    public static <I> Predicate<I> equalTo(I other) {
        return i -> Objects.equals(i, other);
    }

    /**
     * Determines whether or not two objects are not equal.
     *
     * @param other Object to check equality of.
     * @param <I>   Input type.
     * @return Predicate wrapping Objects::equals.
     */
    public static <I> Predicate<I> notEqualTo(I other) {
        return i -> !Objects.equals(i, other);
    }
}
