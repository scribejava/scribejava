package com.github.scribejava.core.base64;

import jakarta.xml.bind.DatatypeConverter;

public class JaxbBase64 extends Base64 {

    @Override
    protected String internalEncode(byte[] bytes) {
        return DatatypeConverter.printBase64Binary(bytes);
    }

    @Override
    protected String internalEncodeUrlWithoutPadding(byte[] bytes) {
        return makeUrlSafe(DatatypeConverter.printBase64Binary(bytes));
    }

    @Override
    protected byte[] internalDecode(String string) {
        return DatatypeConverter.parseBase64Binary(string);
    }

    @Override
    protected byte[] internalDecodeUrl(String string) {
        return internalDecode(unmakeUrlSafe(string));
    }

    static boolean isAvailable() {
        try {
            Class.forName("jakarta.xml.bind.DatatypeConverter", false, JaxbBase64.class.getClassLoader());
            return true;
        } catch (ClassNotFoundException cnfE) {
            return false;
        }
    }
}
