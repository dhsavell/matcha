package io.github.dhsavell.matcha;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class MatchBuilder<I, O> {
    private final MatchExpression<I, O> context;
    private final Predicate<I> matcher;

    public MatchBuilder(MatchExpression<I, O> context, Predicate<I> matcher) {
        this.context = context;
        this.matcher = matcher;
    }

    public MatchExpression<I, O> then(O value) {
        return then(() -> value);
    }

    public MatchExpression<I, O> then(Supplier<O> valueSupplier) {
        return context.withDefinedMatch(FunctionalMatch.fromSupplier(matcher, valueSupplier));
    }

    public MatchExpression<I, O> then(Function<I, O> result) {
        return context.withDefinedMatch(FunctionalMatch.from(matcher, result));
    }
}
