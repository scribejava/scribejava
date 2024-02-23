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
        if (Jaxb230Base64.isAvailable()) {
            return new Jaxb230Base64();
        }
        if (JaxbBase64.isAvailable()) {
            return new JaxbBase64();
        }
        if (CommonsCodecBase64.isAvailable()) {
            return new CommonsCodecBase64();
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

    public static byte[] decode(String string) {
        return getInstance().internalDecode(string);
    }

    public static byte[] decodeUrl(String string) {
        return getInstance().internalDecodeUrl(string);
    }

    protected static String pad(String string) {
        switch (string.length() % 4) {
            case 1: return string + "===";
            case 2: return string + "==";
            case 3: return string + "=";
            default: return string;
        }
    }

    protected static String unpad(String string) {
        int length = string.length();
        while (string.charAt(length - 1) == '=') {
            length--;
        }
        return string.substring(0, length);
    }

    protected static String makeUrlSafe(String string) {
        return unpad(string.replace('+', '-').replace('/', '_'));
    }

    protected static String unmakeUrlSafe(String string) {
        return pad(string.replace('-', '+').replace('_', '/'));
    }

    protected abstract String internalEncode(byte[] bytes);

    protected abstract String internalEncodeUrlWithoutPadding(byte[] bytes);

    protected abstract byte[] internalDecode(String string);

    protected abstract byte[] internalDecodeUrl(String string);
}
