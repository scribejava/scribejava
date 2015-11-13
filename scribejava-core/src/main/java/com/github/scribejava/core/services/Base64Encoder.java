package com.github.scribejava.core.services;

public abstract class Base64Encoder {

    private static Base64Encoder instance;

    public static synchronized Base64Encoder getInstance() {
        if (instance == null) {
            instance = createEncoderInstance();
        }
        return instance;
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
