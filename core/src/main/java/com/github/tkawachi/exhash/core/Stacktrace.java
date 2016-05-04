package com.github.tkawachi.exhash.core;

import java.util.ArrayList;
import java.util.List;

public class Stacktrace {
    public static final boolean DEFAULT_INCLUDE_LINE_NUMBER = false;

    public static IStacktrace getInstance(Throwable th) {
        return getInstance(th, DEFAULT_INCLUDE_LINE_NUMBER);
    }

    public static IStacktrace getInstance(Throwable th, boolean includeLineNumbers) {
        return getInstance(new StandardThrowable(th), includeLineNumbers);
    }

    public static IStacktrace getInstance(IThrowable th) {
        return getInstance(th, DEFAULT_INCLUDE_LINE_NUMBER);
    }

    public static IStacktrace getInstance(IThrowable th, boolean includeLineNumbers) {
        return new ThrowableStacktrace(th, includeLineNumbers);
    }

    public static IStacktrace getCurrent() {
        return getCurrent(DEFAULT_INCLUDE_LINE_NUMBER);
    }

    public static IStacktrace getCurrent(boolean includeLineNumbers) {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();

        // Removes this library stack from elements
        List<StackTraceElement> elementsWithoutLib = new ArrayList<>(elements.length);
        String libPkgName = Stacktrace.class.getPackage().getName();

        for (StackTraceElement elem : elements) {
            if (!elem.getClassName().startsWith(libPkgName)) {
                elementsWithoutLib.add(elem);
            }
        }
        return getInstance(elementsWithoutLib.toArray(new StackTraceElement[0]), includeLineNumbers);
    }

    public static IStacktrace getInstance(StackTraceElement[] elements) {
        return getInstance(elements, DEFAULT_INCLUDE_LINE_NUMBER);
    }

    public static IStacktrace getInstance(StackTraceElement[] elements, boolean includeLineNumbers) {
        return new ArrayStacktrace(elements, includeLineNumbers);
    }
}
