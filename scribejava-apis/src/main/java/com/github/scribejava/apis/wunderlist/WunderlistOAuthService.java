package com.github.scribejava.apis.wunderlist;

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
     * @param scope scope
     * @param state state
     * @param responseType responseType
     * @param userAgent userAgent
     * @param httpClientConfig httpClientConfig
     * @param httpClient httpClient
     * @deprecated use one of getAuthorizationUrl method in {@link com.github.scribejava.core.oauth.OAuth20Service}
     */
    @Deprecated
    public WunderlistOAuthService(WunderlistAPI api, String apiKey, String apiSecret, String callback, String scope,
            String state, String responseType, String userAgent, HttpClientConfig httpClientConfig,
            HttpClient httpClient) {
        super(api, apiKey, apiSecret, callback, scope, state, responseType, userAgent, httpClientConfig, httpClient);
    }

    public WunderlistOAuthService(WunderlistAPI api, String apiKey, String apiSecret, String callback, String scope,
            String responseType, String userAgent, HttpClientConfig httpClientConfig, HttpClient httpClient) {
        super(api, apiKey, apiSecret, callback, scope, responseType, userAgent, httpClientConfig, httpClient);
    }

    @Override
    public void signRequest(String accessToken, OAuthRequest request) {
        super.signRequest(accessToken, request);
        request.addHeader("X-Client-ID", getApiKey());
    }
}
