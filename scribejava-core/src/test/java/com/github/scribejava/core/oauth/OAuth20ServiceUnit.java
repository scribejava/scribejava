package com.github.scribejava.core.oauth;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Parameter;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

class OAuth20ServiceUnit extends OAuth20Service {

    static final String TOKEN = "ae82980abab675c646a070686d5558ad";
    static final String STATE = "123";
    static final String EXPIRES = "3600";

    OAuth20ServiceUnit(DefaultApi20 api, String apiKey, String apiSecret, String callback, String scope, String state,
            String responseType, String userAgent, HttpClientConfig httpClientConfig, HttpClient httpClient) {
        super(api, apiKey, apiSecret, callback, scope, state, responseType, userAgent, httpClientConfig, httpClient);
    }

    @Override
    protected OAuth2AccessToken sendAccessTokenRequestSync(OAuthRequest request) {
        return new OAuth2AccessToken(TOKEN, prepareRawResponse(request));
    }

    private String prepareRawResponse(OAuthRequest request) {
        final Gson json = new Gson();
        final Map<String, String> response = new HashMap<>();
        response.put(OAuthConstants.ACCESS_TOKEN, TOKEN);
        response.put(OAuthConstants.STATE, STATE);
        response.put("expires_in", EXPIRES);

        response.putAll(request.getHeaders());
        response.putAll(request.getOauthParameters());

        for (Parameter param : request.getBodyParams().getParams()) {
            response.put("query-" + param.getKey(), param.getValue());
        }

        return json.toJson(response);
    }

    @Override
    protected Future<OAuth2AccessToken> sendAccessTokenRequestAsync(OAuthRequest request,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback) {

        final OAuth2AccessToken accessToken = new OAuth2AccessToken(TOKEN, prepareRawResponse(request));

        try {
            return new CompletedFuture<>(accessToken);
        } finally {
            if (callback != null) {
                callback.onCompleted(accessToken);
            }
        }
    }
}
