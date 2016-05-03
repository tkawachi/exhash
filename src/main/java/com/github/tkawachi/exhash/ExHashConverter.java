package com.github.tkawachi.exhash;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.CoreConstants;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class ExHashConverter extends ClassicConverter {

    private static final String HASH_LINE_NUMBER_PREFIX = "hashLineNumber=";
    private static final String ALGORITHM_PREFIX = "algorithm=";
    public static final String DEFAULT_ALGORITHM = "MD5";
    public static final boolean DEFAULT_HASH_LINE_NUMBER = false;

    private ExHash exHash = null;

    @Override
    public void start() {
        String algorithm = DEFAULT_ALGORITHM;
        boolean hashLineNumber = DEFAULT_HASH_LINE_NUMBER;

        final List<String> optionList = getOptionList();
        if (optionList != null) {
            for (final String option : optionList) {
                if (option == null) {
                    continue;
                }
                if (option.startsWith(HASH_LINE_NUMBER_PREFIX)) {
                    String value = option.substring(HASH_LINE_NUMBER_PREFIX.length());
                    hashLineNumber = Boolean.valueOf(value);
                } else if (option.startsWith(ALGORITHM_PREFIX)) {
                    algorithm = option.substring(ALGORITHM_PREFIX.length());
                }
            }
        }
        exHash = new ExHash(algorithm, hashLineNumber);

        if (!exHash.isAlgorithmAvailable()) {
            addError("Message digest algorithm " + exHash.getAlgorithm() + " is not available.");
            return;
        }

        super.start();
    }

    @Override
    public String convert(ILoggingEvent event) {
        final IThrowableProxy tp = event.getThrowableProxy();
        if (tp == null) {
            return CoreConstants.EMPTY_STRING;
        }

        try {
            return exHash.hashIThrowableProxy(tp);
        } catch (NoSuchAlgorithmException e) {
            addError("Failed to get message digest algorithm: " + exHash.getAlgorithm(), e);
            return CoreConstants.EMPTY_STRING;
        } catch (UnsupportedEncodingException e) {
            addError("Failed to encode StackTraceElement: " + exHash.getCharset(), e);
            return CoreConstants.EMPTY_STRING;
        }
    }
}
