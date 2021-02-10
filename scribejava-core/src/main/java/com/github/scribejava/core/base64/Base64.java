package com.github.scribejava.core.base64;

public abstract class Base64 {

    private static volatile Base64 instance;

    public static Base64 getInstance() {
        Base64 localInstance = instance;
        if (localInstance == null) {
            synchronized (Base64.class) {
                localInstance = instance;
                if (localInstance == null) {
                    localInstance = createInstance();
                    instance = localInstance;
                }
            }
        }
        return localInstance;
    }

    private static Base64 createInstance() {
        if (Java8Base64.isAvailable()) {
            return new Java8Base64();
        }
        throw new IllegalStateException(
                "No Base64 implementation was provided. Java 8 Base64, Apache Commons Codec or JAXB is needed");
    }

    public static void init(Base64 base64) {
        synchronized (Base64.class) {
            instance = base64;
        }
    }

    public static String encode(byte[] bytes) {
        return getInstance().internalEncode(bytes);
    }

    public static String encodeUrlWithoutPadding(byte[] bytes) {
        return getInstance().internalEncodeUrlWithoutPadding(bytes);
    }

    public static byte[] decodeMime(String string) {
        return getInstance().internalDecodeMime(string);
    }

    protected abstract String internalEncode(byte[] bytes);

    protected abstract String internalEncodeUrlWithoutPadding(byte[] bytes);

    protected abstract byte[] internalDecodeMime(String string);
}
