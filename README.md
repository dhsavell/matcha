<p align="center">
<img height="75px" alt="Matcha" src="./assets/matcha.png">
</p>

# matcha

[![Build Status](https://travis-ci.com/dhsavell/matcha.svg?branch=master)](https://travis-ci.com/dhsavell/matcha)
[![Coverage Status](https://coveralls.io/repos/github/dhsavell/matcha/badge.svg?branch=master)](https://coveralls.io/github/dhsavell/matcha?branch=master)
[![Maintainability](https://api.codeclimate.com/v1/badges/21f08368aa0fcb290cef/maintainability)](https://codeclimate.com/github/dhsavell/matcha/maintainability)

> Fluent pattern matching for Java.

Matcha is an library providing a pattern-matching system for Java. It focuses
on fluency and extensibility in order to be both
descriptive and powerful at the same time.

### Why?

Matcha was made purely for fun to experiment with creating a fluent API in
Java. Pattern matching was chosen as the focus of this project because it's an
interesting but very useful concept.

### Examples

These are some basic examples that display Matcha's basic functionality. Every
pattern match in Matcha is an expression that returns an Optional.

First, here's a general look at what pattern matching with Matcha might look
like:

```java
class AnimalDescriber {
    public static String describeAnimal(Animal animal) {
        return MatchContext.<Animal, String>
                when(animal)
                .is(instance(Dog.class), Dog::bark)
                .is(instance(Cat.class), Cat::meow)
                .is(nullValue(), "Null animal!")
                .getMatch()
                .orElse("I don't know what that animal is!");
    }
}
```

Matcha's pattern matching system can easily be extended with anonymous
functions. It's recommended to wrap them with a name, however, to increase
fluency:

```java
class NameDescriber {
    private static Predicate<String> coolName() {
        return s -> s.equalsIgnoreCase("Bob");
    }

    public static String describeName(String name) {
        return MatchContext.<String, String>
                when(name)
                .is(coolName(), "That's a cool name!")
                .is(s -> s.length() % 2 != 0, "That's an odd name.")
                .getMatch()
                .orElse("I don't know how I feel about that name.");
    }
}
```

With a little more effort, types can even be transformed along the way:

```java
class NumberDescriber {
    private static TransformerPredicate<Number, String> intAsHexString() {
        return new TransformerPredicate<>(
                n -> n instanceof Integer,
                n -> Integer.toHexString((Integer) n)
        );
    }

    public static String describeNumber(Number n) {
        return MatchContext.<Number, String>
                when(n)
                .is(intAsHexString(), hex -> "In hex, that number is " + hex)
                .is(instance(Float.class), f -> "That number is a float!")
                .getMatch()
                .orElse("I don't have anything to say about that number.");
    }
}
```

### See also

Here are some other interesting projects that also provide Java pattern
matching solutions:
 - [johnlcox/motif](https://github.com/johnlcox/motif)
 - [d-plaindoux/suitcase](https://github.com/d-plaindoux/suitcase)
 - [equus52/pattern-matching4j](https://github.com/equus52/pattern-matching4j)
