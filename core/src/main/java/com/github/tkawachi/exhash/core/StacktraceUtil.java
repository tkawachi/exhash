package com.github.tkawachi.exhash.core;

class StacktraceUtil {

    public static final char DEFAULT_SEPARATOR_1 = '/';
    public static final char DEFAULT_SEPARATOR_2 = '/';
    public static final char DEFAULT_ELEM_SEPARATOR = '\n';

    static void appendElements(
            final StringBuilder buffer,
            final StackTraceElement[] elements,
            final boolean includeLineNumber,
            final char separator1,
            final char separator2,
            final char elemSeparator) {

        for (final StackTraceElement elem : elements) {
            buffer.append(elem.getClassName())
                    .append(separator1)
                    .append(elem.getMethodName());
            if (includeLineNumber) {
                buffer.append(separator2)
                        .append(elem.getLineNumber());
            }
            buffer.append(elemSeparator);
        }
    }

    static void appendElements(
            final StringBuilder buffer,
            final StackTraceElement[] elements,
            final boolean includeLineNumber) {
        appendElements(buffer, elements, includeLineNumber,
                DEFAULT_SEPARATOR_1, DEFAULT_SEPARATOR_2, DEFAULT_ELEM_SEPARATOR);
    }
}
