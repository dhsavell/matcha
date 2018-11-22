package io.github.dhsavell.matcha;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A MatchBuilder is used as an intermediate step within a MatchExpression to
 * build a specific condition.
 *
 * @param <I> Type being matched against.
 * @param <O> Resulting type.
 */
public class MatchBuilder<I, O> {
    private final MatchExpression<I, O> context;
    private final Predicate<I> matcher;

    /**
     * Instantiates a new MatchBuilder in the context of a specified MatchExpression.
     *
     * @param context MatchExpression that will be returned after completing this MatchBuilder.
     * @param matcher Predicate that leads to the condition described by this MatchBuilder.
     * @param <A> Type being matched against.
     * @param <B> Resulting type.
     * @return A new instance of a MatchBuilder.
     */
    public static <A, B> MatchBuilder<A, B> newInstance(MatchExpression<A, B> context, Predicate<A> matcher) {
        return new MatchBuilder<>(context, matcher);
    }

    private MatchBuilder(MatchExpression<I, O> context, Predicate<I> matcher) {
        this.context = context;
        this.matcher = matcher;
    }

    /**
     * Specifies the resulting value for the current match.
     *
     * @param value Value to return if the condition for this match is met.
     * @return A MatchExpression with this matcher applied.
     */
    public MatchExpression<I, O> then(O value) {
        return then(() -> value);
    }

    /**
     * Specifies the resulting value from a supplier for the current match.
     *
     * @param valueSupplier Supplier to return from if the condition for this match is met.
     * @return A MatchExpression with this matcher applied.
     */
    public MatchExpression<I, O> then(Supplier<O> valueSupplier) {
        return context.withDefinedMatch(FunctionalMatch.fromSupplier(matcher, valueSupplier));
    }

    /**
     * Specifies the resulting value from a function of the value being matched against.
     *
     * @param valueProducer Function used to calculate the result if the condition for this match is met.
     * @return A MatchExpression with this matcher applied.
     */
    public MatchExpression<I, O> then(Function<I, O> valueProducer) {
        return context.withDefinedMatch(FunctionalMatch.from(matcher, valueProducer));
    }
}
