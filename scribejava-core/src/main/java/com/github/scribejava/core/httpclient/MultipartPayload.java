package com.github.scribejava.core.httpclient;

import java.util.ArrayList;
import java.util.List;

/**
 * The class containing more than one payload of multipart/form-data request
 */
public class MultipartPayload {

    private final String boundary;
    private final List<BodyPartPayload> bodyParts = new ArrayList<>();

    public MultipartPayload(String boundary) {
        this.boundary = boundary;
    }

    public byte[] getStartBoundary(BodyPartPayload bodyPart) {
        return ("--" + boundary + "\r\n"
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
            contentLength += bodyPart.getPayload().length
                    + bodyPart.getContentDisposition().length();
            if (bodyPart.getContentType() != null) {
                contentLength += 16 //length of constant portions of contentType header
                        + bodyPart.getContentType().length();
            }
        }

        contentLength += (37 //length of constant portions of contentDisposition header,
                //see getStartBoundary and getEndBoundary methods
                + boundary.length() * 2 //twice. start and end parts
                ) * bodyParts.size(); //for every part
        return contentLength;
    }

    public List<BodyPartPayload> getBodyParts() {
        return bodyParts;
    }

    public void addMultipartPayload(String contentDisposition, String contentType, byte[] payload) {
        bodyParts.add(new BodyPartPayload(contentDisposition, contentType, payload));
    }
}
