package io.github.dhsavell.matcha;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.github.dhsavell.matcha.MatchExpression.when;

public class MatchExpressionTest {

    @Test
    public void getMatchReturnsEmptyWhenUnmatched() {
        assertThat(when(0).matchedTo(Object.class).getMatch().isPresent()).isFalse();
    }

    @Test
    public void otherwiseReturnsFallbackValue() {
        assertThat(when(0).matchedTo(String.class).otherwise("Fallback")).isEqualTo("Fallback");
    }

    @Test
    public void otherwiseReturnsFallbackValueFromSupplier() {
        assertThat(when(0).matchedTo(String.class).otherwise(() -> "Fallback")).isEqualTo("Fallback");
    }

    @Test
    public void correspondingSimpleMatcherIsApplied() {
        String result =
                when(0).matchedTo(String.class)
                        .matches(0).then("Zero")
                        .matches(1).then("One")
                        .otherwise("Number is not zero or one!");

        assertThat(result).isEqualTo("Zero");
    }

    @Test
    public void correspondingComplexMatcherIsApplied() {
        String result =
                when(1024).matchedTo(String.class)
                        .matches(i -> i % 5 == 0).then("Divisible by 5")
                        .matches(i -> i % 2 == 0).then("Divisible by 2")
                        .otherwise("Not sure what the number is divisible by!");

        assertThat(result).isEqualTo("Divisible by 2");
    }

    @Test
    public void matchResultCanBeObtainedFromSupplier() {
        String result =
                when(1).matchedTo(String.class)
                    .matches(i -> i + 1 == 2).then(() -> "The number is 1")
                    .otherwise("Not sure what the number is!");

        assertThat(result).isEqualTo("The number is 1");
    }

    @Test
    public void matchResultCanBeObtainedFromFunction() {
        String result =
                when(1).matchedTo(String.class)
                    .matches(i -> i + 1 == 2).then(i -> i + " plus 1 is 2")
                    .otherwise("Not sure how to describe the number!");

        assertThat(result).isEqualTo("1 plus 1 is 2");
    }
}