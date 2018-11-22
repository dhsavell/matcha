package io.github.dhsavell.matcha.example;

import static io.github.dhsavell.matcha.MatchExpression.when;

public class NumberDescriber {

    public static String describeNumber(Number number) {
        return when(number).matchedTo(String.class)
                .is(Double.class).then(i -> i + " is a double.")
                .matches(n -> (n.intValue() & 1) != 0).then(i -> i + " is odd.")
                .matches(42).then("42 is an interesting number.")
                .otherwise(n -> "I don't have much to say about " + n + ".");
    }

    public static void main(String[] args) {
        System.out.println(describeNumber(0));
        System.out.println(describeNumber(15.1));
        System.out.println(describeNumber(15));
        System.out.println(describeNumber(42));
    }
}