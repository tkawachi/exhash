package com.github.tkawachi.exhash.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class ThrowableStacktraceTest {
    @Test
    public void getStacktraceText() throws Exception {
        Throwable th1 = new Exception("th1");
        th1.setStackTrace(new StackTraceElement[]{
                new StackTraceElement("pkg.Class1", "method1", "file1", 1),
                new StackTraceElement("pkg.Class2", "method2", "file2", 2)
        });

        Throwable cause = new Exception("th2");
        cause.setStackTrace(new StackTraceElement[]{
                new StackTraceElement("pkg.Class3", "method3", "file3", 3),
                new StackTraceElement("pkg.Class4", "method4", "file4", 4)
        });

        th1.initCause(cause);
        IStacktrace st = new ThrowableStacktrace(new StandardThrowable(th1));
        String expected =
                "pkg.Class1/method1\n" +
                        "pkg.Class2/method2\n" +
                        "Caused by:\n" +
                        "pkg.Class3/method3\n" +
                        "pkg.Class4/method4\n" +
                        "----\n" +
                        "----\n";
        assertEquals(expected, st.getStacktraceText());
    }

}
