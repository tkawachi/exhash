package com.github.tkawachi.exhash;

/**
 * Interface to get StackTraceElements of a Throwable.
 */
public interface IThrowable {
    StackTraceElement[] getStackTrace();

    IThrowable getCause();

    IThrowable[] getSuppressed();
}
