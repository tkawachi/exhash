package com.github.tkawachi.exhash;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.CoreConstants;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class ExHashConverter extends ClassicConverter {

    private static final String UTF_8 = "UTF-8";
    private static final char SEPARATOR = '/';
    private static final String HASH_LINE_NUMBER_PREFIX = "hashLineNumber=";
    private static final String ALGORITHM_PREFIX = "algorithm=";

    private String algorithm = "MD5";
    private boolean hashLineNumber = false;

    @Override
    public void start() {
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
        super.start();
    }

    @Override
    public String convert(ILoggingEvent event) {
        final IThrowableProxy tp = event.getThrowableProxy();
        if (tp == null) {
            return CoreConstants.EMPTY_STRING;
        }

        final StringBuilder buffer = new StringBuilder();
        appendStackTraceElements(tp, buffer);
        try {
            return hash(buffer.toString());
        } catch (NoSuchAlgorithmException e) {
            addError("Failed to get algorithm: " + algorithm, e);
            return CoreConstants.EMPTY_STRING;
        } catch (UnsupportedEncodingException e) {
            addError("Failed to encode StackTraceElement: " + UTF_8, e);
            return CoreConstants.EMPTY_STRING;
        }
    }

    private void appendStackTraceElements(final IThrowableProxy tp, final StringBuilder buffer) {
        final StackTraceElementProxy[] elements = tp.getStackTraceElementProxyArray();
        for (final StackTraceElementProxy elem : elements) {
            final StackTraceElement ste = elem.getStackTraceElement();
            buffer.append(ste.getClassName())
                    .append(SEPARATOR)
                    .append(ste.getMethodName())
                    .append(SEPARATOR);
            if (hashLineNumber) {
                buffer.append(ste.getLineNumber())
                        .append(SEPARATOR);
            }
        }

        final IThrowableProxy cause = tp.getCause();
        if (cause != null) {
            appendStackTraceElements(cause, buffer);
        }
    }

    private String hash(final String string) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest md = MessageDigest.getInstance(algorithm);
        final byte[] hashBytes = md.digest(string.getBytes(UTF_8));
        return DatatypeConverter.printHexBinary(hashBytes);
    }
}
