package com.github.tkawachi.exhash;

import org.junit.Test;

import static org.junit.Assert.*;

public class ExHashTest {
    @Test
    public void testIsAlgorithmAvailableValid() {
        ExHash h = new ExHash("MD5", false);
        assertTrue(h.isAlgorithmAvailable());
    }

    @Test
    public void testIsAlgorithmAvailableInvalid() {
        ExHash h = new ExHash("InvalidAlgorithm", false);
        assertFalse(h.isAlgorithmAvailable());
    }

    @Test
    public void testHashLineNumberFalse() throws Throwable {
        ExHash h = new ExHash("MD5", false);
        Throwable t1 = null;
        Throwable t2 = null;
        try {
            foo(1);
        } catch (RuntimeException e) {
            t1 = e;
        }
        try {
            foo(2);
        } catch (RuntimeException e) {
            t2 = e;
        }
        assertEquals(h.hashThrowable(t1), h.hashThrowable(t2));
    }

    @Test
    public void testHashLineNumberTrue() throws Throwable {
        ExHash h = new ExHash("MD5", true);
        Throwable t1 = null;
        Throwable t2 = null;
        try {
            foo(1);
        } catch (RuntimeException e) {
            t1 = e;
        }
        try {
            foo(2);
        } catch (RuntimeException e) {
            t2 = e;
        }
        assertNotEquals(h.hashThrowable(t1), h.hashThrowable(t2));
    }

    @Test
    public void testHashCause() throws Throwable {
        ExHash h = new ExHash("MD5", false);
        Throwable t1 = null;
        Throwable t2 = null;
        try {
            foo(1);
        } catch (RuntimeException e) {
            t1 = e;
        }
        try {
            foo(1);
        } catch (RuntimeException e) {
            t2 = e;
        }
        assertEquals(h.hashThrowable(t1), h.hashThrowable(t2));

        // Set a cause to t1. Then hash are different.
        t1.initCause(new RuntimeException("cause"));
        assertNotEquals(h.hashThrowable(t1), h.hashThrowable(t2));
    }


    void foo(int i) {
        bar(i);
    }

    void bar(int i) {
        baz(i);
    }

    void baz(int i) {
        if (i == 1) {
            throw new RuntimeException("i == 1");
        }
        if (i == 2) {
            throw new RuntimeException("i == 2");
        }
    }
}
