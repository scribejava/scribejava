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

    public OAuthRequest(Verb verb, String url, OAuthService service) {
        super(verb, url, service);
    }

    /**
     * Execute the request and return a {@link Response}
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
            getService().getConfig().log("Cannot use sync operations, only async");
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
        final OAuthConfig config = getService().getConfig();
        if (config.getConnectTimeout() != null) {
            connection.setConnectTimeout(config.getConnectTimeout().intValue());
        }
        if (config.getReadTimeout() != null) {
            connection.setReadTimeout(config.getReadTimeout().intValue());
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
            System.setProperty("http.keepAlive", isConnectionKeepAlive() ? "true" : "false");
            connection = (HttpURLConnection) new URL(completeUrl).openConnection();
            connection.setInstanceFollowRedirects(isFollowRedirects());
        }
    }

    void addHeaders() {
        for (final Map.Entry<String, String> entry : getHeaders().entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
    }

    void addBody(final byte[] content) throws IOException {
        connection.setRequestProperty(CONTENT_LENGTH, String.valueOf(content.length));

        if (connection.getRequestProperty(CONTENT_TYPE) == null) {
            connection.setRequestProperty(CONTENT_TYPE, DEFAULT_CONTENT_TYPE);
        }
        connection.setDoOutput(true);
        connection.getOutputStream().write(content);
    }

    void setConnection(final HttpURLConnection connection) {
        this.connection = connection;
    }
}
