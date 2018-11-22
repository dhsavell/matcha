package io.github.dhsavell.matcha.matcher;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An object containing a Predicate of an "input" type and a Function ("transformer") of an "input" to an "output" type.
 * This is used as a partial {@link IntermediateValueMatcher} which is completed in a
 * {@link io.github.dhsavell.matcha.MatchContext}.
 *
 * @param <I> Input type.
 * @param <T> Intermediate type, used to produce the output value.
 */
public class TransformerPredicate<I, T> {
    private final Predicate<I> predicate;
    private final Function<I, T> transformer;

    public static <A, B> TransformerPredicate<A, B> from(Predicate<A> predicate, Function<A, B> transformer) {
        return new TransformerPredicate<>(predicate, transformer);
    }

    private TransformerPredicate(Predicate<I> predicate, Function<I, T> transformer) {
        this.predicate = predicate;
        this.transformer = transformer;
    }

    public Predicate<I> getPredicate() {
        return predicate;
    }

    public Function<I, T> getTransformer() {
        return transformer;
    }
}
