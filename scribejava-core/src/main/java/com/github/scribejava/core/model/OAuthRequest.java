package com.github.scribejava.core.model;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import com.github.scribejava.core.exceptions.OAuthConnectionException;
import com.github.scribejava.core.oauth.OAuthService;
import java.io.File;

public class OAuthRequest extends AbstractRequest {

    private HttpURLConnection connection;

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
            createConnection();
            return doSend();
        } catch (IOException | RuntimeException e) {
            throw new OAuthConnectionException(getCompleteUrl(), e);
        }
    }

    Response doSend() throws IOException {
        final Verb verb = getVerb();
        connection.setRequestMethod(verb.name());
        if (config.getConnectTimeout() != null) {
            connection.setConnectTimeout(config.getConnectTimeout());
        }
        if (config.getReadTimeout() != null) {
            connection.setReadTimeout(config.getReadTimeout());
        }
        addHeaders();
        if (hasBodyContent()) {
            final File filePayload = getFilePayload();
            if (filePayload != null) {
                throw new UnsupportedOperationException("Sync Requests do not support File payload for the moment");
            } else if (getStringPayload() != null) {
                addBody(getStringPayload().getBytes(getCharset()));
            } else {
                addBody(getByteArrayPayload());
            }
        }
        return new Response(connection);
    }

    private void createConnection() throws IOException {
        final String completeUrl = getCompleteUrl();
        if (connection == null) {
            connection = (HttpURLConnection) new URL(completeUrl).openConnection();
            connection.setInstanceFollowRedirects(isFollowRedirects());
        }
    }

    void addHeaders() {
        for (Map.Entry<String, String> entry : getHeaders().entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
        final String userAgent = config.getUserAgent();
        if (userAgent != null) {
            connection.setRequestProperty(OAuthConstants.USER_AGENT_HEADER_NAME, userAgent);
        }
    }

    void addBody(byte[] content) throws IOException {
        connection.setRequestProperty(CONTENT_LENGTH, String.valueOf(content.length));

        if (connection.getRequestProperty(CONTENT_TYPE) == null) {
            connection.setRequestProperty(CONTENT_TYPE, DEFAULT_CONTENT_TYPE);
        }
        connection.setDoOutput(true);
        connection.getOutputStream().write(content);
    }

    void setConnection(HttpURLConnection connection) {
        this.connection = connection;
    }
}
