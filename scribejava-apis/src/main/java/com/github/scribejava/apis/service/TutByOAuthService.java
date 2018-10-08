package com.github.scribejava.apis.service;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.oauth.OAuth20Service;

/**
 * @deprecated doesn't do anything useful, all TutBy specific logic was moved to
 * {@link com.github.scribejava.apis.tutby.TutByBearerSignature}
 */
@Deprecated
public class TutByOAuthService extends OAuth20Service {

    public TutByOAuthService(DefaultApi20 api, String apiKey, String apiSecret, String callback, String scope,
            String state, String responseType, String userAgent, HttpClientConfig httpClientConfig,
            HttpClient httpClient) {
        super(api, apiKey, apiSecret, callback, scope, state, responseType, userAgent, httpClientConfig, httpClient);
    }

    /**
     * @deprecated moved to {@link com.github.scribejava.apis.tutby.TutByBearerSignature}
     */
    @Deprecated
    @Override
    public void signRequest(String accessToken, OAuthRequest request) {
        super.signRequest(accessToken, request);
    }
}
