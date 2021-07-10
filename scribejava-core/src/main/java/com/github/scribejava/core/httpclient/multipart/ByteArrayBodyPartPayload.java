package com.github.scribejava.core.httpclient.multipart;

import java.util.Map;

public class ByteArrayBodyPartPayload extends BodyPartPayload {

    private final byte[] payload;
    private final int off;
    private final int len;

    public ByteArrayBodyPartPayload(byte[] payload, int off, int len, Map<String, String> headers) {
        super(headers);
        this.payload = payload;
        this.off = off;
        this.len = len;
    }

    public ByteArrayBodyPartPayload(byte[] payload, Map<String, String> headers) {
        this(payload, 0, payload.length, headers);
    }

    public ByteArrayBodyPartPayload(byte[] payload, String contentType) {
        this(payload, convertContentTypeToHeaders(contentType));
    }

    public ByteArrayBodyPartPayload(byte[] payload, int off, int len, String contentType) {
        this(payload, off, len, convertContentTypeToHeaders(contentType));
    }

    public ByteArrayBodyPartPayload(byte[] payload) {
        this(payload, (Map<String, String>) null);
    }

    public ByteArrayBodyPartPayload(byte[] payload, int off, int len) {
        this(payload, off, len, (Map<String, String>) null);
    }

    public byte[] getPayload() {
        return payload;
    }

    public int getOff() {
        return off;
    }

    public int getLen() {
        return len;
    }

}
