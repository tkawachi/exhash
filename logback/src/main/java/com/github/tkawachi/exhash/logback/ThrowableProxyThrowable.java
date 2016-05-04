package com.github.tkawachi.exhash.logback;

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import com.github.tkawachi.exhash.core.IThrowable;

/**
 * IThrowable implementation of logback's IThrowableProxy.
 */
public class ThrowableProxyThrowable implements IThrowable {
    private final IThrowableProxy proxy;

    public ThrowableProxyThrowable(IThrowableProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        final StackTraceElementProxy[] elements = proxy.getStackTraceElementProxyArray();
        if (elements == null) {
            return null;
        }
        final StackTraceElement[] result = new StackTraceElement[elements.length];
        for (int i = 0; i < elements.length; i++) {
            result[i] = elements[i].getStackTraceElement();
        }
        return result;
    }

    @Override
    public IThrowable getCause() {
        return null;
    }

    @Override
    public IThrowable[] getSuppressed() {
        return new IThrowable[0];
    }
}
