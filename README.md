# exhash

[![Build Status](https://travis-ci.org/tkawachi/exhash.svg?branch=master)](https://travis-ci.org/tkawachi/exhash)

A hash of an exception.

When a certain exception with a huge stacktrace occurs, it's cumbersome to search logs
for the same exceptions. This program logs a hash of an exception's stacktrace.
It will be easier to search using the hash.

## Idea

It generates a stack trace string from a `Throwable`. A stack trace string only contains
class names and method names, not including line numbers. By not including line numbers,
the generated hash is tolerant of small code changes like adding a comment line.

```
pkg.Class1/func1
pkg.Class2/func2
Caused by:
pkg.Class3/func3
pkg.Class4/func4
----
----
```

Then it hashes a stack trace string with MD5.

## exhash-core usage

Add a following dependency to build.gradle:

```
dependencies {
  compile 'com.github.tkawachi:exhash-core:0.0.4'
}
```

```java
Throwable th = ...;
IStacktrace stacktrace = Stacktrace.getInstance(th);
IStacktraceHash h = new StacktraceHash();
h.hash(stacktrace); // returns a string hash
```

To include line numbers in a stack trace string, pass true to `includeLineNumbers`
argument.

```java
IStacktrace stacktrace = Stacktrace.getInstance(th, true);
```

A message digest algorithm can be changed by passing `algorithm` constructor argument.
It should be a valid algorithm to call `java.security.MessageDigest()`.
For example, to use SHA-1:

```java
IStacktraceHash h = new StacktraceHash("SHA-1");
```

## exhash-logback usage

Add a following dependency to build.gradle:

```
dependencies {
  compile 'com.github.tkawachi:exhash-logback:0.0.4'
}
```

Add following to `logback.xml` under `<configuration>`

```
<conversionRule conversionWord="exHash"
                converterClass="com.github.tkawachi.exhash.logback.ExHashConverter" />
```

Then `%exHash` can be used in `<pattern>`. For example:

```
<pattern>%date %message exHash=%exHash%n</pattern>
```

The above pattern will outputs something like this:

```
2016-05-04 01:19:21,185 Message exHash=91803EF04EDC4636E6913611D6D6004F
```

MD5 algorithm is used for hashing by default. It can be changed by
passing `algorithm=ALGORITHM` option. `ALGORITHM` should be a valid algorithm
to call `java.security.MessageDigest(ALGORITHM)`. For example, to use SHA-1:

```
<pattern>%date %message exHash=%exHash{algorithm=SHA-1}%n</pattern>
```

Line numbers can be included to generate a hash by passing `includeLineNumber=true`.
For example,

```
<pattern>%date %message exHash=%exHash{includeLineNumber=true}%n</pattern>
```

Multiple options can be passed by separating by comma (`,`).

```
<pattern>%date %message exHash=%exHash{algorithm=SHA-1, includeLineNumber=true}%n</pattern>
```

Length of a hash can be limited by [Format modifies](http://logback.qos.ch/manual/layouts.html#formatModifiers).

```
<pattern>%date %message exHash=%.-7exHash%n</pattern>
```

will output something like this:

```
2016-05-04 01:23:22,916 Message exHash=91803EF
```

[Sample logback.xml used in tests](https://github.com/tkawachi/logback-exhash/blob/master/logback/src/test/resources/logback-test.xml)
