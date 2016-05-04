package com.github.tkawachi.exhash.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class ExceptionHashTest {
    @Test
    public void testIsAlgorithmAvailableValid() {
        ExceptionHash h = new ExceptionHash();
        assertTrue(h.isAlgorithmAvailable());
    }

    @Test
    public void testIsAlgorithmAvailableInvalid() {
        ExceptionHash h = new ExceptionHash("InvalidAlgorithm", ExceptionHash.DEFAULT_INCLUDE_LINE_NUMBER);
        assertFalse(h.isAlgorithmAvailable());
    }

    @Test
    public void testHashLineNumberFalse() throws Throwable {
        ExceptionHash h = new ExceptionHash(ExceptionHash.DEFAULT_ALGORITHM, false);
        Throwable t1 = null;
        Throwable t2 = null;
        try {
            new TestClass().foo(1);
        } catch (RuntimeException e) {
            t1 = e;
        }
        try {
            new TestClass().foo(2);
        } catch (RuntimeException e) {
            t2 = e;
        }
        assertEquals(h.hash(new StandardThrowable(t1)), h.hash(new StandardThrowable(t2)));
    }

    @Test
    public void testHashLineNumberTrue() throws Throwable {
        ExceptionHash h = new ExceptionHash(ExceptionHash.DEFAULT_ALGORITHM, true);
        Throwable t1 = null;
        Throwable t2 = null;
        try {
            new TestClass().foo(1);
        } catch (RuntimeException e) {
            t1 = e;
        }
        try {
            new TestClass().foo(2);
        } catch (RuntimeException e) {
            t2 = e;
        }
        assertNotEquals(h.hash(new StandardThrowable(t1)), h.hash(new StandardThrowable(t2)));
    }

    @Test
    public void testHashCause() throws Throwable {
        ExceptionHash h = new ExceptionHash();
        Throwable t1 = null;
        Throwable t2 = null;
        try {
            new TestClass().foo(1);
        } catch (RuntimeException e) {
            t1 = e;
        }
        try {
            new TestClass().foo(1);
        } catch (RuntimeException e) {
            t2 = e;
        }
        assertEquals(h.hash(new StandardThrowable(t1)), h.hash(new StandardThrowable(t2)));

        // Set a cause to t1. Then hash are different.
        if (t1 != null) {
            t1.initCause(new RuntimeException("cause"));
        }
        assertNotEquals(h.hash(new StandardThrowable(t1)), h.hash(new StandardThrowable(t2)));
    }

    @Test
    public void testGenerateHashSoure() throws Throwable {
        ExceptionHash h = new ExceptionHash();
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
        String expected =
                "pkg.Class1/method1\n" +
                        "pkg.Class2/method2\n" +
                        "Caused by:\n" +
                        "pkg.Class3/method3\n" +
                        "pkg.Class4/method4\n" +
                        "----\n" +
                        "----\n";
        assertEquals(expected, h.generateHashSource(new StandardThrowable(th1)));
    }


}
