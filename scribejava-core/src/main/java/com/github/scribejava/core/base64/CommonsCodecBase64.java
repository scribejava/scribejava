package com.github.scribejava.core.base64;

public class CommonsCodecBase64 extends Base64 {

    private static final org.apache.commons.codec.binary.Base64 BASE64_ENCODER;
    private static final org.apache.commons.codec.binary.Base64 BASE64_URL_ENCODER_WITHOUT_PADDING;

    static {
        if (isAvailable()) {
            BASE64_ENCODER = new org.apache.commons.codec.binary.Base64();
            BASE64_URL_ENCODER_WITHOUT_PADDING = new org.apache.commons.codec.binary.Base64(0, null, true);
        } else {
            BASE64_ENCODER = null;
            BASE64_URL_ENCODER_WITHOUT_PADDING = null;
        }
    }

    @Override
    protected String internalEncode(byte[] bytes) {
        return BASE64_ENCODER.encodeToString(bytes);
    }

    @Override
    protected String internalEncodeUrlWithoutPadding(byte[] bytes) {
        return BASE64_URL_ENCODER_WITHOUT_PADDING.encodeToString(bytes);
    }

    static boolean isAvailable() {
        try {
            Class.forName("org.apache.commons.codec.binary.Base64", false, CommonsCodecBase64.class.getClassLoader());
            return true;
        } catch (ClassNotFoundException cnfE) {
            return false;
        }
    }
}
