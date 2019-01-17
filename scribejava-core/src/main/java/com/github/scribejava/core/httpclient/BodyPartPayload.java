package com.github.scribejava.core.httpclient;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @deprecated use {@link com.github.scribejava.core.httpclient.multipart.ByteArrayBodyPartPayload}
 */
@Deprecated
public class BodyPartPayload {

    private final Map<String, String> headers;
    private final byte[] payload;

    public BodyPartPayload(Map<String, String> headers, byte[] payload) {
        this.headers = headers;
        this.payload = payload;
    }

    public BodyPartPayload(String contentDisposition, String contentType, byte[] payload) {
        this(createHeadersMap(contentDisposition, contentType), payload);
    }

    public BodyPartPayload(String contentDisposition, byte[] payload) {
        this(contentDisposition, null, payload);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * @return return
     * @deprecated use {@link #getHeaders() } and then get("Content-Disposition")
     */
    @Deprecated
    public String getContentDisposition() {
        return headers.get("Content-Disposition");
    }

    /**
     * @return return
     * @deprecated use {@link #getHeaders() } and then get("Content-Type")
     */
    @Deprecated
    public String getContentType() {
        return headers.get(HttpClient.CONTENT_TYPE);
    }

    public byte[] getPayload() {
        return payload;
    }

    private static Map<String, String> createHeadersMap(String contentDisposition, String contentType) {
        if (contentDisposition == null && contentType == null) {
            return Collections.emptyMap();
        }

        final Map<String, String> headers = new HashMap<>();
        if (contentDisposition != null) {
            headers.put("Content-Disposition", contentDisposition);
        }
        if (contentType != null) {
            headers.put(HttpClient.CONTENT_TYPE, contentType);
        }
        return headers;
    }
}
