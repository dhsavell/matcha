package io.github.dhsavell.matcha;

import org.junit.Test;

import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;
import static io.github.dhsavell.matcha.MatchContext.when;

public class MatchContextTest {
    @Test
    public void emptyMatchShouldReturnNone() {
        Object matchResult = when(null).getMatch();
        assertThat(matchResult).isEqualTo(Optional.empty());
    }

    @Test
    public void otherwiseProvidesFallbackValue() {
        String matchResult = MatchContext.<String, String>when("").otherwise("Hello!");
        assertThat(matchResult).isEqualTo("Hello!");
    }

    @Test
    public void otherwiseProvidesFallbackValueFromSupplier() {
        String matchResult = MatchContext.<String, String>when("").otherwise(() -> "Hello!");
        assertThat(matchResult).isEqualTo("Hello!");
    }

    @Test(expected = RuntimeException.class)
    public void otherwiseThrowThrowsExceptionFromSupplier() {
        when(null).otherwiseThrow(RuntimeException::new);
    }
}