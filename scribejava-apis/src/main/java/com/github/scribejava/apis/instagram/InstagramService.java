package com.github.scribejava.apis.instagram;

import com.github.scribejava.apis.InstagramApi;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import java.util.concurrent.Future;

public class InstagramService extends OAuth20Service {

    public InstagramService(InstagramApi api, String apiKey, String apiSecret, String callback, String defaultScope,
            String responseType, OutputStream debugStream, String userAgent, HttpClientConfig httpClientConfig,
            HttpClient httpClient) {
        super(api, apiKey, apiSecret, callback, defaultScope, responseType, debugStream, userAgent, httpClientConfig,
                httpClient);
    }

    /**
     * Refresh a long-lived Instagram User Access Token that is at least 24 hours old but has not expired. Refreshed
     * tokens are valid for 60 days from the date at which they are refreshed.
     *
     * @param accessToken long-lived access token
     * @param scope (not used)
     * @return refresh token request
     */
    @Override
    protected OAuthRequest createRefreshTokenRequest(String accessToken, String scope) {
        if (accessToken == null || accessToken.isEmpty()) {
            throw new IllegalArgumentException("The accessToken cannot be null or empty");
        }
        final OAuthRequest request = new OAuthRequest(Verb.GET, getApi().getRefreshTokenEndpoint());

        request.addParameter(OAuthConstants.GRANT_TYPE, "ig_refresh_token");
        request.addParameter(OAuthConstants.ACCESS_TOKEN, accessToken);

        logRequestWithParams("refresh token", request);

        return request;
    }

    public Future<OAuth2AccessToken> getLongLivedAccessTokenAsync(OAuth2AccessToken accessToken) {
        return getLongLivedAccessToken(accessToken.getAccessToken(), null);
    }

    public Future<OAuth2AccessToken> getLongLivedAccessTokenAsync(String shortLivedAccessToken) {
        return getLongLivedAccessToken(shortLivedAccessToken, null);
    }

    public Future<OAuth2AccessToken> getLongLivedAccessToken(String shortLivedAccessToken,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback) {
        return sendAccessTokenRequestAsync(createLongLivedAccessTokenRequest(shortLivedAccessToken), callback);
    }

    public Future<OAuth2AccessToken> getLongLivedAccessToken(OAuth2AccessToken accessToken,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback) {
        return getLongLivedAccessToken(accessToken.getAccessToken(), callback);
    }

    /**
     * Get long-lived access token.
     *
     * Initial accessToken is valid for 1 hour so one can get long-lived access token. Long-lived access token is valid
     * for 60 days.
     *
     * @param accessToken short-lived access token
     * @return long-lived access token with filled expireIn and refreshToken
     * @throws java.lang.InterruptedException
     * @throws java.util.concurrent.ExecutionException
     * @throws java.io.IOException
     */
    public OAuth2AccessToken getLongLivedAccessToken(OAuth2AccessToken accessToken)
            throws InterruptedException, ExecutionException, IOException {
        return getLongLivedAccessToken(accessToken.getAccessToken());
    }

    public OAuth2AccessToken getLongLivedAccessToken(String shortLivedAccessToken)
            throws InterruptedException, ExecutionException, IOException {
        final OAuthRequest request = createLongLivedAccessTokenRequest(shortLivedAccessToken);
        return sendAccessTokenRequestSync(request);
    }

    private OAuthRequest createLongLivedAccessTokenRequest(String shortLivedAccessToken) {
        final OAuthRequest request = new OAuthRequest(Verb.GET, InstagramApi.LONG_LIVED_ACCESS_TOKEN_ENDPOINT);

        getApi().getClientAuthentication().addClientAuthentication(request, getApiKey(), getApiSecret());

        request.addParameter(OAuthConstants.GRANT_TYPE, "ig_exchange_token");
        request.addParameter(OAuthConstants.ACCESS_TOKEN, shortLivedAccessToken);

        if (isDebug()) {
            log("created long-lived access token request with body params [%s], query string params [%s]",
                    request.getBodyParams().asFormUrlEncodedString(),
                    request.getQueryStringParams().asFormUrlEncodedString());
        }
        return request;
    }
}
