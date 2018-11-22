package io.github.dhsavell.matcha;

/**
 * A Match represents a singular condition in a MatchExpression. For example, the condition "when x is greater than 5,
 * return false" is an example of a Match.
 *
 * @param <I> Type being matched against.
 * @param <O> Resulting type.
 */
public interface Match<I, O> {
    /**
     * Determines whether or not the given input value applies to this Match.
     *
     * @param value Value to test.
     * @return Whether or not the given value is applicable to this Match.
     */
    boolean isMatch(I value);

    /**
     * Gets the result of this Match, given a valid input value.
     *
     * @param value Value to get an output for.
     * @return Resulting value from this Match.
     */
    O getResultFor(I value);
}
