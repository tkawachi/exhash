# logback-exhash

Log a hash of an exception.

When a certain exception occurs, it's cumbersome to search logs for the same 
exceptions. This program logs a hash of an exception's stacktrace.
It will be easier to search using the hash.

## Usage

(TODO Publish .jar and write how to add a dependency)

Add following to `logback.xml` under `<configuration>`

```
<conversionRule conversionWord="exHash"
                converterClass="com.github.tkawachi.exhash.ExHashConverter" />
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

Line numbers in a stacktrace are not hashed by default. By eliminating
line numbers, the hash is tolerant of small code changes like adding a
comment line. Line numbers can be included by passing `hashLineNumber=true`.
For example,

```
<pattern>%date %message exHash=%exHash{hashLineNumber=true}%n</pattern>
```

Multiple options can be passed by separating by comma (`,`).

```
<pattern>%date %message exHash=%exHash{algorithm=SHA-1, hashLineNumber=true}%n</pattern>
```

Length of a hash can be limited by [Format modifies](http://logback.qos.ch/manual/layouts.html#formatModifiers).

```
<pattern>%date %message exHash=%.-7exHash%n</pattern>
```

will output something like this:

```
2016-05-04 01:23:22,916 Message exHash=91803EF
```
