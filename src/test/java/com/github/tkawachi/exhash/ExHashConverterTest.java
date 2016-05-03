package com.github.tkawachi.exhash;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class ExHashConverterTest {
    private Logger logger = LoggerFactory.getLogger(ExHashConverterTest.class);

    @Test
    public void testLog() {
        try {
            foo(1);
        } catch (RuntimeException e) {
            logger.info("Hello", e);
        }
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
