package io.github.dhsavell.matcha.matcher;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A Matcher based on a given Predicate and Function.
 * @param <I> Input type.
 * @param <O> Output type.
 */
public class FunctionalInterfaceMatcher<I, O> implements Matcher<I, O> {
    private final Predicate<I> matcher;
    private final Function<I, O> result;

    public FunctionalInterfaceMatcher(Predicate<I> matcher, Function<I, O> result) {
        this.matcher = matcher;
        this.result = result;
    }

    @Override
    public boolean isMatch(I input) {
        return matcher.test(input);
    }

    @Override
    public O getResultForInput(I input) {
        return result.apply(input);
    }
}
