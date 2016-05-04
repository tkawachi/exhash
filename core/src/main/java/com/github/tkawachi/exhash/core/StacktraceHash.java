package com.github.tkawachi.exhash.core;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StacktraceHash implements IStacktraceHash {
    public static final String DEFAULT_ALGORITHM = "MD5";
    private static final String CHARSET = "UTF-8";

    private final String algorithm;

    public StacktraceHash(final String algorithm) {
        this.algorithm = algorithm;
    }

    public StacktraceHash() {
        this(DEFAULT_ALGORITHM);
    }

    public String getAlgorithm() {
        return algorithm;
    }


    @Override
    public String hash(final IStacktrace st) throws StacktraceHashException {
        return hashString(st.getStacktraceText());
    }

    private String hashString(final String string) throws StacktraceHashException {
        try {
            final MessageDigest md = MessageDigest.getInstance(algorithm);
            final byte[] hashBytes = md.digest(string.getBytes(CHARSET));
            return DatatypeConverter.printHexBinary(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new StacktraceHashException(
                    "Failed to get message digest algorithm: " + getAlgorithm(), e);
        } catch (UnsupportedEncodingException e) {
            throw new StacktraceHashException(
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
