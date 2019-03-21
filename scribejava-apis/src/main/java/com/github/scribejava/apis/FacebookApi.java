package com.github.scribejava.apis;

import com.github.scribejava.apis.facebook.FacebookAccessTokenJsonExtractor;
import com.github.scribejava.apis.facebook.FacebookService;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth2.clientauthentication.ClientAuthentication;
import com.github.scribejava.core.oauth2.clientauthentication.RequestBodyAuthenticationScheme;
import java.io.OutputStream;

/**
 * Facebook API
 */
public class FacebookApi extends DefaultApi20 {

    private final String version;

    protected FacebookApi() {
        this("2.11");
    }

    protected FacebookApi(String version) {
        this.version = version;
    }

    private static class InstanceHolder {

        private static final FacebookApi INSTANCE = new FacebookApi();
    }

    public static FacebookApi instance() {
        return InstanceHolder.INSTANCE;
    }

    public static FacebookApi customVersion(String version) {
        return new FacebookApi(version);
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://graph.facebook.com/v" + version + "/oauth/access_token";
    }

    @Override
    public String getRefreshTokenEndpoint() {
        throw new UnsupportedOperationException("Facebook doesn't support refreshing tokens");
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://www.facebook.com/v" + version + "/dialog/oauth";
    }

    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return FacebookAccessTokenJsonExtractor.instance();
    }

    @Override
    public ClientAuthentication getClientAuthentication() {
        return RequestBodyAuthenticationScheme.instance();
    }

    /**
     *
     * @param apiKey apiKey
     * @param apiSecret apiSecret
     * @param callback callback
     * @param defaultScope defaultScope
     * @param debugStream debugStream
     * @param responseType responseType
     * @param userAgent userAgent
     * @param httpClientConfig httpClientConfig
     * @param httpClient httpClient
     * @return service
     * @deprecated use {@link #createService(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, com.github.scribejava.core.httpclient.HttpClientConfig,
     * com.github.scribejava.core.httpclient.HttpClient) }
     */
    @Deprecated
    @Override
    public FacebookService createService(String apiKey, String apiSecret, String callback, String defaultScope,
            OutputStream debugStream, String responseType, String userAgent, HttpClientConfig httpClientConfig,
            HttpClient httpClient) {
        return createService(apiKey, apiSecret, callback, defaultScope, responseType, userAgent, httpClientConfig,
                httpClient);
    }

    @Override
    public FacebookService createService(String apiKey, String apiSecret, String callback, String defaultScope,
            String responseType, String userAgent, HttpClientConfig httpClientConfig, HttpClient httpClient) {
        return new FacebookService(this, apiKey, apiSecret, callback, defaultScope, responseType, userAgent,
                httpClientConfig, httpClient);
    }
}
