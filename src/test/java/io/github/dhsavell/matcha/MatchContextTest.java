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
}