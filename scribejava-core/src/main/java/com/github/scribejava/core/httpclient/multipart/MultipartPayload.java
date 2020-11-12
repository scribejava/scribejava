package com.github.scribejava.core.httpclient.multipart;

import com.github.scribejava.core.httpclient.HttpClient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MultipartPayload extends BodyPartPayload {

    private static final String DEFAULT_SUBTYPE = "form-data";

    private final String boundary;
    private String preamble;
    private final List<BodyPartPayload> bodyParts = new ArrayList<>();
    private String epilogue;

    public MultipartPayload() {
        this(null, MultipartUtils.generateDefaultBoundary(), null);
    }

    public MultipartPayload(String boundary) {
        this(null, boundary, null);
    }

    public MultipartPayload(String subtype, String boundary) {
        this(subtype, boundary, null);
    }

    public MultipartPayload(Map<String, String> headers) {
        this(null, parseOrGenerateBoundary(headers), headers);
    }

    public MultipartPayload(String boundary, Map<String, String> headers) {
        this(null, boundary, headers);
    }

    public MultipartPayload(String subtype, String boundary, Map<String, String> headers) {
        super(composeHeaders(subtype, boundary, headers));
        this.boundary = boundary;
    }

    private static Map<String, String> composeHeaders(String subtype, String boundary, Map<String, String> headersIn)
            throws IllegalArgumentException {
        MultipartUtils.checkBoundarySyntax(boundary);
        final Map<String, String> headersOut;
        String contentTypeHeader = headersIn == null ? null : headersIn.get(HttpClient.CONTENT_TYPE);
        if (contentTypeHeader == null) {
            contentTypeHeader = "multipart/" + (subtype == null ? DEFAULT_SUBTYPE : subtype)
                    + "; boundary=\"" + boundary + '"';
            if (headersIn == null) {
                headersOut = Collections.singletonMap(HttpClient.CONTENT_TYPE, contentTypeHeader);
            } else {
                headersOut = headersIn;
                headersOut.put(HttpClient.CONTENT_TYPE, contentTypeHeader);
            }
        } else {
            headersOut = headersIn;
            final String parsedBoundary = MultipartUtils.parseBoundaryFromHeader(contentTypeHeader);
            if (parsedBoundary == null) {
                headersOut.put(HttpClient.CONTENT_TYPE, contentTypeHeader + "; boundary=\"" + boundary + '"');
            } else if (!parsedBoundary.equals(boundary)) {
                throw new IllegalArgumentException(
                        "Different boundaries was passed in constructors. One as argument, second as header");
            }
        }
        return headersOut;
    }

    private static String parseOrGenerateBoundary(Map<String, String> headers) {
        final String parsedBoundary = MultipartUtils.parseBoundaryFromHeader(headers.get(HttpClient.CONTENT_TYPE));
        return parsedBoundary == null ? MultipartUtils.generateDefaultBoundary() : parsedBoundary;
    }

    public void addBodyPart(BodyPartPayload bodyPartPayload) {
        bodyParts.add(bodyPartPayload);
    }

    public void addBodyPart(MultipartPayload multipartPayload) {
        if (multipartPayload.getBoundary().equals(boundary)) {
            throw new IllegalArgumentException("{'boundary'}={'" + boundary
                    + "'} is the same for parent MultipartPayload and child");
        }
        bodyParts.add(multipartPayload);
    }

    public List<BodyPartPayload> getBodyParts() {
        return bodyParts;
    }

    public String getBoundary() {
        return boundary;
    }

    public String getPreamble() {
        return preamble;
    }

    public void setPreamble(String preamble) {
        this.preamble = preamble;
    }

    public String getEpilogue() {
        return epilogue;
    }

    public void setEpilogue(String epilogue) {
        this.epilogue = epilogue;
    }
}
