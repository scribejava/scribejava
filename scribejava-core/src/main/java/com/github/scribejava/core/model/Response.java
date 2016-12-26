package com.github.scribejava.core.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.utils.StreamUtils;

public class Response {

    private final int code;
    private final String message;
    private final Map<String, String> headers;
    private String body;
    private InputStream stream;

    /**
     *
     * @param code code
     * @param message message
     * @param headers headers
     * @param body body
     * @param stream stream
     * @deprecated use either {@link #Response(int, java.lang.String, java.util.Map, java.io.InputStream) }
     * or {@link #Response(int, java.lang.String, java.util.Map, java.lang.String) }
     */
    @Deprecated
    public Response(int code, String message, Map<String, String> headers, String body, InputStream stream) {
        this.code = code;
        this.message = message;
        this.headers = headers;
        this.body = body;
        this.stream = stream;
    }

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

    /**
     *
     * @param connection connection
     * @throws IOException
     * @deprecated use {@link #Response(int, java.lang.String, java.util.Map, java.lang.String, java.io.InputStream) }
     */
    @Deprecated
    Response(HttpURLConnection connection) throws IOException {
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

    /**
     *
     * @param conn conn
     * @return
     * @deprecated use {@link OAuthRequest#parseHeaders(java.net.HttpURLConnection) }
     */
    @Deprecated
    private Map<String, String> parseHeaders(HttpURLConnection conn) {
        final Map<String, String> headers = new HashMap<>();
        for (Entry<String, List<String>> entry : conn.getHeaderFields().entrySet()) {
            final String key = entry.getKey();
            if ("Content-Encoding".equalsIgnoreCase(key)) {
                headers.put("Content-Encoding", entry.getValue().get(0));
            } else {
                headers.put(key, entry.getValue().get(0));
            }
        }
        return headers;
    }

    public final boolean isSuccessful() {
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
    public final int getCode() {
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
