package com.github.scribejava.core.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.utils.StreamUtils;

public class Response {

    private int code;
    private String message;
    private String body;
    private InputStream stream;
    private Map<String, String> headers;

    public Response(final int code, final String message, final Map<String, String> headers, final String body, final InputStream stream) {
        this.code = code;
        this.headers = headers;
        this.body = body;
        this.message = message;
        this.stream = stream;
    }

    Response(final HttpURLConnection connection) throws IOException {
        try {
            connection.connect();
            code = connection.getResponseCode();
            message = connection.getResponseMessage();
            headers = parseHeaders(connection);
            stream = isSuccessful() ? connection.getInputStream() : connection.getErrorStream();
        } catch (UnknownHostException e) {
            throw new OAuthException("The IP address of a host could not be determined.", e);
        }
    }

    private String parseBodyContents() {
        body = StreamUtils.getStreamContents(getStream());
        return body;
    }

    private Map<String, String> parseHeaders(final HttpURLConnection conn) {
        final Map<String, String> headers = new HashMap<>();
        for (final String key : conn.getHeaderFields().keySet()) {
            headers.put(key, conn.getHeaderFields().get(key).get(0));
        }
        return headers;
    }

    public final boolean isSuccessful() {
        return getCode() >= 200 && getCode() < 400;
    }

    /**
     * Obtains the HTTP Response body
     *
     * @return response body
     */
    public String getBody() {
        return body == null ? parseBodyContents() : body;
    }

    /**
     * Obtains the meaningful stream of the HttpUrlConnection, either inputStream or errorInputStream, depending on the status code
     *
     * @return input stream / error stream
     */
    private InputStream getStream() {
        return stream;
    }

    /**
     * Obtains the HTTP status code
     *
     * @return the status code
     */
    public final int getCode() {
        return code;
    }

    /**
     * Obtains the HTTP status message. Returns <code>null</code> if the message can not be discerned from the response (not valid HTTP)
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
    public String getHeader(final String name) {
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
