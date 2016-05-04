package com.github.tkawachi.exhash.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class StacktraceHashTest {
    @Test
    public void testIsAlgorithmAvailableValid() {
        StacktraceHash h = new StacktraceHash();
        assertTrue(h.isAlgorithmAvailable());
    }

    @Test
    public void testIsAlgorithmAvailableInvalid() {
        StacktraceHash h = new StacktraceHash("InvalidAlgorithm");
        assertFalse(h.isAlgorithmAvailable());
    }

    @Test
    public void testHashLineNumberFalse() throws Throwable {
        StacktraceHash h = new StacktraceHash(StacktraceHash.DEFAULT_ALGORITHM);
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
        assertEquals(
                h.hash(Stacktrace.getInstance(t1, false)),
                h.hash(Stacktrace.getInstance(t2, false))
        );
    }

    @Test
    public void testHashLineNumberTrue() throws Throwable {
        StacktraceHash h = new StacktraceHash(StacktraceHash.DEFAULT_ALGORITHM);
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
        assertNotEquals(
                h.hash(Stacktrace.getInstance(t1, true)),
                h.hash(Stacktrace.getInstance(t2, true))
        );
    }

    @Test
    public void testHashCause() throws Throwable {
        StacktraceHash h = new StacktraceHash();
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
        assertEquals(
                h.hash(Stacktrace.getInstance(t1)),
                h.hash(Stacktrace.getInstance(t2))
        );

        // Set a cause to t1. Then hash are different.
        if (t1 != null) {
            t1.initCause(new RuntimeException("cause"));
        }
        assertNotEquals(
                h.hash(Stacktrace.getInstance(t1)),
                h.hash(Stacktrace.getInstance(t2))
        );
    }


}
