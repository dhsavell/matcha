package io.github.dhsavell.matcha;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static com.google.common.truth.Truth.assertThat;
import static io.github.dhsavell.matcha.MatchExpression.when;

public class MatchExpressionTest {

    @Test
    @DisplayName("When given an unmatched value, an empty Optional can be returned")
    public void getMatchReturnsEmptyWhenUnmatched() {
        assertThat(when(0).matchedTo(Object.class).getMatch().isPresent()).isFalse();
    }

    @Test
    @DisplayName("When given an unmatched value, a fallback value can be returned")
    public void otherwiseReturnsFallbackValue() {
        assertThat(when(0).matchedTo(String.class).otherwise("Fallback")).isEqualTo("Fallback");
    }

    @Test
    @DisplayName("When given an unmatched value, a fallback value from a supplier can be returned")
    public void otherwiseReturnsFallbackValueFromSupplier() {
        assertThat(when(0).matchedTo(String.class).otherwise(() -> "Fallback")).isEqualTo("Fallback");
    }

    @Test
    @DisplayName("Between 2 simple matches, the corresponding match should be applied")
    public void correspondingSimpleMatcherIsApplied() {
        String result =
                when(0).matchedTo(String.class)
                        .matches(0).then("Zero")
                        .matches(1).then("One")
                        .otherwise("Number is not zero or one!");

        assertThat(result).isEqualTo("Zero");
    }

    @Test
    @DisplayName("Between 2 customized matches, the corresponding match should be applied")
    public void correspondingComplexMatcherIsApplied() {
        String result =
                when(1024).matchedTo(String.class)
                        .matches(i -> i % 5 == 0).then("Divisible by 5")
                        .matches(i -> i % 2 == 0).then("Divisible by 2")
                        .otherwise("Not sure what the number is divisible by!");

        assertThat(result).isEqualTo("Divisible by 2");
    }

    @Test
    @DisplayName("Match results can be obtained from suppliers")
    public void matchResultFromSupplier() {
        String result =
                when(1).matchedTo(String.class)
                    .matches(i -> i + 1 == 2).then(() -> "The number is 1")
                    .otherwise("Not sure what the number is!");

        assertThat(result).isEqualTo("The number is 1");
    }

    @Test
    @DisplayName("Match results can be derived from functions")
    public void matchResultFromFunction() {
        String result =
                when(1).matchedTo(String.class)
                    .matches(i -> i + 1 == 2).then(i -> i + " plus 1 is 2")
                    .otherwise("Not sure how to describe the number!");

        assertThat(result).isEqualTo("1 plus 1 is 2");
    }
}