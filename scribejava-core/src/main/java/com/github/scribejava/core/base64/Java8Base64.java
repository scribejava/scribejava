package com.github.scribejava.core.base64;

public class Java8Base64 extends Base64 {

    private static final com.github.scribejava.java8.base64.Java8Base64 JAVA8_BASE64
            = new com.github.scribejava.java8.base64.Java8Base64();

    @Override
    protected String internalEncode(byte[] bytes) {
        return JAVA8_BASE64.internalEncode(bytes);
    }

    @Override
    protected String internalEncodeUrlWithoutPadding(byte[] bytes) {
        return JAVA8_BASE64.internalEncodeUrlWithoutPadding(bytes);
    }

    @Override
    protected byte[] internalDecodeMime(String string) {
        return JAVA8_BASE64.internalDecodeMime(string);
    }

}
