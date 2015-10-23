# Library for random stuff

This library contains various simple implementations of things I made at some point somewhere. Here's a list:

## Functional

### Either

Similar to Optional, but can contain either of 2 types (never both, but can contain neither)

    Does not need external libraries

### IOFunction

Simple replacement for Function (or CheckedFunction) when you need an actual Function that throws an IOException

I needed it for Jackson API wrapper

    Does not need external libraries

### Action / CheckedAction

An alternative to Runnable when you want a functional interface which executes arbitrary actions without returning
anything

Runnable carries too much asynchronous context to be used generally, as far as I am concerned

    Does not need external libraries

### Some

    Some.of(someIntFunction).zeroTo(amount)
    
is equivalent to

    IntStream.range(0, amount).mapToObj(someIntFunction).collect(toList());
    
and similar shenanigans

I use this particular stuff with Fake, making it easy to generate a list of fake stuff for testing

    Does not need external libraries

## IO

### StreamReader

It just reads some Reader, or a Reader made from other input stream variants; most libraries have this as an util method,
but what can I say, I like my objects (sometimes!)

## Misc

### CommonControllerLogic

Contains various methods I use in my controllers, i.e. validateId (which can only be positive), etc

    Requires Guava for the time method, since TimeHandler requires it
    compile 'com.google.guava:guava:18.0'

### Constants

Contains various constants that pop up from time to time in my work; why not have them in one place?

    Does not need external libraries

### SpecialUtils

Contains methods that are not generic enough to warrant their own class, but are still fairly relevant

For example: SpecialUtils.equalsJavaMathBigDecimal() which acts like a replacement for Object.equals() for
BigDecimals when their scale is not relevant

    Does not need external libraries

### Scaleless

Just a wrapper for BigDecimals which uses compare() instead of equals() for comparison/hashcode

Intended for use in equals()/hashCode() implementations only

    Does not need external libraries

## Neat

### Str

Neat way to work with StringBuilders; replaces simple conditionals and loops with chained methods calls:

    Str.of(someVarargsArray).andIf(condition, nextVarargsArrayOnlyIfConditionIsTrue).andSome(prefixBeforeEveryElement, someList).toString();

Intended for simple cases only, but can be used partially and so on.

    Does not need external libraries

### ComparableUsing

Just a small silly extension to Comparable, not intended to be used in collections, but rather as a general thing...
in fact, I've removed the extension and just added the method to avoid shenanigans

    Does not need external libraries

### Null

Very neat null checker:

    Null.check(o1, o2, o3).ifAny("o1, o2, o3 can't be null");
    
which throws a NullPointerException, and in its message prints out the array (o1, o2, o3) so you can identify what is
null easily and without hassle

    Does not need external libraries

## Resty

Resty v3, next step from https://github.com/TheGoodlike13/resty

Uses java 8 and is a lot more general in how to use it

Basically, it helps manage URLs and make HTTP requests using a step builder pattern

    Requires Guava for Endpoints, a cache for DynamicURLs (optional, since you can cache yourself)
    Requires Jackson for HttpResponse parsing
    compile 'com.google.guava:guava:18.0'
    compile 'com.fasterxml.jackson.core:jackson-databind:2.6.1'

## Retry

Inspired by https://github.com/alexpanov/retries
    
I didn't like the verbose API and lack of java 8 functional interface support, so I decided to give a try myself

    Does not need external libraries

## Test

### Fake

Very simple way to generate some random "fake" value for testing:

    Fake.name(someId)
    Fake.phone(someId)
    Fake.day(someId)
    
also easy to use with Some.of()
    
    Does not need external libraries
  
## Time
    
Converts between various java time formats

    Time.at(timezone).from(epochMillis).toDateTime()
    Time.convert(localDate).toSqlDate()
    
and many more

    Does not need external libraries
    
## v2.validate
    
Useful way to validate something; all validators in impl package are immutable; they also implement Predicate, which
allows them to be used very flexibly, i.e. using static imports:

    listOfStrings.stream().allMatch(string().not().isNull().not().isBlank());
    string.chars().allMatch(codePoint().isDigit().or().equal(','));
    bigDecimal().not().isPositive().ifInvalid(ZERO).thenDo(invalidBigDecimalList::add);
    
I'll add more validators/validation methods when I think of any useful ones
    
    Requires Guava for CharArrayValidator, IntValidator and LongValidator
    compile 'com.google.guava:guava:18.0'
    
## Validation

Deprecated! Use v2.validate instead!

I don't like annotations, unless they do something REALLY simple; I certainly don't like annotations that try to do
something difficult and just aren't good enough by themselves; I opted for a simpler, java way to validate things:

    Validate.string(someString).not().Null().not().blank().email().ifInvalid(someExceptionSupplier)
    Validate.Int(someInt).Null().or().dayOfMonth().ifInvalid(someExceptionSupplier)
    
feel fairly simple to me :)

    Requires Guava for CharArrayValidator
    compile 'com.google.guava:guava:18.0'

## Libraries

### Jackson

Jackson API wrapper which has:
  * singleton (enum) ObjectMapper with default settings (except that it always includes null)
  * bunch of simple methods for conversion
  
    Json.read(SomeClass.class).from(someSource);
    Json.from(someSource).to(SomeClass.class);
    Json.bytesFrom(someObject);
    
and many more similar methods

    Requires Jackson
    compile 'com.fasterxml.jackson.core:jackson-databind:2.6.1'

### JodaTime

Conversion between various java and joda time classes:
  * uses cached instances for different timezones
  * uses epoch millis as basis for conversion
  
    Time.forZone(someZone).from(anyTimeRepresentation).toEpochMillis()
    Time.convert(someDateType).toSqlDate();
    
and many more similar methods

    Requires JodaTime, Guava for LoadingCache
    compile "joda-time:joda-time:2.8.2"
    compile 'com.google.guava:guava:18.0'

### JooL

Intellij idea seems to whine about Seq.of(T[]) and similar (clashes with Stream.of(T[])), so I made a class that
works around that without explicit need to do it myself

    Requires JooL
    compile 'org.jooq:jool:0.9.7'

### JooQ

Various methods which come up often when using JooQ in code, mostly with queries

    Requires JooQ
    compile 'org.jooq:jooq:3.6.2'

### spring.MockMvc

I got sick of copying this stuff when doing integration tests, so I made a basic wrapper which follows some basic
logic that works for me

    Requires some Spring imports
    compile 'org.springframework:spring-test:4.2.1.RELEASE'
    compile 'org.springframework:spring-web:4.2.1.RELEASE'
    compile 'org.springframework:spring-beans:4.2.1.RELEASE'
    compile 'javax.servlet:javax.servlet-api:3.1.0'

### spring.GMail

Just a simple implementation, almost immediately ready to use in Bean config files

    Requires some Spring imports and javax.mail
    compile 'org.springframework:spring-context-support:4.2.1.RELEASE'
    compile 'javax.mail:javax.mail-api:1.5.4'
