package io.github.dhsavell.matcha.matcher;

import io.github.dhsavell.matcha.MatchContext;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.github.dhsavell.matcha.MatchContext.when;
import static io.github.dhsavell.matcha.matcher.ObjectMatchers.*;

public class ObjectMatcherTests {
    @Test
    public void matchNullValue() {
        String result = MatchContext.<Object, String>when(null)
                .is(nonNullValue(), "Not null!")
                .is(nullValue(), "Null!")
                .getMatch()
                .orElse("No match produced!");

        assertThat(result).isEqualTo("Null!");
    }

    @Test
    public void matchNonNullValue() {
        String result = MatchContext.<Object, String>when(new Object())
                .is(nonNullValue(), "Not null!")
                .is(nullValue(), "Null!")
                .getMatch()
                .orElse("No match produced!");

        assertThat(result).isEqualTo("Not null!");
    }

    @Test
    public void matchInstanceValue() {
        assertThat(describeVehicle(new Car())).startsWith("I'm a car");
        assertThat(describeVehicle(new Airplane())).startsWith("I'm a plane");
        assertThat(describeVehicle(new Spaceship())).startsWith("I'm a spaceship");
    }

    private String describeVehicle(Vehicle v) {
        return MatchContext.<Vehicle, String>when(v)
                .is(instance(Car.class), car -> "I'm a car driving on " + car.getRoad())
                .is(instance(Airplane.class), plane -> "I'm a plane at an altitude of " + plane.getAltitude())
                .is(instance(Spaceship.class), ship -> "I'm a spaceship on the planet " + ship.getPlanet())
                .getMatch()
                .orElse("I have no idea what I am!");
    }

    private interface Vehicle { }
    private static class Car implements Vehicle {
        public String getRoad() {
            return "Route 66";
        }
    }

    private static class Airplane implements Vehicle {
        public double getAltitude() {
            return 10000;
        }
    }

    private static class Spaceship implements Vehicle {
        public String getPlanet() {
            return "Mars";
        }
    }
}
