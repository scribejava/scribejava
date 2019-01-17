package com.github.scribejava.core.httpclient.multipart;

import java.util.Map;

public class ByteArrayBodyPartPayload extends BodyPartPayload {

    private final byte[] payload;

    public ByteArrayBodyPartPayload(byte[] payload) {
        this.payload = payload;
    }

    public ByteArrayBodyPartPayload(byte[] payload, String contentType) {
        super(contentType);
        this.payload = payload;
    }

    public ByteArrayBodyPartPayload(byte[] payload, Map<String, String> headers) {
        super(headers);
        this.payload = payload;
    }

    public byte[] getPayload() {
        return payload;
    }
}
