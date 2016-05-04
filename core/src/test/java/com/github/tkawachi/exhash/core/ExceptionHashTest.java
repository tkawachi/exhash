package com.github.tkawachi.exhash.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class ExceptionHashTest {
    @Test
    public void testIsAlgorithmAvailableValid() {
        ExceptionHash h = new ExceptionHash("MD5", false);
        assertTrue(h.isAlgorithmAvailable());
    }

    @Test
    public void testIsAlgorithmAvailableInvalid() {
        ExceptionHash h = new ExceptionHash("InvalidAlgorithm", false);
        assertFalse(h.isAlgorithmAvailable());
    }

    @Test
    public void testHashLineNumberFalse() throws Throwable {
        ExceptionHash h = new ExceptionHash("MD5", false);
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
        assertEquals(h.hash(new StandardThrowable(t1)), h.hash(new StandardThrowable(t2)));
    }

    @Test
    public void testHashLineNumberTrue() throws Throwable {
        ExceptionHash h = new ExceptionHash("MD5", true);
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
        assertNotEquals(h.hash(new StandardThrowable(t1)), h.hash(new StandardThrowable(t2)));
    }

    @Test
    public void testHashCause() throws Throwable {
        ExceptionHash h = new ExceptionHash("MD5", false);
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
        assertEquals(h.hash(new StandardThrowable(t1)), h.hash(new StandardThrowable(t2)));

        // Set a cause to t1. Then hash are different.
        t1.initCause(new RuntimeException("cause"));
        assertNotEquals(h.hash(new StandardThrowable(t1)), h.hash(new StandardThrowable(t2)));
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
