package com.github.scribejava.core.oauth;

import java.io.IOException;
import java.util.concurrent.Future;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuth2Authorization;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.pkce.AuthorizationUrlWithPKCE;
import com.github.scribejava.core.pkce.PKCE;
import com.github.scribejava.core.pkce.PKCEService;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import com.github.scribejava.core.revoke.TokenTypeHint;

public class OAuth20Service extends OAuthService {

    private static final String VERSION = "2.0";
    private static final PKCEService PKCE_SERVICE = new PKCEService();
    private final DefaultApi20 api;
    private final String responseType;
    private final String state;

    public OAuth20Service(DefaultApi20 api, String apiKey, String apiSecret, String callback, String scope,
            String state, String responseType, String userAgent, HttpClientConfig httpClientConfig,
            HttpClient httpClient) {
        super(apiKey, apiSecret, callback, scope, userAgent, httpClientConfig, httpClient);
        this.responseType = responseType;
        this.state = state;
        this.api = api;
    }

    //protected to facilitate mocking
    protected OAuth2AccessToken sendAccessTokenRequestSync(OAuthRequest request)
            throws IOException, InterruptedException, ExecutionException {
        return api.getAccessTokenExtractor().extract(execute(request));
    }

    //protected to facilitate mocking
    protected Future<OAuth2AccessToken> sendAccessTokenRequestAsync(OAuthRequest request) {
        return sendAccessTokenRequestAsync(request, null);
    }

    //protected to facilitate mocking
    protected Future<OAuth2AccessToken> sendAccessTokenRequestAsync(OAuthRequest request,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback) {

        return execute(request, callback, new OAuthRequest.ResponseConverter<OAuth2AccessToken>() {
            @Override
            public OAuth2AccessToken convert(Response response) throws IOException {
                return getApi().getAccessTokenExtractor().extract(response);
            }
        });
    }

    public Future<OAuth2AccessToken> getAccessTokenAsync(String code) {
        return getAccessToken(code, null, null);
    }

    public Future<OAuth2AccessToken> getAccessTokenAsync(String code, String pkceCodeVerifier) {
        return getAccessToken(code, null, pkceCodeVerifier);
    }

    public OAuth2AccessToken getAccessToken(String code) throws IOException, InterruptedException, ExecutionException {
        return getAccessToken(code, (String) null);
    }

    public OAuth2AccessToken getAccessToken(String code, String pkceCodeVerifier)
            throws IOException, InterruptedException, ExecutionException {
        final OAuthRequest request = createAccessTokenRequest(code, pkceCodeVerifier);

        return sendAccessTokenRequestSync(request);
    }

    /**
     * Start the request to retrieve the access token. The optionally provided callback will be called with the Token
     * when it is available.
     *
     * @param code code
     * @param callback optional callback
     * @param pkceCodeVerifier pkce Code Verifier
     * @return Future
     */
    public Future<OAuth2AccessToken> getAccessToken(String code, OAuthAsyncRequestCallback<OAuth2AccessToken> callback,
            String pkceCodeVerifier) {
        final OAuthRequest request = createAccessTokenRequest(code, pkceCodeVerifier);

        return sendAccessTokenRequestAsync(request, callback);
    }

    public Future<OAuth2AccessToken> getAccessToken(String code,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback) {

        return getAccessToken(code, callback, null);
    }

    protected OAuthRequest createAccessTokenRequest(String code) {
        final OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());

        api.getClientAuthentication().addClientAuthentication(request, getApiKey(), getApiSecret());

