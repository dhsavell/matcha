package io.github.dhsavell.matcha.matcher;

/**
 * Represents a singular matcher that maps an input pattern to an output.
 *
 * @param <I> Input type to match.
 * @param <O> Output type.
 */
public interface Matcher<I, O> {
    /**
     * Determines whether or not a given input of type I is a match.
     *
     * @param input Input to test.
     * @return Whether or not the given input is a match.
     */
    boolean isMatch(I input);

    /**
     * Gets the output value for a given input.
     *
     * @param input Input to test.
     * @return Output value for the given input.
     */
    O getResultForInput(I input);
}
