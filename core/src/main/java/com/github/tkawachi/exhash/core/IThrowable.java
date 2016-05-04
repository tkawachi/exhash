package com.github.tkawachi.exhash.core;

/**
 * Interface to get StackTraceElements of a Throwable.
 */
public interface IThrowable {
    StackTraceElement[] getStackTrace();

    IThrowable getCause();

    IThrowable[] getSuppressed();
}