        request.addParameter(OAuthConstants.CODE, code);
        final String callback = getCallback();
        if (callback != null) {
            request.addParameter(OAuthConstants.REDIRECT_URI, callback);
        }
        final String scope = getScope();
        if (scope != null) {
            request.addParameter(OAuthConstants.SCOPE, scope);
        }
        request.addParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.AUTHORIZATION_CODE);
        return request;
    }

    protected OAuthRequest createAccessTokenRequest(String code, String pkceCodeVerifier) {
        final OAuthRequest request = createAccessTokenRequest(code);
        if (pkceCodeVerifier != null) {
            request.addParameter(PKCE.PKCE_CODE_VERIFIER_PARAM, pkceCodeVerifier);
        }
        return request;
    }

    public Future<OAuth2AccessToken> refreshAccessTokenAsync(String refreshToken) {
        return refreshAccessToken(refreshToken, null);
    }

    public OAuth2AccessToken refreshAccessToken(String refreshToken)
            throws IOException, InterruptedException, ExecutionException {
        final OAuthRequest request = createRefreshTokenRequest(refreshToken);

        return sendAccessTokenRequestSync(request);
    }

    public Future<OAuth2AccessToken> refreshAccessToken(String refreshToken,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback) {
        final OAuthRequest request = createRefreshTokenRequest(refreshToken);

        return sendAccessTokenRequestAsync(request, callback);
    }

    protected OAuthRequest createRefreshTokenRequest(String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new IllegalArgumentException("The refreshToken cannot be null or empty");
        }
        final OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getRefreshTokenEndpoint());

        api.getClientAuthentication().addClientAuthentication(request, getApiKey(), getApiSecret());

        final String scope = getScope();
        if (scope != null) {
            request.addParameter(OAuthConstants.SCOPE, scope);
        }

        request.addParameter(OAuthConstants.REFRESH_TOKEN, refreshToken);
        request.addParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.REFRESH_TOKEN);
        return request;
    }

    public OAuth2AccessToken getAccessTokenPasswordGrant(String uname, String password)
            throws IOException, InterruptedException, ExecutionException {
        final OAuthRequest request = createAccessTokenPasswordGrantRequest(uname, password);

        return sendAccessTokenRequestSync(request);
    }

    public Future<OAuth2AccessToken> getAccessTokenPasswordGrantAsync(String uname, String password) {
        return getAccessTokenPasswordGrantAsync(uname, password, null);
    }

    /**
     * Request Access Token Password Grant async version
     *
     * @param uname User name
     * @param password User password
     * @param callback Optional callback
     * @return Future
     */
    public Future<OAuth2AccessToken> getAccessTokenPasswordGrantAsync(String uname, String password,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback) {
        final OAuthRequest request = createAccessTokenPasswordGrantRequest(uname, password);

        return sendAccessTokenRequestAsync(request, callback);
    }

    protected OAuthRequest createAccessTokenPasswordGrantRequest(String username, String password) {
        final OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        request.addParameter(OAuthConstants.USERNAME, username);
        request.addParameter(OAuthConstants.PASSWORD, password);

        final String scope = getScope();
        if (scope != null) {
            request.addParameter(OAuthConstants.SCOPE, scope);
        }

        request.addParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.PASSWORD);

        api.getClientAuthentication().addClientAuthentication(request, getApiKey(), getApiSecret());

        return request;
    }

    public Future<OAuth2AccessToken> getAccessTokenClientCredentialsGrantAsync() {
        return getAccessTokenClientCredentialsGrant(null);
    }

    public OAuth2AccessToken getAccessTokenClientCredentialsGrant()
            throws IOException, InterruptedException, ExecutionException {
        final OAuthRequest request = createAccessTokenClientCredentialsGrantRequest();

        return sendAccessTokenRequestSync(request);
    }

    /**
     * Start the request to retrieve the access token using client-credentials grant. The optionally provided callback
     * will be called with the Token when it is available.
     *
     * @param callback optional callback
     * @return Future
     */
    public Future<OAuth2AccessToken> getAccessTokenClientCredentialsGrant(
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback) {
        final OAuthRequest request = createAccessTokenClientCredentialsGrantRequest();

        return sendAccessTokenRequestAsync(request, callback);
    }

    protected OAuthRequest createAccessTokenClientCredentialsGrantRequest() {
        final OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());

        api.getClientAuthentication().addClientAuthentication(request, getApiKey(), getApiSecret());

        final String scope = getScope();
        if (scope != null) {
            request.addParameter(OAuthConstants.SCOPE, scope);
        }
        request.addParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.CLIENT_CREDENTIALS);
        return request;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVersion() {
        return VERSION;
    }

    public void signRequest(String accessToken, OAuthRequest request) {
        api.getBearerSignature().signRequest(accessToken, request);
    }

    public void signRequest(OAuth2AccessToken accessToken, OAuthRequest request) {
        signRequest(accessToken == null ? null : accessToken.getAccessToken(), request);
    }

    public AuthorizationUrlWithPKCE getAuthorizationUrlWithPKCE() {
        return getAuthorizationUrlWithPKCE(null);
    }

    public AuthorizationUrlWithPKCE getAuthorizationUrlWithPKCE(Map<String, String> additionalParams) {
        final PKCE pkce = PKCE_SERVICE.generatePKCE();
        return new AuthorizationUrlWithPKCE(pkce, getAuthorizationUrl(additionalParams, pkce));
    }

    /**
     * Returns the URL where you should redirect your users to authenticate your application.
     *
     * @return the URL where you should redirect your users
     */
    public String getAuthorizationUrl() {
        return getAuthorizationUrl(null, null);
    }

    /**
     * Returns the URL where you should redirect your users to authenticate your application.
     *
     * @param additionalParams any additional GET params to add to the URL
     * @return the URL where you should redirect your users
     */
    public String getAuthorizationUrl(Map<String, String> additionalParams) {
        return getAuthorizationUrl(additionalParams, null);
    }

    public String getAuthorizationUrl(PKCE pkce) {
        return getAuthorizationUrl(null, pkce);
    }

    public String getAuthorizationUrl(Map<String, String> additionalParams, PKCE pkce) {
        final Map<String, String> params;
        if (pkce == null) {
            params = additionalParams;
        } else {
            params = additionalParams == null ? new HashMap<String, String>() : new HashMap<>(additionalParams);
            params.putAll(pkce.getAuthorizationUrlParams());
        }
        return api.getAuthorizationUrl(getResponseType(), getApiKey(), getCallback(), getScope(), getState(), params);
    }

    public DefaultApi20 getApi() {
        return api;
    }

    protected OAuthRequest createRevokeTokenRequest(String tokenToRevoke, TokenTypeHint tokenTypeHint) {
        final OAuthRequest request = new OAuthRequest(Verb.POST, api.getRevokeTokenEndpoint());

        api.getClientAuthentication().addClientAuthentication(request, getApiKey(), getApiSecret());

        request.addParameter("token", tokenToRevoke);
        if (tokenTypeHint != null) {
            request.addParameter("token_type_hint", tokenTypeHint.toString());
        }
        return request;
    }

    public Future<Void> revokeTokenAsync(String tokenToRevoke) {
        return revokeTokenAsync(tokenToRevoke, null);
    }

    public Future<Void> revokeTokenAsync(String tokenToRevoke, TokenTypeHint tokenTypeHint) {
        return revokeToken(tokenToRevoke, null, tokenTypeHint);
    }

    public void revokeToken(String tokenToRevoke) throws IOException, InterruptedException, ExecutionException {
        revokeToken(tokenToRevoke, (TokenTypeHint) null);
    }

    public void revokeToken(String tokenToRevoke, TokenTypeHint tokenTypeHint)
            throws IOException, InterruptedException, ExecutionException {
        final OAuthRequest request = createRevokeTokenRequest(tokenToRevoke, tokenTypeHint);

        checkForErrorRevokeToken(execute(request));
    }

    public Future<Void> revokeToken(String tokenToRevoke, OAuthAsyncRequestCallback<Void> callback) {
        return revokeToken(tokenToRevoke, callback, null);
    }

    public Future<Void> revokeToken(String tokenToRevoke, OAuthAsyncRequestCallback<Void> callback,
            TokenTypeHint tokenTypeHint) {
        final OAuthRequest request = createRevokeTokenRequest(tokenToRevoke, tokenTypeHint);

        return execute(request, callback, new OAuthRequest.ResponseConverter<Void>() {
            @Override
            public Void convert(Response response) throws IOException {
                checkForErrorRevokeToken(response);
                return null;
            }
        });
    }

    private void checkForErrorRevokeToken(Response response) throws IOException {
        if (response.getCode() != 200) {
            OAuth2AccessTokenJsonExtractor.instance().generateError(response.getBody());
        }
    }

    public OAuth2Authorization extractAuthorization(String redirectLocation) {
        final OAuth2Authorization authorization = new OAuth2Authorization();
        int end = redirectLocation.indexOf('#');
        if (end == -1) {
            end = redirectLocation.length();
        }
        for (String param : redirectLocation.substring(redirectLocation.indexOf('?') + 1, end).split("&")) {
            final String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                switch (keyValue[0]) {
                    case "code":
                        authorization.setCode(keyValue[1]);
                        break;
                    case "state":
                        authorization.setState(keyValue[1]);
                        break;
                    default: //just ignore any other param;
                }
            }
        }
        return authorization;
    }

    public String getResponseType() {
        return responseType;
    }

    public String getState() {
        return state;
    }
}
