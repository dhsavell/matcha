package io.github.dhsavell.matcha;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A FunctionalMatch is an implementation of a Match based on a given Predicate and Function.
 *
 * @param <I> Type being matched against.
 * @param <O> Resulting type.
 */
public class FunctionalMatch<I, O> implements Match<I, O> {
    private final Predicate<I> predicate;
    private final Function<I, O> resultProducer;

    /**
     * Instantiates a new FunctionalMatch from a given Predicate and Function.
     *
     * @param predicate Predicate used to determine whether or not a given value matches.
     * @param result Function used to derive a result from a given input.
     * @param <A> Type being matched against.
     * @param <B> Resulting type.
     * @return A FunctionalMatch based on the given predicate and result.
     */
    public static <A, B> FunctionalMatch<A, B> from(Predicate<A> predicate, Function<A, B> result) {
        return new FunctionalMatch<>(predicate, result);
    }

    /**
     * Instantiates a new FunctionalMatch from a given Predicate and Supplier.
     *
     * @param predicate Predicate used to determine whether or not a given value matches.
     * @param result Supplier used to produce results.
     * @param <A> Type being matched against.
     * @param <B> Resulting type.
     * @return A FunctionalMatch based on the given predicate and result.
     */
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
