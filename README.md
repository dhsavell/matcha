<p align="center">
<img height="75px" alt="Matcha" src="./assets/matcha.png">
</p>

# matcha

[![Build Status](https://travis-ci.com/dhsavell/matcha.svg?branch=master)](https://travis-ci.com/dhsavell/matcha)
[![Coverage Status](https://coveralls.io/repos/github/dhsavell/matcha/badge.svg?branch=master)](https://coveralls.io/github/dhsavell/matcha?branch=master)
[![Maintainability](https://api.codeclimate.com/v1/badges/21f08368aa0fcb290cef/maintainability)](https://codeclimate.com/github/dhsavell/matcha/maintainability)

> Fluent pattern matching for Java.

Matcha is an library providing a pattern-matching system for Java. It primarily
focuses on being fluent, concise, and extendable.

### Example

```java
public class NumberDescriber {
    public static String describeNumber(Number number) {
        return when(number).matchedTo(String.class)
                .is(Double.class).then(i -> i + " is a double.")
                .matches(n -> (n.intValue() & 1) != 0).then(i -> i + " is odd.")
                .matches(42).then("42 is an interesting number.")
                .otherwise(n -> "I don't have much to say about " + n + ".");
    }
}
```

### See also

Here are some other interesting projects that also implement pattern matching
in Java:

 - [johnlcox/motif](https://github.com/johnlcox/motif)
 - [d-plaindoux/suitcase](https://github.com/d-plaindoux/suitcase)
 - [equus52/pattern-matching4j](https://github.com/equus52/pattern-matching4j)
