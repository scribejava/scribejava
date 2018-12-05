package com.github.scribejava.core.httpclient;

import java.util.ArrayList;
import java.util.List;

/**
 * The class containing more than one payload of multipart/form-data request
 */
public class MultipartPayload {

    private final String subtype;
    private final String boundary;
    private final List<BodyPartPayload> bodyParts = new ArrayList<>();

    public MultipartPayload(String subtype, String boundary) {
        this.subtype = subtype;
        this.boundary = boundary;
    }

    public byte[] getStartBoundary(BodyPartPayload bodyPart) {
        return ("\r\n--" + boundary + "\r\n"
                + "Content-Disposition: " + bodyPart.getContentDisposition() + "\r\n"
                + (bodyPart.getContentType() == null
                ? "" : HttpClient.CONTENT_TYPE + ": " + bodyPart.getContentType() + "\r\n")
                + "\r\n").getBytes();
    }

    public byte[] getEndBoundary() {
        return ("\r\n--" + boundary + "--\r\n").getBytes();
    }

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

    public String getSubtype() {
        return subtype;
    }

    public String getBoundary() {
        return boundary;
    }

    public List<BodyPartPayload> getBodyParts() {
        return bodyParts;
    }

    public void addMultipartPayload(String contentDisposition, String contentType, byte[] payload) {
        bodyParts.add(new BodyPartPayload(contentDisposition, contentType, payload));
    }
}
