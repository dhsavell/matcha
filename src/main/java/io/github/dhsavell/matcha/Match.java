package io.github.dhsavell.matcha;

public interface Match<I, O> {
    boolean isMatch(I value);
    O getResultFor(I value);
}
