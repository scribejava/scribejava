package com.github.scribejava.core.base64;

import javax.xml.bind.DatatypeConverter;

/**
 * JAXB v2.3.0 (the latest for JRE 7)
 */
public class Jaxb230Base64 extends Base64 {

    @Override
    protected String internalEncode(byte[] bytes) {
        return DatatypeConverter.printBase64Binary(bytes);
    }

    @Override
    protected String internalEncodeUrlWithoutPadding(byte[] bytes) {
        return makeUrlSafe(jakarta.xml.bind.DatatypeConverter.printBase64Binary(bytes));
    }

    @Override
    protected byte[] internalDecode(String string) {
        return jakarta.xml.bind.DatatypeConverter.parseBase64Binary(string);
    }

    @Override
    protected byte[] internalDecodeUrl(String string) {
        return internalDecode(unmakeUrlSafe(string));
    }

    static boolean isAvailable() {
        try {
            Class.forName("javax.xml.bind.DatatypeConverter", false, Jaxb230Base64.class.getClassLoader());
            return true;
        } catch (ClassNotFoundException cnfE) {
            return false;
        }
    }
}
