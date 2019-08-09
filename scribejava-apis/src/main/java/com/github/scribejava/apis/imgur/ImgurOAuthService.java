package com.github.scribejava.apis.imgur;

import com.github.scribejava.apis.ImgurApi;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.oauth.AccessTokenRequestParams;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.pkce.PKCE;

public class ImgurOAuthService extends OAuth20Service {

    public ImgurOAuthService(DefaultApi20 api, String apiKey, String apiSecret, String callback, String defaultScope,
            String responseType, String userAgent, HttpClientConfig httpClientConfig, HttpClient httpClient) {
        super(api, apiKey, apiSecret, callback, defaultScope, responseType, userAgent, httpClientConfig, httpClient);
    }

    @Override
    protected OAuthRequest createAccessTokenRequest(AccessTokenRequestParams params) {
        final DefaultApi20 api = getApi();
        final OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        request.addBodyParameter(OAuthConstants.CLIENT_ID, getApiKey());
        request.addBodyParameter(OAuthConstants.CLIENT_SECRET, getApiSecret());

        final String oauthVerifier = params.getCode();
        if (ImgurApi.isOob(getCallback())) {
            request.addBodyParameter(OAuthConstants.GRANT_TYPE, "pin");
            request.addBodyParameter("pin", oauthVerifier);
        } else {
            request.addBodyParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.AUTHORIZATION_CODE);
            request.addBodyParameter(OAuthConstants.CODE, oauthVerifier);
        }

        final String pkceCodeVerifier = params.getPkceCodeVerifier();
        if (pkceCodeVerifier != null) {
            request.addParameter(PKCE.PKCE_CODE_VERIFIER_PARAM, pkceCodeVerifier);
        }

        return request;
    }

    @Override
    public void signRequest(String accessToken, OAuthRequest request) {
        request.addHeader(OAuthConstants.HEADER,
                accessToken == null ? "Client-ID " + getApiKey() : "Bearer " + accessToken);
    }
}
