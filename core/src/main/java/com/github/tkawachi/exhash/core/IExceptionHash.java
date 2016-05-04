package com.github.tkawachi.exhash.core;

public interface IExceptionHash {
    String hash(IThrowable throwable) throws ExceptionHashException;
}
