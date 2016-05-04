package com.github.tkawachi.exhash.core;

public class ArrayStacktrace implements IStacktrace {

    private final StackTraceElement[] elements;
    private final boolean includeLineNumber;

    public ArrayStacktrace(StackTraceElement[] elements, boolean includeLineNumber) {
        this.elements = elements;
        this.includeLineNumber = includeLineNumber;
    }

    @Override
    public String getStacktraceText() {
        final StringBuilder buffer = new StringBuilder();
        StacktraceUtil.appendElements(buffer, elements, includeLineNumber);
        return buffer.toString();
    }
}
