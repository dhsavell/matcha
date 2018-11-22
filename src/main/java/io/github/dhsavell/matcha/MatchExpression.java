package io.github.dhsavell.matcha;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
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
    private final List<Match<I, O>> matchers;

    private MatchExpression(I matchValue, List<Match<I, O>> matchers) {
        this.matchValue = matchValue;
        this.matchers = matchers;
    }

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

    private static <A, B> MatchExpression<A, B> empty(A matchValue) {
        return new MatchExpression<>(matchValue, new ArrayList<>());
    }

    MatchExpression<I, O> withDefinedMatch(Match<I, O> newMatcher) {
        ArrayList<Match<I, O>> updatedMatchList = new ArrayList<>(matchers);
        updatedMatchList.add(newMatcher);
        return new MatchExpression<>(matchValue, updatedMatchList);
    }

    /**
     * Defines a match based on the given predicate.
     *
     * @param predicate Predicate used to check whether or not a given value is a match.
     * @return A MatchBuilder, used to complete defining the current match.
     */
    public MatchBuilder<I, O> matches(Predicate<I> predicate) {
        return MatchBuilder.newInstance(this, predicate);
    }

    /**
     * Defines a match based on equality with a given object.
     *
     * @param value Value compared with to check if a given input is a match.
     * @return A MatchBuilder, used to complete defining the current match.
     */
    public MatchBuilder<I, O> matches(I value) {
        return matches(value::equals);
    }

    public <A> MatchBuilder<I, O> is(Class<? extends A> typeToMatch) {
        return matches(typeToMatch::isInstance);
    }

    /**
     * Specifies a fallback value if no other matches are met.
     *
     * @param fallbackValue Fallback value to return if no other match succeeds.
     * @return The value matched or the given fallback value (if no other matches succeed).
     */
    public O otherwise(O fallbackValue) {
        return getMatch().orElse(fallbackValue);
    }

    /**
     * Specifies a fallback supplier to return from if no other matches are met.
     *
     * @param fallbackSupplier Fallback supplier to return from if no other match succeeds.
     * @return The value matched or a value from the  given fallback supplier (if no other matches succeed).
     */
    public O otherwise(Supplier<O> fallbackSupplier) {
        return getMatch().orElseGet(fallbackSupplier);
    }

    /**
     * Specifies a fallback function to derive a value from if no other matches are met.
     *
     * @param fallbackProvider Fallback function to return from if no other match succeeds.
     * @return The value matched or a value from the  given fallback supplier (if no other matches succeed).
     */
    public O otherwise(Function<I, O> fallbackProvider) {
        return getMatch().orElse(fallbackProvider.apply(matchValue));
    }

    /**
     * @return An Optional value containing the result of this MatchExpression.
     */
    public Optional<O> getMatch() {
        return matchers.stream()
                .filter(matcher -> matcher.isMatch(matchValue))
                .findFirst()
                .map(matcher -> matcher.getResultFor(matchValue));
    }

    /**
     * MatchExpression.Initial is a class used to define the resulting type of a match expression before beginning
     * pattern matching.
     *
     * @param <I> Type to match against.
     */
    public static final class Initial<I> {
        private final I matchValue;

        private Initial(I matchValue) {
            this.matchValue = matchValue;
        }

        /**
         * Specifies the type that will result from the following MatchExpression.
         *
         * @param resultingType Class representing the type resulting from the following MatchExpression.
         * @param <O> Resulting type.
         * @return A MatchExpression with a set output type.
         */
        public <O> MatchExpression<I, O> matchedTo(Class<? extends O> resultingType) {
            return MatchExpression.empty(matchValue);
        }
    }
}
