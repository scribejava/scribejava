package com.github.scribejava.java8.base64;

import java.util.Base64;

public class Java8Base64 {

    private static final Base64.Encoder BASE64_ENCODER = Base64.getEncoder();
    private static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();
    private static final Base64.Encoder BASE64_URL_ENCODER_WITHOUT_PADDING = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder BASE64_URL_DECODER = Base64.getUrlDecoder();

    public String internalEncode(byte[] bytes) {
        return BASE64_ENCODER.encodeToString(bytes);
    }

    public String internalEncodeUrlWithoutPadding(byte[] bytes) {
        return BASE64_URL_ENCODER_WITHOUT_PADDING.encodeToString(bytes);
    }

    public byte[] internalDecode(String string) {
        return BASE64_DECODER.decode(string);
    }

    public byte[] internalDecodeUrl(String string) {
        return BASE64_URL_DECODER.decode(string);
    }
}
