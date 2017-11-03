package com.github.scribejava.core.services;

import javax.xml.bind.DatatypeConverter;

/**
 * @deprecated use standard java8 java.util.Base64
 */
@Deprecated
public class DatatypeConverterEncoder extends Base64Encoder {

    @Override
    public String encode(byte[] bytes) {
        return DatatypeConverter.printBase64Binary(bytes);
    }

    @Override
    public String getType() {
        return "DatatypeConverter";
    }
}
