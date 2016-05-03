package com.github.tkawachi.exhash;

/**
 * IThrowable implementation for standard java.lang.Throwable.
 */
public class StandardThrowable implements IThrowable {

    private final Throwable throwable;

    public StandardThrowable(Throwable throwable) {
        this.throwable = throwable;
    }


    @Override
    public StackTraceElement[] getStackTrace() {
        return throwable.getStackTrace();
    }

    @Override
    public IThrowable getCause() {
        final Throwable cause = throwable.getCause();
        if (cause == null) {
            return null;
        }
        return new StandardThrowable(cause);

    }

    @Override
    public IThrowable[] getSuppressed() {
        final Throwable[] suppressed = throwable.getSuppressed();
        if (suppressed == null) {
            return null;
        }
        final IThrowable[] result = new IThrowable[suppressed.length];
        for (int i = 0; i < suppressed.length; i++) {
            result[i] = new StandardThrowable(suppressed[i]);
        }
        return result;
    }
}
