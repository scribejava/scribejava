package com.github.scribejava.apis;

import com.github.scribejava.apis.imgur.ImgurOAuthService;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.ParameterList;
import java.io.OutputStream;
import java.util.Map;

public class ImgurApi extends DefaultApi20 {

    protected ImgurApi() {
    }

    private static class InstanceHolder {
        private static final ImgurApi INSTANCE = new ImgurApi();
    }

    public static ImgurApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.imgur.com/oauth2/token";
    }

    @Override
    public String getAuthorizationUrl(String responseType, String apiKey, String callback, String scope, String state,
            Map<String, String> additionalParams) {
        final ParameterList parameters = new ParameterList(additionalParams);
        parameters.add(OAuthConstants.RESPONSE_TYPE, isOob(callback) ? "pin" : "code");
        parameters.add(OAuthConstants.CLIENT_ID, apiKey);

        if (callback != null) {
            parameters.add(OAuthConstants.REDIRECT_URI, callback);
        }

        if (scope != null) {
            parameters.add(OAuthConstants.SCOPE, scope);
        }

        if (state != null) {
            parameters.add(OAuthConstants.STATE, state);
        }

        return parameters.appendTo("https://api.imgur.com/oauth2/authorize");
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        throw new UnsupportedOperationException("use getAuthorizationUrl instead");
    }

    @Override
    public ImgurOAuthService createService(String apiKey, String apiSecret, String callback, String scope,
            OutputStream debugStream, String state, String responseType, String userAgent,
            HttpClientConfig httpClientConfig, HttpClient httpClient) {
        return new ImgurOAuthService(this, apiKey, apiSecret, callback, scope, state, responseType, userAgent,
                httpClientConfig, httpClient);
    }

    public static boolean isOob(String callback) {
        return OAuthConstants.OOB.equals(callback);
    }
}
