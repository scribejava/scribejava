package com.github.scribejava.core.model;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;
import com.github.scribejava.core.exceptions.OAuthConnectionException;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.oauth.OAuthService;
import java.io.File;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;

public class OAuthRequest extends AbstractRequest {

    private final OAuthConfig config;

    public OAuthRequest(Verb verb, String url, OAuthConfig config) {
        super(verb, url);
        this.config = config;
    }

    /**
     * Execute the request and return a {@link Response}
     *
     * the same as {@link OAuthService#execute(com.github.scribejava.core.model.OAuthRequest)}
     *
     * @return Http Response
     *
     * @throws RuntimeException if the connection cannot be created.
     */
    public Response send() {
        try {
            return doSend(config, isFollowRedirects(), getHeaders(), getVerb(), getCompleteUrl(), this);
        } catch (IOException | RuntimeException e) {
            throw new OAuthConnectionException(getCompleteUrl(), e);
        }
    }

    private static Response doSend(OAuthConfig config, boolean followRedirects, Map<String, String> headers,
            Verb httpVerb, String completeUrl, OAuthRequest request) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) new URL(completeUrl).openConnection();
        connection.setInstanceFollowRedirects(followRedirects);
        connection.setRequestMethod(httpVerb.name());
        if (config.getConnectTimeout() != null) {
            connection.setConnectTimeout(config.getConnectTimeout());
        }
        if (config.getReadTimeout() != null) {
            connection.setReadTimeout(config.getReadTimeout());
        }
        addHeaders(connection, headers, config.getUserAgent());
        if (httpVerb == Verb.POST || httpVerb == Verb.PUT || httpVerb == Verb.DELETE) {
            final File filePayload = request.getFilePayload();
            if (filePayload != null) {
                throw new UnsupportedOperationException("Sync Requests do not support File payload for the moment");
            } else if (request.getStringPayload() != null) {
                addBody(connection, request.getStringPayload().getBytes(request.getCharset()));
            } else {
                addBody(connection, request.getByteArrayPayload());
            }
        }

        try {
            connection.connect();
            final int responseCode = connection.getResponseCode();
            return new Response(responseCode, connection.getResponseMessage(), parseHeaders(connection),
                    responseCode >= 200 && responseCode < 400 ? connection.getInputStream()
                            : connection.getErrorStream());
        } catch (UnknownHostException e) {
            throw new OAuthException("The IP address of a host could not be determined.", e);
        }
    }

    private static Map<String, String> parseHeaders(HttpURLConnection conn) {
        final Map<String, String> headers = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : conn.getHeaderFields().entrySet()) {
            final String key = entry.getKey();
            if ("Content-Encoding".equalsIgnoreCase(key)) {
                headers.put("Content-Encoding", entry.getValue().get(0));
            } else {
                headers.put(key, entry.getValue().get(0));
            }
        }
        return headers;
    }

    private static void addHeaders(HttpURLConnection connection, Map<String, String> headers, String userAgent) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
        if (userAgent != null) {
            connection.setRequestProperty(OAuthConstants.USER_AGENT_HEADER_NAME, userAgent);
        }
    }

    private static void addBody(HttpURLConnection connection, byte[] content) throws IOException {
        connection.setRequestProperty(CONTENT_LENGTH, String.valueOf(content.length));

        if (connection.getRequestProperty(CONTENT_TYPE) == null) {
            connection.setRequestProperty(CONTENT_TYPE, DEFAULT_CONTENT_TYPE);
        }
        connection.setDoOutput(true);
        connection.getOutputStream().write(content);
    }
}
