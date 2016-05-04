package com.github.tkawachi.exhash.core;

public interface IStacktraceHash {
    String hash(IStacktrace st) throws StacktraceHashException;
}
