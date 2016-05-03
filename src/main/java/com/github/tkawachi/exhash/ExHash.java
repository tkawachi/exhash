package com.github.tkawachi.exhash;

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.classic.spi.ThrowableProxy;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ExHash {
    private static final String CHARSET = "UTF-8";
    private static final char SEPARATOR = '/';

    private final String algorithm;
    private final boolean hashLineNumber;

    public ExHash(final String algorithm, final boolean hashLineNumber) {
        this.algorithm = algorithm;
        this.hashLineNumber = hashLineNumber;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public boolean getHashLineNumber() {
        return hashLineNumber;
    }

    public String getCharset() {
        return CHARSET;
    }

    public String hashIThrowableProxy(final IThrowableProxy tp)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        final StringBuilder buffer = new StringBuilder();
        appendStackTraceElements(tp, buffer);
        return hashString(buffer.toString());
    }

    public String hashThrowable(final Throwable t)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return hashIThrowableProxy(new ThrowableProxy(t));
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

    private String hashString(final String string) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest md = MessageDigest.getInstance(algorithm);
        final byte[] hashBytes = md.digest(string.getBytes(CHARSET));
        return DatatypeConverter.printHexBinary(hashBytes);
    }

    public boolean isAlgorithmAvailable() {
        try {
            MessageDigest.getInstance(algorithm);
            return true;
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }
}
