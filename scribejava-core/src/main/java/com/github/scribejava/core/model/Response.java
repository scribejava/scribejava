package com.github.scribejava.core.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import com.github.scribejava.core.utils.StreamUtils;

public class Response {

    private final int code;
    private final String message;
    private final Map<String, String> headers;
    private String body;
    private InputStream stream;

    private Response(int code, String message, Map<String, String> headers) {
        this.code = code;
        this.message = message;
        this.headers = headers;
    }

    public Response(int code, String message, Map<String, String> headers, InputStream stream) {
        this(code, message, headers);
        this.stream = stream;
    }

    public Response(int code, String message, Map<String, String> headers, String body) {
        this(code, message, headers);
        this.body = body;
    }

    private String parseBodyContents() throws IOException {
        if (stream == null) {
            return null;
        }
        if ("gzip".equals(getHeader("Content-Encoding"))) {
            body = StreamUtils.getGzipStreamContents(stream);
        } else {
            body = StreamUtils.getStreamContents(stream);
        }
        return body;
    }

    public boolean isSuccessful() {
        return code >= 200 && code < 400;
    }

    public String getBody() throws IOException {
        return body == null ? parseBodyContents() : body;
    }

    /**
     * Obtains the meaningful stream of the HttpUrlConnection, either inputStream or errorInputStream, depending on the
     * status code
     *
     * @return input stream / error stream
     */
    public InputStream getStream() {
        return stream;
    }

    /**
     * Obtains the HTTP status code
     *
     * @return the status code
     */
    public int getCode() {
        return code;
    }

    /**
     * Obtains the HTTP status message. Returns <code>null</code> if the message can not be discerned from the response
     * (not valid HTTP)
     *
     * @return the status message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Obtains a {@link Map} containing the HTTP Response Headers
     *
     * @return headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Obtains a single HTTP Header value, or null if undefined
     *
     * @param name the header name.
     *
     * @return header value or null.
     */
    public String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public String toString() {
        return "Response{" +
            "code=" + code +
            ", message='" + message + '\'' +
            ", body='" + body + '\'' +
            ", headers=" + headers +
            '}';
    }
}
