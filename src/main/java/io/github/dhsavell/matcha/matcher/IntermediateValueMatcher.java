package io.github.dhsavell.matcha.matcher;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A Matcher which transforms its input to an intermediate type T before producing an output. In other words, if a
 * normal matcher follows the process I => O, an IntermediateValueMatcher follows the process I => T => O.
 *
 * @param <I> Input type.
 * @param <T> Intermediate type.
 * @param <O> Output type.
 */
public class IntermediateValueMatcher<I, T, O> implements Matcher<I, O> {
    private final Predicate<I> matcher;
    private final Function<I, T> transformer;
    private final Function<T, O> result;

    public static <A, B, C> IntermediateValueMatcher<A, B, C> fromTransformerPredicate(TransformerPredicate<A, B> transformerPredicate, Function<B, C> result) {
        return new IntermediateValueMatcher<>(transformerPredicate.getPredicate(), transformerPredicate.getTransformer(), result);
    }

    public IntermediateValueMatcher(Predicate<I> matcher, Function<I, T> transformer, Function<T, O> result) {
        this.matcher = matcher;
        this.transformer = transformer;
        this.result = result;
    }

    @Override
    public boolean isMatch(I input) {
        return matcher.test(input);
    }

    @Override
    public O getResultForInput(I input) {
        return result.apply(transformer.apply(input));
    }
}
