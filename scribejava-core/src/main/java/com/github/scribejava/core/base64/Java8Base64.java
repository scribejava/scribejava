package com.github.scribejava.core.base64;

public class Java8Base64 extends Base64 {

    private static final com.github.scribejava.java8.base64.Java8Base64 JAVA8_BASE64
            = isAvailable() ? new com.github.scribejava.java8.base64.Java8Base64() : null;

    @Override
    protected String internalEncode(byte[] bytes) {
        return JAVA8_BASE64.internalEncode(bytes);
    }

    @Override
    protected String internalEncodeUrlWithoutPadding(byte[] bytes) {
        return JAVA8_BASE64.internalEncodeUrlWithoutPadding(bytes);
    }

    static boolean isAvailable() {
        try {
            Class.forName("java.util.Base64", false, Java8Base64.class.getClassLoader());
            return true;
        } catch (ClassNotFoundException cnfE) {
            return false;
        }
    }
}
