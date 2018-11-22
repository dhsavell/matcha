package io.github.dhsavell.matcha;

import org.pcollections.PVector;
import org.pcollections.TreePVector;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * As the primary pattern matching context, this class is responsible for tying together various conditions within
 * a pattern matching block and determining a result. It should not be instantiated directly and instead should be
 * built using MatchExpression::when (a static import for this is highly recommended for the sake of fluency).
 *
 * @param <I> Input match type.
 * @param <O> Output match type.
 */
public final class MatchExpression<I, O> {
    private final I matchValue;
    private final PVector<Match<I, O>> matchers;

    /**
     * Begins the matching process by instantiating a new MatchExpression with the given input and output types.
     *
     * @param matchValue Value to match against.
     * @param <A>        Input value type.
     * @return A new MatchExpression object in which new patterns can be added to using the MatchExpression::is methods.
     */
    public static <A> MatchExpression.Initial<A> when(A matchValue) {
        return new MatchExpression.Initial<>(matchValue);
    }

    static <A, B> MatchExpression<A, B> empty(A matchValue) {
        return new MatchExpression<>(matchValue, TreePVector.empty());
    }

    private MatchExpression(I matchValue, PVector<Match<I, O>> matchers) {
        this.matchValue = matchValue;
        this.matchers = matchers;
    }

    MatchExpression<I, O> withDefinedMatch(Match<I, O> newMatcher) {
        return new MatchExpression<>(matchValue, matchers.plus(newMatcher));
    }

    public MatchBuilder<I, O> matches(Predicate<I> predicate) {
        return new MatchBuilder<>(this, predicate);
    }

    public MatchBuilder<I, O> matches(I value) {
        return matches(value::equals);
    }

    public O otherwise(O fallbackValue) {
        return getMatch().orElse(fallbackValue);
    }

    public O otherwise(Supplier<O> fallbackSupplier) {
        return getMatch().orElseGet(fallbackSupplier);
    }

    /**
     * @return Optional value resulting from this MatchExpression.
     */
    public Optional<O> getMatch() {
        return matchers.stream()
                .filter(matcher -> matcher.isMatch(matchValue))
                .findFirst()
                .map(matcher -> matcher.getResultFor(matchValue));
    }

    public static final class Initial<I> {
        private final I matchValue;

        private Initial(I matchValue) {
            this.matchValue = matchValue;
        }

        public <O> MatchExpression<I, O> matchedTo(Class<? extends O> resultingType) {
            return MatchExpression.empty(matchValue);
        }
    }
}
