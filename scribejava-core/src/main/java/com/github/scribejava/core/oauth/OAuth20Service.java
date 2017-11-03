package com.github.scribejava.core.oauth;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.Future;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuth2Authorization;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.pkce.AuthorizationUrlWithPKCE;
import com.github.scribejava.core.pkce.PKCE;
import com.github.scribejava.core.pkce.PKCEService;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class OAuth20Service extends OAuthService<OAuth2AccessToken> {

    private static final String VERSION = "2.0";
    private static final Base64.Encoder BASE_64_ENCODER = Base64.getEncoder();
    private static final PKCEService PKCE_SERVICE = new PKCEService();
    private final DefaultApi20 api;

    /**
     * Default constructor
     *
     * @param api OAuth2.0 api information
     * @param config OAuth 2.0 configuration param object
     */
    public OAuth20Service(DefaultApi20 api, OAuthConfig config) {
        super(config);
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

        return execute(request, callback, response -> getApi().getAccessTokenExtractor().extract(response));
    }

    public final Future<OAuth2AccessToken> getAccessTokenAsync(String code) {
        return getAccessToken(code, null, null);
    }

    public final Future<OAuth2AccessToken> getAccessTokenAsync(String code, String pkceCodeVerifier) {
        return getAccessToken(code, null, pkceCodeVerifier);
    }

    public final OAuth2AccessToken getAccessToken(String code)
            throws IOException, InterruptedException, ExecutionException {
        return getAccessToken(code, (String) null);
    }

    public final OAuth2AccessToken getAccessToken(String code, String pkceCodeVerifier)
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
    public final Future<OAuth2AccessToken> getAccessToken(String code,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback, String pkceCodeVerifier) {
        final OAuthRequest request = createAccessTokenRequest(code, pkceCodeVerifier);

        return sendAccessTokenRequestAsync(request, callback);
    }

    public final Future<OAuth2AccessToken> getAccessToken(String code,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback) {

        return getAccessToken(code, callback, null);
    }

    protected OAuthRequest createAccessTokenRequest(String code) {
        final OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        final OAuthConfig config = getConfig();
        request.addParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        final String apiSecret = config.getApiSecret();
        if (apiSecret != null) {
            request.addParameter(OAuthConstants.CLIENT_SECRET, apiSecret);
        }
        request.addParameter(OAuthConstants.CODE, code);
        request.addParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
        final String scope = config.getScope();
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

    public final Future<OAuth2AccessToken> refreshAccessTokenAsync(String refreshToken) {
        return refreshAccessToken(refreshToken, null);
    }

    public final OAuth2AccessToken refreshAccessToken(String refreshToken)
            throws IOException, InterruptedException, ExecutionException {
        final OAuthRequest request = createRefreshTokenRequest(refreshToken);

        return sendAccessTokenRequestSync(request);
    }

    public final Future<OAuth2AccessToken> refreshAccessToken(String refreshToken,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback) {
        final OAuthRequest request = createRefreshTokenRequest(refreshToken);

        return sendAccessTokenRequestAsync(request, callback);
    }

    protected OAuthRequest createRefreshTokenRequest(String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new IllegalArgumentException("The refreshToken cannot be null or empty");
        }
        final OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getRefreshTokenEndpoint());
        final OAuthConfig config = getConfig();
        request.addParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        final String apiSecret = config.getApiSecret();
        if (apiSecret != null) {
            request.addParameter(OAuthConstants.CLIENT_SECRET, apiSecret);
        }
        request.addParameter(OAuthConstants.REFRESH_TOKEN, refreshToken);
        request.addParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.REFRESH_TOKEN);
        return request;
    }

    public final OAuth2AccessToken getAccessTokenPasswordGrant(String uname, String password)
            throws IOException, InterruptedException, ExecutionException {
        final OAuthRequest request = createAccessTokenPasswordGrantRequest(uname, password);

        return sendAccessTokenRequestSync(request);
    }

    public final Future<OAuth2AccessToken> getAccessTokenPasswordGrantAsync(String uname, String password) {
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
    public final Future<OAuth2AccessToken> getAccessTokenPasswordGrantAsync(String uname, String password,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback) {
        final OAuthRequest request = createAccessTokenPasswordGrantRequest(uname, password);

        return sendAccessTokenRequestAsync(request, callback);
    }

    protected OAuthRequest createAccessTokenPasswordGrantRequest(String username, String password) {
        final OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        final OAuthConfig config = getConfig();
        request.addParameter(OAuthConstants.USERNAME, username);
        request.addParameter(OAuthConstants.PASSWORD, password);

        final String scope = config.getScope();
        if (scope != null) {
            request.addParameter(OAuthConstants.SCOPE, scope);
        }

        request.addParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.PASSWORD);

        final String apiKey = config.getApiKey();
        final String apiSecret = config.getApiSecret();
        if (apiKey != null && apiSecret != null) {
            request.addHeader(OAuthConstants.HEADER, OAuthConstants.BASIC + ' '
                    + BASE_64_ENCODER.encodeToString(
                            String.format("%s:%s", apiKey, apiSecret).getBytes(Charset.forName("UTF-8"))));
        }

        return request;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public void signRequest(OAuth2AccessToken accessToken, OAuthRequest request) {
        api.getSignatureType().signRequest(accessToken, request);
    }

    public final AuthorizationUrlWithPKCE getAuthorizationUrlWithPKCE() {
        return getAuthorizationUrlWithPKCE(null);
    }

    public final AuthorizationUrlWithPKCE getAuthorizationUrlWithPKCE(Map<String, String> additionalParams) {
        final PKCE pkce = PKCE_SERVICE.generatePKCE();
        return new AuthorizationUrlWithPKCE(pkce, getAuthorizationUrl(additionalParams, pkce));
    }

    /**
     * Returns the URL where you should redirect your users to authenticate your application.
     *
     * @return the URL where you should redirect your users
     */
    public final String getAuthorizationUrl() {
        return getAuthorizationUrl(null, null);
    }

    /**
     * Returns the URL where you should redirect your users to authenticate your application.
     *
     * @param additionalParams any additional GET params to add to the URL
     * @return the URL where you should redirect your users
     */
    public final String getAuthorizationUrl(Map<String, String> additionalParams) {
        return getAuthorizationUrl(additionalParams, null);
    }

    public final String getAuthorizationUrl(PKCE pkce) {
        return getAuthorizationUrl(null, pkce);
    }

    public String getAuthorizationUrl(Map<String, String> additionalParams, PKCE pkce) {
        final Map<String, String> params;
        if (pkce == null) {
            params = additionalParams;
        } else {
            params = additionalParams == null ? new HashMap<>() : new HashMap<>(additionalParams);
            params.putAll(pkce.getAuthorizationUrlParams());
        }
        return api.getAuthorizationUrl(getConfig(), params);
    }

    public DefaultApi20 getApi() {
        return api;
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
}
