package com.github.tkawachi.exhash.core;

public class Stacktrace {
    public static IStacktrace getInstance(Throwable th) {
        return getInstance(new StandardThrowable(th));
    }

    public static IStacktrace getInstance(Throwable th, boolean includeLineNumbers) {
        return getInstance(new StandardThrowable(th), includeLineNumbers);
    }

    public static IStacktrace getInstance(IThrowable th) {
        return new ThrowableStacktrace(th);
    }

    public static IStacktrace getInstance(IThrowable th, boolean includeLineNumbers) {
        return new ThrowableStacktrace(th, includeLineNumbers);
    }
}
