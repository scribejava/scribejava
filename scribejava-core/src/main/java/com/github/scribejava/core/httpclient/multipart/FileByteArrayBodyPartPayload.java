package com.github.scribejava.core.httpclient.multipart;

import com.github.scribejava.core.httpclient.HttpClient;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FileByteArrayBodyPartPayload extends ByteArrayBodyPartPayload {

    public FileByteArrayBodyPartPayload(byte[] payload) {
        this(payload, null);
    }

    public FileByteArrayBodyPartPayload(byte[] payload, String name) {
        this(payload, name, null);
    }

    public FileByteArrayBodyPartPayload(byte[] payload, String name, String filename) {
        this(null, payload, name, filename);
    }

    public FileByteArrayBodyPartPayload(String contentType, byte[] payload) {
        this(contentType, payload, null);
    }

    public FileByteArrayBodyPartPayload(String contentType, byte[] payload, String name) {
        this(contentType, payload, name, null);
    }

    public FileByteArrayBodyPartPayload(String contentType, byte[] payload, String name, String filename) {
        super(payload, composeHeaders(contentType, name, filename));
    }

    private static Map<String, String> composeHeaders(String contentType, String name, String filename) {

        String contentDispositionHeader = "form-data";
        if (name != null) {
            contentDispositionHeader += "; name=\"" + name + '"';
        }
        if (filename != null) {
            contentDispositionHeader += "; filename=\"" + filename + '"';
        }
        if (contentType == null) {
            return Collections.singletonMap("Content-Disposition", contentDispositionHeader);
        } else {
            final Map<String, String> headers = new HashMap<>();
            headers.put(HttpClient.CONTENT_TYPE, contentType);
            headers.put("Content-Disposition", contentDispositionHeader);
            return headers;
        }
    }
}
