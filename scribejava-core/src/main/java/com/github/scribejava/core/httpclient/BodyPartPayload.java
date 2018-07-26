package com.github.scribejava.core.httpclient;

public class BodyPartPayload {

    private final String contentDisposition;
    private final String contentType;
    private final byte[] payload;

    public BodyPartPayload(String contentDisposition, String contentType, byte[] payload) {
        this.contentDisposition = contentDisposition;
        this.contentType = contentType;
        this.payload = payload;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getPayload() {
        return payload;
    }

}
