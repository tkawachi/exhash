package com.github.tkawachi.exhash.core;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ExceptionHash implements IExceptionHash {
    private static final String CHARSET = "UTF-8";
    private static final char SEPARATOR1 = '/';
    private static final char SEPARATOR2 = '\n';
    private static final String CAUSED_BY = "Caused by:\n";
    private static final String SUPPRESSED = "Suppressed:\n";
    private static final String END_OF_THROWABLE = "----";

    private final String algorithm;
    private final boolean includeLineNumber;

    public ExceptionHash(final String algorithm, final boolean includeLineNumber) {
        this.algorithm = algorithm;
        this.includeLineNumber = includeLineNumber;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public boolean getIncludeLineNumber() {
        return includeLineNumber;
    }

    @Override
    public String hash(final IThrowable t) throws ExceptionHashException {
        return hashString(generateHashSource(t));
    }

    String generateHashSource(final IThrowable t) throws ExceptionHashException {
        StringBuilder buffer = new StringBuilder();
        appendStackTraceElements(t, buffer);
        return buffer.toString();
    }

    private void appendStackTraceElements(final IThrowable th, final StringBuilder buffer) {
        final StackTraceElement[] elements = th.getStackTrace();
        for (final StackTraceElement elem : elements) {
            buffer.append(elem.getClassName())
                    .append(SEPARATOR1)
                    .append(elem.getMethodName());
            if (includeLineNumber) {
                buffer.append(SEPARATOR1)
                        .append(elem.getLineNumber());
            }
            buffer.append(SEPARATOR2);
        }

        final IThrowable cause = th.getCause();
        if (cause != null) {
            buffer.append(CAUSED_BY);
            appendStackTraceElements(cause, buffer);
        }

        final IThrowable[] suppressed = th.getSuppressed();
        if (suppressed != null && suppressed.length > 0) {
            buffer.append(SUPPRESSED);
            for (final IThrowable t : suppressed) {
                appendStackTraceElements(t, buffer);
            }
        }

        buffer.append(END_OF_THROWABLE);
    }

    private String hashString(final String string) throws ExceptionHashException {
        try {
            final MessageDigest md = MessageDigest.getInstance(algorithm);
            final byte[] hashBytes = md.digest(string.getBytes(CHARSET));
            return DatatypeConverter.printHexBinary(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new ExceptionHashException(
                    "Failed to get message digest algorithm: " + getAlgorithm(), e);
        } catch (UnsupportedEncodingException e) {
            throw new ExceptionHashException(
                    "Failed to encode StackTraceElement: " + CHARSET, e);
        }
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
