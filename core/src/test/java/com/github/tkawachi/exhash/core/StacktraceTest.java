package com.github.tkawachi.exhash.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class StacktraceTest {
    @Test
    public void testGetCurrent() throws Exception {
        String txt1 = Stacktrace.getCurrent().getStacktraceText();
        String txt2 = Stacktrace.getCurrent(Stacktrace.DEFAULT_INCLUDE_LINE_NUMBER).getStacktraceText();
        assertEquals(txt1, txt2);
    }

}
