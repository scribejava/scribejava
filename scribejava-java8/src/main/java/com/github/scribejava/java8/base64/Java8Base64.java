package com.github.scribejava.java8.base64;

public class Java8Base64 {

    private static final java.util.Base64.Encoder BASE64_ENCODER = java.util.Base64.getEncoder();
    private static final java.util.Base64.Encoder BASE64_URL_ENCODER_WITHOUT_PADDING
            = java.util.Base64.getUrlEncoder().withoutPadding();

    public String internalEncode(byte[] bytes) {
        return BASE64_ENCODER.encodeToString(bytes);
    }

    public String internalEncodeUrlWithoutPadding(byte[] bytes) {
        return BASE64_URL_ENCODER_WITHOUT_PADDING.encodeToString(bytes);
    }

}
