package com.github.scribejava.apis.wunderlist;

import java.io.OutputStream;

import com.github.scribejava.apis.WunderlistAPI;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.oauth.OAuth20Service;

public class WunderlistOAuthService extends OAuth20Service {

    /**
     * @param api api
     * @param apiKey apiKey
     * @param apiSecret apiSecret
     * @param callback callback
     * @param defaultScope defaultScope
     * @param responseType responseType
     * @param userAgent userAgent
     * @param httpClientConfig httpClientConfig
     * @param httpClient httpClient
     * @deprecated use {@link #WunderlistOAuthService(com.github.scribejava.apis.WunderlistAPI, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.io.OutputStream, java.lang.String,
     * com.github.scribejava.core.httpclient.HttpClientConfig, com.github.scribejava.core.httpclient.HttpClient) }
     */
    @Deprecated
    public WunderlistOAuthService(WunderlistAPI api, String apiKey, String apiSecret, String callback,
            String defaultScope, String responseType, String userAgent, HttpClientConfig httpClientConfig,
            HttpClient httpClient) {
        this(api, apiKey, apiSecret, callback, defaultScope, responseType, null, userAgent, httpClientConfig,
                httpClient);
    }

    public WunderlistOAuthService(WunderlistAPI api, String apiKey, String apiSecret, String callback,
            String defaultScope, String responseType, OutputStream debugStream, String userAgent,
            HttpClientConfig httpClientConfig, HttpClient httpClient) {
        super(api, apiKey, apiSecret, callback, defaultScope, responseType, debugStream, userAgent, httpClientConfig,
                httpClient);
    }

    @Override
    public void signRequest(String accessToken, OAuthRequest request) {
        super.signRequest(accessToken, request);
        request.addHeader("X-Client-ID", getApiKey());
    }
}
