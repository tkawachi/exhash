package com.github.tkawachi.exhash.logback;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.CoreConstants;
import com.github.tkawachi.exhash.core.*;

import java.util.List;

public class ExHashConverter extends ClassicConverter {

    public static final String DEFAULT_ALGORITHM = "MD5";
    private static final String INCLUDE_LINE_NUMBER_PREFIX = "includeLineNumber=";
    private static final String ALGORITHM_PREFIX = "algorithm=";
    private IStacktraceHash exHash = null;
    private boolean includeLineNumber = ThrowableStacktrace.DEFAULT_INCLUDE_LINE_NUMBER;

    @Override
    public void start() {
        String algorithm = DEFAULT_ALGORITHM;

        final List<String> optionList = getOptionList();
        if (optionList != null) {
            for (final String option : optionList) {
                if (option == null) {
                    continue;
                }
                if (option.startsWith(INCLUDE_LINE_NUMBER_PREFIX)) {
                    String value = option.substring(INCLUDE_LINE_NUMBER_PREFIX.length());
                    includeLineNumber = Boolean.valueOf(value);
                } else if (option.startsWith(ALGORITHM_PREFIX)) {
                    algorithm = option.substring(ALGORITHM_PREFIX.length());
                }
            }
        }
        final StacktraceHash h = new StacktraceHash(algorithm);
        if (!h.isAlgorithmAvailable()) {
            addError("Message digest algorithm " + h.getAlgorithm() + " is not available.");
            return;
        }
        exHash = h;

        super.start();
    }

    @Override
    public String convert(ILoggingEvent event) {
        final IThrowableProxy tp = event.getThrowableProxy();
        if (tp == null) {
            return CoreConstants.EMPTY_STRING;
        }

        try {
            IStacktrace st = Stacktrace.getInstance(new ThrowableProxyThrowable(tp), includeLineNumber);
            return exHash.hash(st);
        } catch (StacktraceHashException e) {
            addError(e.getMessage(), e);
            return CoreConstants.EMPTY_STRING;
        }
    }
}
