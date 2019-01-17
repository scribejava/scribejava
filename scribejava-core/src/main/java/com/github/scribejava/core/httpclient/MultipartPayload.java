package com.github.scribejava.core.httpclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The class containing more than one payload of multipart/form-data request
 *
 * @deprecated use {@link com.github.scribejava.core.httpclient.multipart.MultipartPayload}
 */
@Deprecated
public class MultipartPayload {

    private static final String DEFAULT_SUBTYPE = "form-data";

    private final String boundary;
    private final Map<String, String> headers;
    private final List<BodyPartPayload> bodyParts = new ArrayList<>();

    public MultipartPayload(String boundary) {
        this.boundary = boundary;
        headers = Collections.singletonMap(HttpClient.CONTENT_TYPE,
                "multipart/" + DEFAULT_SUBTYPE + "; boundary=\"" + boundary + '"');
    }

    /**
     * @param bodyPart bodyPart
     * @return return
     * @deprecated no replace for that. implement yourself. See code here
     * {@link com.github.scribejava.core.httpclient.jdk.JDKHttpClient
     * #getPayload(com.github.scribejava.core.httpclient.MultipartPayload)}
     */
    @Deprecated
    public byte[] getStartBoundary(BodyPartPayload bodyPart) {
        final StringBuilder startBoundary = new StringBuilder();
        startBoundary.append("\r\n--")
                .append(boundary)
                .append("\r\n");

        for (Map.Entry<String, String> header : bodyPart.getHeaders().entrySet()) {
            startBoundary.append(header.getKey())
                    .append(": ")
                    .append(header.getValue())
                    .append("\r\n");
        }
        return startBoundary.append("\r\n").toString().getBytes();
    }

    /**
     * @return return
     * @deprecated no replace for that. implement yourself. See code here
     * {@link com.github.scribejava.core.httpclient.jdk.JDKHttpClient
     * #getPayload(com.github.scribejava.core.httpclient.MultipartPayload)}
     */
    @Deprecated
    public byte[] getEndBoundary() {
        return ("\r\n--" + boundary + "--\r\n").getBytes();
    }

    /**
     * @return return
     * @deprecated no replace for that. implement yourself. See code here
     * {@link com.github.scribejava.core.httpclient.jdk.JDKHttpClient
     * #getPayload(com.github.scribejava.core.httpclient.MultipartPayload)}
     */
    @Deprecated
    public int getContentLength() {
        int contentLength = 0;

        for (BodyPartPayload bodyPart : bodyParts) {
            contentLength += getStartBoundary(bodyPart).length + bodyPart.getPayload().length;
        }
        if (!bodyParts.isEmpty()) {
            contentLength += getEndBoundary().length;
        }
        return contentLength;
    }

    public List<BodyPartPayload> getBodyParts() {
        return bodyParts;
    }

    public void addMultipartPayload(String contentDisposition, String contentType, byte[] payload) {
        bodyParts.add(new BodyPartPayload(contentDisposition, contentType, payload));
    }

    /**
     * @return return
     * @deprecated use {@link #getHeaders() } and then get("Content-Type")
     */
    @Deprecated
    public String getContentType() {
        return headers.get(HttpClient.CONTENT_TYPE);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBoundary() {
        return boundary;
    }
}
