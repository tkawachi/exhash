package com.github.tkawachi.exhash;

public interface IExceptionHash {
    String hash(IThrowable throwable) throws ExceptionHashException;
}
