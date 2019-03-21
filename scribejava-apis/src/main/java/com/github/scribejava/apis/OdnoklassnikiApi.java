package com.github.scribejava.apis;

import com.github.scribejava.apis.odnoklassniki.OdnoklassnikiOAuthService;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignature;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignatureURIQueryParameter;
import com.github.scribejava.core.oauth2.clientauthentication.ClientAuthentication;
import com.github.scribejava.core.oauth2.clientauthentication.RequestBodyAuthenticationScheme;
import java.io.OutputStream;

public class OdnoklassnikiApi extends DefaultApi20 {

    protected OdnoklassnikiApi() {
    }

    private static class InstanceHolder {
        private static final OdnoklassnikiApi INSTANCE = new OdnoklassnikiApi();
    }

    public static OdnoklassnikiApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.ok.ru/oauth/token.do";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://connect.ok.ru/oauth/authorize";
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
    public OdnoklassnikiOAuthService createService(String apiKey, String apiSecret, String callback,
            String defaultScope, OutputStream debugStream, String responseType, String userAgent,
            HttpClientConfig httpClientConfig, HttpClient httpClient) {
        return createService(apiKey, apiSecret, callback, defaultScope, responseType, userAgent, httpClientConfig,
                httpClient);
    }

    @Override
    public OdnoklassnikiOAuthService createService(String apiKey, String apiSecret, String callback,
            String defaultScope, String responseType, String userAgent, HttpClientConfig httpClientConfig,
            HttpClient httpClient) {
        return new OdnoklassnikiOAuthService(this, apiKey, apiSecret, callback, defaultScope, responseType, userAgent,
                httpClientConfig, httpClient);
    }

    @Override
    public BearerSignature getBearerSignature() {
        return BearerSignatureURIQueryParameter.instance();
    }

    @Override
    public ClientAuthentication getClientAuthentication() {
        return RequestBodyAuthenticationScheme.instance();
    }
}
