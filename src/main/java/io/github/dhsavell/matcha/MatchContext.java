package io.github.dhsavell.matcha;

import io.github.dhsavell.matcha.matcher.FunctionalInterfaceMatcher;
import io.github.dhsavell.matcha.matcher.IntermediateValueMatcher;
import io.github.dhsavell.matcha.matcher.Matcher;
import io.github.dhsavell.matcha.matcher.TransformerPredicate;
import org.pcollections.PVector;
import org.pcollections.TreePVector;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * As the primary pattern matching context, this class is responsible for tying together various conditions within
 * a pattern matching block and determining a result. It should not be instantiated directly and instead should be
 * built using MatchContext::when (a static import for this is highly recommended for the sake of fluency).
 * <p>
 * This class works in conjunction with various Matchers, which can be found in package
 * {@link io.github.dhsavell.matcha.matcher}. Custom matchers should be named as if they are read after "is" in a
 * sentence.
 *
 * @param <I> Input match type.
 * @param <O> Output match type.
 */
public final class MatchContext<I, O> {
    private final I matchValue;
    private final PVector<Matcher<I, O>> matchers;

    /**
     * Begins the matching process by instantiating a new MatchContext with the given input and output types.
     *
     * @param matchValue Value to match against.
     * @param <A>        Input value type.
     * @param <B>        Output value type.
     * @return A new MatchContext object in which new patterns can be added to using the MatchContext::is methods.
     */
    public static <A, B> MatchContext<A, B> when(A matchValue) {
        return new MatchContext<>(matchValue, TreePVector.empty());
    }

    private MatchContext(I matchValue, PVector<Matcher<I, O>> matchers) {
        this.matchValue = matchValue;
        this.matchers = matchers;
    }

    /**
     * Adds a pattern to match against.
     *
     * @param matcher Matcher (containing the match predicate and result supplier) to add to this MatchContext.
     * @return A new MatchContext with the given matcher applied.
     */
    public MatchContext<I, O> is(Matcher<I, O> matcher) {
        return new MatchContext<>(matchValue, matchers.plus(matcher));
    }

    /**
     * Adds a pattern based on a given predicate and result value.
     *
     * @param pattern Predicate determining whether or not the current input is a match.
     * @param result  Resulting value if this match is triggered.
     * @return A new MatchContext with this matcher applied.
     */
    public MatchContext<I, O> is(Predicate<I> pattern, O result) {
        return is(pattern, i -> result);
    }

    /**
     * Adds a pattern based on a given predicate and result supplier.
     *
     * @param pattern Predicate determining whether or not the current input is a match.
     * @param result  Supplier for producing a result value.
     * @return A new MatchContext with this matcher applied.
     */
    public MatchContext<I, O> is(Predicate<I> pattern, Supplier<O> result) {
        return is(pattern, i -> result.get());
    }

    /**
     * Adds a pattern based on a given predicate and result function.
     *
     * @param pattern Predicate determining whether or not the current input is a match.
     * @param result  Function for producing a result value.
     * @return A new MatchContext with this matcher applied.
     */
    public MatchContext<I, O> is(Predicate<I> pattern, Function<I, O> result) {
        return is(new FunctionalInterfaceMatcher<>(pattern, result));
    }

    /**
     * Adds a pattern based on a pattern, transformer, and result function using the output of the transformer. It can
     * be thought of as a "type pipeline":
     *
     * <pre>
     *      I (type of value being matched against) => T (intermediate type) => O (output type)
     * </pre>
     * <p>
     * For example, consider the following scenario:
     * <p>
     * - interface Vehicle
     * - class Car implements Vehicle and has method getRoad()
     * - class Airplane implements Vehicle and has method getAltitude()
     * - class Spaceship implements Vehicle and has method getPlanet()
     * <p>
     * Using {@link io.github.dhsavell.matcha.matcher.ObjectMatchers#instance(Class)}, this variant of {@code is}
     * could be used like so:
     * <p>
     * {@code
     * // ... within match context ...
     * .is(instance(Car.class), car -> "I'm a car! I'm currently driving on " + car.getCurrentRoad())
     * .is(instance(Airplane.class), airplane -> "I'm an airplane! My altitude is " + airplaine.getAltitude())
     * .is(instance(Spaceship.class), spaceship -> "I'm a spaceship! I am visiting " + spaceship.getPlanet())
     * // ...
     * }
     * <p>
     * In this case, the "type pipeline" from earlier would look like this:
     * <pre>
     *     [ I => T ] => O
     *     Where [ I => T ] is covered by ObjectMatchers::instance
     * </pre>
     *
     * @param patternWithTransformer Pattern and transformer to build an IntermediateValueMatcher from.
     * @param result                 Function for producing a result value.
     * @param <T>                    Intermediate type yielded by the transformer.
     * @return A new MatchContext with this matcher applied.
     */
    public <T> MatchContext<I, O> is(TransformerPredicate<I, T> patternWithTransformer, Function<T, O> result) {
        return is(IntermediateValueMatcher.fromTransformerPredicate(patternWithTransformer, result));
    }

    /**
     * Returns the value resulting from this MatchContext or a given value if no match is found.
     * @param result Fallback value to return if no other conditions are met.
     * @return Value resulting from this MatchContext.
     */
    public O otherwise(O result) {
        return getMatch().orElse(result);
    }

    /**
     * Returns the value resulting from this MatchContext or the result of a given supplier if no match is found.
     * @param result Fallback supplier to return from if no other conditions are met.
     * @return Value resulting from this MatchContext.
     */
    public O otherwise(Supplier<O> result) {
        return getMatch().orElseGet(result);
    }

    /**
     * Returns the value resulting from this MatchContext or throws an exception if no match is found.
     * @param exceptionSupplier Exception supplier to throw from if no other conditions are met.
     * @return Value resulting from this MatchContext.
     * @throws X In the event no match was found.
     */
    public <X extends Throwable> O otherwiseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        return getMatch().orElseThrow(exceptionSupplier);
    }

    /**
     * @return Optional value resulting from this MatchContext.
     */
    public Optional<O> getMatch() {
        return matchers.stream()
                .filter(matcher -> matcher.isMatch(matchValue))
                .findFirst()
                .map(matcher -> matcher.getResultForInput(matchValue));
    }
}
