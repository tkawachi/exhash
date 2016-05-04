package com.github.tkawachi.exhash.core;

/**
 * IStacktrace implementation for IThrowable.
 */
public class ThrowableStacktrace implements IStacktrace {
    private static final String CAUSED_BY = "Caused by:\n";
    private static final String SUPPRESSED = "Suppressed:\n";
    private static final String END_OF_THROWABLE = "----\n";

    private final IThrowable th;
    private final boolean includeLineNumber;

    public ThrowableStacktrace(IThrowable th, boolean includeLineNumber) {
        this.th = th;
        this.includeLineNumber = includeLineNumber;
    }

    public boolean getIncludeLineNumber() {
        return includeLineNumber;
    }

    @Override
    public String getStacktraceText() {
        StringBuilder buffer = new StringBuilder();
        appendStackTraceElements(this.th, buffer);
        return buffer.toString();
    }

    private void appendStackTraceElements(final IThrowable th, final StringBuilder buffer) {
        final StackTraceElement[] elements = th.getStackTrace();
        StacktraceUtil.appendElements(buffer, elements, includeLineNumber);

        final IThrowable cause = th.getCause();
        if (cause != null) {
            buffer.append(CAUSED_BY);
            appendStackTraceElements(cause, buffer);
        }

        final IThrowable[] suppressed = th.getSuppressed();
        if (suppressed != null && suppressed.length > 0) {
            buffer.append(SUPPRESSED);
            for (final IThrowable t : suppressed) {
                appendStackTraceElements(t, buffer);
            }
        }

        buffer.append(END_OF_THROWABLE);
    }

}
