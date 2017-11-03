package com.github.scribejava.core.services;

/**
 * @deprecated use standard java8 java.util.Base64
 */
@Deprecated
public abstract class Base64Encoder {

    private static class InstanceHolder {
        private static final Base64Encoder INSTANCE = createEncoderInstance();
    }

    public static Base64Encoder getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static Base64Encoder createEncoderInstance() {
        if (CommonsEncoder.isPresent()) {
            return new CommonsEncoder();
        } else {
            return new DatatypeConverterEncoder();
        }
    }

    public static String type() {
        return getInstance().getType();
    }

    public abstract String encode(byte[] bytes);

    public abstract String getType();
}
