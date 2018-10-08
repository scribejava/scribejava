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

    @Override
    public OAuth20Service createService(String apiKey, String apiSecret, String callback, String scope,
            OutputStream debugStream, String state, String responseType, String userAgent,
            HttpClientConfig httpClientConfig, HttpClient httpClient) {
        return new WunderlistOAuthService(this, apiKey, apiSecret, callback, scope, state, responseType, userAgent,
                httpClientConfig, httpClient);
    }
}
