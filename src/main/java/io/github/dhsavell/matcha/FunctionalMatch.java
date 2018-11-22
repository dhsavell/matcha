package io.github.dhsavell.matcha;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FunctionalMatch<I, O> implements Match<I, O> {
    private final Predicate<I> predicate;
    private final Function<I, O> resultProducer;

    public static <A, B> FunctionalMatch<A, B> from(Predicate<A> predicate, Function<A, B> result) {
        return new FunctionalMatch<>(predicate, result);
    }

    public static <A, B> FunctionalMatch<A, B> fromSupplier(Predicate<A> predicate, Supplier<B> result) {
        return new FunctionalMatch<>(predicate, a -> result.get());
    }

    private FunctionalMatch(Predicate<I> predicate, Function<I, O> resultProducer) {
        this.predicate = predicate;
        this.resultProducer = resultProducer;
    }

    @Override
    public boolean isMatch(I value) {
        return predicate.test(value);
    }

    @Override
    public O getResultFor(I value) {
        return resultProducer.apply(value);
    }
}
