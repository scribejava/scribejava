package com.github.scribejava.core.model;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import com.github.scribejava.core.exceptions.OAuthConnectionException;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.oauth.OAuthService;

public class OAuthRequest extends AbstractRequest {

    private HttpURLConnection connection;

    private final OAuthConfig config;

    /**
     *
     * @param verb verb
     * @param url url
     * @param service service
     * @deprecated use {@link #OAuthRequest(com.github.scribejava.core.model.Verb, java.lang.String,
     * com.github.scribejava.core.model.OAuthConfig)}
     */
    @Deprecated
    public OAuthRequest(Verb verb, String url, OAuthService<?> service) {
        this(verb, url, service.getConfig());
    }

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
        final ForceTypeOfHttpRequest forceTypeOfHttpRequest = ScribeJavaConfig.getForceTypeOfHttpRequests();

        if (ForceTypeOfHttpRequest.FORCE_ASYNC_ONLY_HTTP_REQUESTS == forceTypeOfHttpRequest) {
            throw new OAuthException("Cannot use sync operations, only async");
        }
        if (ForceTypeOfHttpRequest.PREFER_ASYNC_ONLY_HTTP_REQUESTS == forceTypeOfHttpRequest) {
            config.log("Cannot use sync operations, only async");
        }
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
            addBody(getByteBodyContents());
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
