package com.github.scribejava.apis;

import com.github.scribejava.apis.polar.PolarJsonTokenExtractor;
import com.github.scribejava.apis.polar.PolarOAuthService;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuth2AccessToken;

import java.io.OutputStream;

/**
 * Polar's OAuth2 client's implementation source: https://www.polar.com/accesslink-api/#authentication
 */
public class PolarAPI extends DefaultApi20 {

    protected PolarAPI() {
    }

    private static class InstanceHolder {

        private static final PolarAPI INSTANCE = new PolarAPI();
    }

    public static PolarAPI instance() {
        return PolarAPI.InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://polarremote.com/v2/oauth2/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://flow.polar.com/oauth2/authorization";
    }

    @Override
    public PolarOAuthService createService(String apiKey, String apiSecret, String callback, String defaultScope,
            String responseType, OutputStream debugStream, String userAgent, HttpClientConfig httpClientConfig,
            HttpClient httpClient) {

        return new PolarOAuthService(this, apiKey, apiSecret, callback, defaultScope, responseType, debugStream,
                userAgent, httpClientConfig, httpClient);
    }

    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return PolarJsonTokenExtractor.instance();
    }
}
