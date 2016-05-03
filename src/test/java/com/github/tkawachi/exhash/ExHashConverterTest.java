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
            throw new RuntimeException("Foo");
        } catch (RuntimeException e) {
            logger.info("Message", e);
        }
    }
}
