package com.github.scribejava.apis;

import com.github.scribejava.apis.wunderlist.WunderlistOAuthService;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignature;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignatureURIQueryParameter;
import com.github.scribejava.core.oauth2.clientauthentication.ClientAuthentication;
import com.github.scribejava.core.oauth2.clientauthentication.RequestBodyAuthenticationScheme;
import java.io.OutputStream;

/**
 * Wunderlist.com Api
 */
public class WunderlistAPI extends DefaultApi20 {

    protected WunderlistAPI() {
    }

    private static class InstanceHolder {

        private static final WunderlistAPI INSTANCE = new WunderlistAPI();
    }

    public static WunderlistAPI instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://www.wunderlist.com/oauth/access_token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://www.wunderlist.com/oauth/authorize";
    }

    @Override
    public BearerSignature getBearerSignature() {
        return BearerSignatureURIQueryParameter.instance();
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
    public OAuth20Service createService(String apiKey, String apiSecret, String callback, String defaultScope,
            OutputStream debugStream, String responseType, String userAgent, HttpClientConfig httpClientConfig,
            HttpClient httpClient) {
        return createService(apiKey, apiSecret, callback, defaultScope, responseType, userAgent, httpClientConfig,
                httpClient);
    }

    @Override
    public OAuth20Service createService(String apiKey, String apiSecret, String callback, String defaultScope,
            String responseType, String userAgent, HttpClientConfig httpClientConfig, HttpClient httpClient) {
        return new WunderlistOAuthService(this, apiKey, apiSecret, callback, defaultScope, responseType, userAgent,
                httpClientConfig, httpClient);
    }
}
