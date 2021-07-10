package com.github.scribejava.core.httpclient.multipart;

import com.github.scribejava.core.httpclient.HttpClient;
import java.util.Collections;
import java.util.Map;

public abstract class BodyPartPayload {

    private final Map<String, String> headers;

    public BodyPartPayload() {
        this((Map<String, String>) null);
    }

    public BodyPartPayload(String contentType) {
        this(convertContentTypeToHeaders(contentType));
    }

    public BodyPartPayload(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    protected static Map<String, String> convertContentTypeToHeaders(String contentType) {
        return contentType == null ? null : Collections.singletonMap(HttpClient.CONTENT_TYPE, contentType);
    }
}
