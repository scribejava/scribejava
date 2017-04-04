package com.github.scribejava.core.model;

import com.github.scribejava.core.builder.api.OAuth1SignatureType;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Parameter object that groups OAuth config values
 */
public class OAuthConfig {

    private final String apiKey;
    private final String apiSecret;
    private final String callback;
    /**
     * @deprecated override or change in Pull Request
     * {@link com.github.scribejava.core.builder.api.DefaultApi10a#getSignatureType()}
     */
    @Deprecated
    private OAuth1SignatureType signatureType;
    private final String scope;
    private final OutputStream debugStream;
    private final String state;
    private final String responseType;
    private final String userAgent;

    private HttpClientConfig httpClientConfig;
    private HttpClient httpClient;

    public OAuthConfig(String key, String secret) {
        this(key, secret, null, null, null, null, null, null, null, null);
    }

    /**
     * @param apiKey apiKey
     * @param apiSecret apiSecret
     * @param callback callback
     * @param signatureType signatureType
     * @param scope scope
     * @param debugStream debugStream
     * @param state state
     * @param responseType responseType
     * @param userAgent userAgent
     * @param httpClientConfig httpClientConfig
     * @param httpClient httpClient
     *
     * @deprecated use {@link #OAuthConfig(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.io.OutputStream, java.lang.String, java.lang.String, java.lang.String,
     * com.github.scribejava.core.httpclient.HttpClientConfig, com.github.scribejava.core.httpclient.HttpClient)}
     * <br>
     * without OAuth1SignatureType param. to change OAuth1SignatureType override or change in Pull Request
     * <br>{@link com.github.scribejava.core.builder.api.DefaultApi10a#getSignatureType()}
     */
    @Deprecated
    public OAuthConfig(String apiKey, String apiSecret, String callback, OAuth1SignatureType signatureType,
            String scope, OutputStream debugStream, String state, String responseType, String userAgent,
            HttpClientConfig httpClientConfig, HttpClient httpClient) {
        this(apiKey, apiSecret, callback, scope, debugStream, state, responseType, userAgent, httpClientConfig,
                httpClient);
        this.signatureType = signatureType;
    }

    public OAuthConfig(String apiKey, String apiSecret, String callback, String scope, OutputStream debugStream,
            String state, String responseType, String userAgent, HttpClientConfig httpClientConfig,
            HttpClient httpClient) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.callback = callback;
        this.scope = scope;
        this.debugStream = debugStream;
        this.state = state;
        this.responseType = responseType;
        this.userAgent = userAgent;
        this.httpClientConfig = httpClientConfig;
        this.httpClient = httpClient;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public String getCallback() {
        return callback;
    }

    /**
     * @return configured OAuth1SignatureType to override from API
     *
     * @deprecated override or change in Pull Request
     * {@link com.github.scribejava.core.builder.api.DefaultApi10a#getSignatureType()}
     */
    @Deprecated
    public OAuth1SignatureType getSignatureType() {
        return signatureType;
    }

    public String getScope() {
        return scope;
    }

    public String getState() {
        return state;
    }

    public String getResponseType() {
        return responseType;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void log(String message) {
        if (debugStream != null) {
            message += '\n';
            try {
                debugStream.write(message.getBytes("UTF8"));
            } catch (IOException | RuntimeException e) {
                throw new RuntimeException("there were problems while writting to the debug stream", e);
            }
        }
    }

    public HttpClientConfig getHttpClientConfig() {
        return httpClientConfig;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }
}
