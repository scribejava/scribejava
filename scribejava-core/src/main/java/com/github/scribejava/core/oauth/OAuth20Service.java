package com.github.scribejava.core.oauth;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.DeviceAuthorization;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuth2AccessTokenErrorResponse;
import com.github.scribejava.core.model.OAuth2Authorization;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth2.OAuth2Error;
import com.github.scribejava.core.pkce.PKCE;
import com.github.scribejava.core.revoke.TokenTypeHint;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class OAuth20Service extends OAuthService {

    private static final String VERSION = "2.0";
    private final DefaultApi20 api;
    private final String responseType;
    private final String defaultScope;

    public OAuth20Service(DefaultApi20 api, String apiKey, String apiSecret, String callback, String defaultScope,
            String responseType, OutputStream debugStream, String userAgent, HttpClientConfig httpClientConfig,
            HttpClient httpClient) {
        super(apiKey, apiSecret, callback, debugStream, userAgent, httpClientConfig, httpClient);
        this.responseType = responseType;
        this.api = api;
        this.defaultScope = defaultScope;
    }

    //protected to facilitate mocking
    protected OAuth2AccessToken sendAccessTokenRequestSync(OAuthRequest request)
            throws IOException, InterruptedException, ExecutionException {
        if (isDebug()) {
            log("send request for access token synchronously to %s", request.getCompleteUrl());
        }
        try (Response response = execute(request)) {
            if (isDebug()) {
                log("response status code: %s", response.getCode());
                log("response body: %s", response.getBody());
            }

            return api.getAccessTokenExtractor().extract(response);
        }
    }

    //protected to facilitate mocking
    protected Future<OAuth2AccessToken> sendAccessTokenRequestAsync(OAuthRequest request) {
        return sendAccessTokenRequestAsync(request, null);
    }

    //protected to facilitate mocking
    protected Future<OAuth2AccessToken> sendAccessTokenRequestAsync(OAuthRequest request,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback) {
        if (isDebug()) {
            log("send request for access token asynchronously to %s", request.getCompleteUrl());
        }

        return execute(request, callback, new OAuthRequest.ResponseConverter<OAuth2AccessToken>() {
            @Override
            public OAuth2AccessToken convert(Response response) throws IOException {
                log("received response for access token");
                if (isDebug()) {
                    log("response status code: %s", response.getCode());
                    log("response body: %s", response.getBody());
                }
                final OAuth2AccessToken token = api.getAccessTokenExtractor().extract(response);
                response.close();
                return token;
            }
        });
    }

    public Future<OAuth2AccessToken> getAccessTokenAsync(String code) {
        return getAccessToken(AccessTokenRequestParams.create(code), null);
    }

    public Future<OAuth2AccessToken> getAccessTokenAsync(AccessTokenRequestParams params) {
        return getAccessToken(params, null);
    }

    public OAuth2AccessToken getAccessToken(String code) throws IOException, InterruptedException, ExecutionException {
        return getAccessToken(AccessTokenRequestParams.create(code));
    }

    public OAuth2AccessToken getAccessToken(AccessTokenRequestParams params)
            throws IOException, InterruptedException, ExecutionException {
        return sendAccessTokenRequestSync(createAccessTokenRequest(params));
    }

    /**
     * Start the request to retrieve the access token. The optionally provided callback will be called with the Token
     * when it is available.
     *
     * @param params params
     * @param callback optional callback
     * @return Future
     */
    public Future<OAuth2AccessToken> getAccessToken(AccessTokenRequestParams params,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback) {
        return sendAccessTokenRequestAsync(createAccessTokenRequest(params), callback);
    }

    public Future<OAuth2AccessToken> getAccessToken(String code,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback) {
        return getAccessToken(AccessTokenRequestParams.create(code), callback);
    }

    protected OAuthRequest createAccessTokenRequest(AccessTokenRequestParams params) {
        final OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());

        api.getClientAuthentication().addClientAuthentication(request, getApiKey(), getApiSecret());

        request.addParameter(OAuthConstants.CODE, params.getCode());
        final String callback = getCallback();
        if (callback != null) {
            request.addParameter(OAuthConstants.REDIRECT_URI, callback);
        }
        final String scope = params.getScope();
        if (scope != null) {
            request.addParameter(OAuthConstants.SCOPE, scope);
        } else if (defaultScope != null) {
            request.addParameter(OAuthConstants.SCOPE, defaultScope);
        }
        request.addParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.AUTHORIZATION_CODE);

        final String pkceCodeVerifier = params.getPkceCodeVerifier();
        if (pkceCodeVerifier != null) {
            request.addParameter(PKCE.PKCE_CODE_VERIFIER_PARAM, pkceCodeVerifier);
        }
        logRequestWithParams("access token", request);
        return request;
    }

    public Future<OAuth2AccessToken> refreshAccessTokenAsync(String refreshToken) {
        return refreshAccessToken(refreshToken, (OAuthAsyncRequestCallback<OAuth2AccessToken>) null);
    }

    public Future<OAuth2AccessToken> refreshAccessTokenAsync(String refreshToken, String scope) {
        return refreshAccessToken(refreshToken, scope, null);
    }

    public OAuth2AccessToken refreshAccessToken(String refreshToken)
            throws IOException, InterruptedException, ExecutionException {
        return refreshAccessToken(refreshToken, (String) null);
    }

    public OAuth2AccessToken refreshAccessToken(String refreshToken, String scope)
            throws IOException, InterruptedException, ExecutionException {
        final OAuthRequest request = createRefreshTokenRequest(refreshToken, scope);

        return sendAccessTokenRequestSync(request);
    }

    public Future<OAuth2AccessToken> refreshAccessToken(String refreshToken,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback) {
        final OAuthRequest request = createRefreshTokenRequest(refreshToken, null);

        return sendAccessTokenRequestAsync(request, callback);
    }

    public Future<OAuth2AccessToken> refreshAccessToken(String refreshToken, String scope,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback) {
        final OAuthRequest request = createRefreshTokenRequest(refreshToken, scope);

        return sendAccessTokenRequestAsync(request, callback);
    }

    protected OAuthRequest createRefreshTokenRequest(String refreshToken, String scope) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new IllegalArgumentException("The refreshToken cannot be null or empty");
        }
        final OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getRefreshTokenEndpoint());

        api.getClientAuthentication().addClientAuthentication(request, getApiKey(), getApiSecret());

        if (scope != null) {
            request.addParameter(OAuthConstants.SCOPE, scope);
        } else if (defaultScope != null) {
            request.addParameter(OAuthConstants.SCOPE, defaultScope);
        }

        request.addParameter(OAuthConstants.REFRESH_TOKEN, refreshToken);
        request.addParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.REFRESH_TOKEN);

        logRequestWithParams("refresh token", request);

        return request;
    }

    public OAuth2AccessToken getAccessTokenPasswordGrant(String username, String password)
            throws IOException, InterruptedException, ExecutionException {
        final OAuthRequest request = createAccessTokenPasswordGrantRequest(username, password, null);

        return sendAccessTokenRequestSync(request);
    }

    public OAuth2AccessToken getAccessTokenPasswordGrant(String username, String password, String scope)
            throws IOException, InterruptedException, ExecutionException {
        final OAuthRequest request = createAccessTokenPasswordGrantRequest(username, password, scope);

        return sendAccessTokenRequestSync(request);
    }

    public Future<OAuth2AccessToken> getAccessTokenPasswordGrantAsync(String username, String password) {
        return getAccessTokenPasswordGrantAsync(username, password,
                (OAuthAsyncRequestCallback<OAuth2AccessToken>) null);
    }

    public Future<OAuth2AccessToken> getAccessTokenPasswordGrantAsync(String username, String password, String scope) {
        return getAccessTokenPasswordGrantAsync(username, password, scope, null);
    }

    /**
     * Request Access Token Password Grant async version
     *
     * @param username User name
     * @param password User password
     * @param callback Optional callback
     * @return Future
     */
    public Future<OAuth2AccessToken> getAccessTokenPasswordGrantAsync(String username, String password,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback) {
        final OAuthRequest request = createAccessTokenPasswordGrantRequest(username, password, null);

        return sendAccessTokenRequestAsync(request, callback);
    }

    public Future<OAuth2AccessToken> getAccessTokenPasswordGrantAsync(String username, String password, String scope,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback) {
        final OAuthRequest request = createAccessTokenPasswordGrantRequest(username, password, scope);

        return sendAccessTokenRequestAsync(request, callback);
    }

    protected OAuthRequest createAccessTokenPasswordGrantRequest(String username, String password, String scope) {
        final OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        request.addParameter(OAuthConstants.USERNAME, username);
        request.addParameter(OAuthConstants.PASSWORD, password);

        if (scope != null) {
            request.addParameter(OAuthConstants.SCOPE, scope);
        } else if (defaultScope != null) {
            request.addParameter(OAuthConstants.SCOPE, defaultScope);
        }

        request.addParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.PASSWORD);

        api.getClientAuthentication().addClientAuthentication(request, getApiKey(), getApiSecret());

        logRequestWithParams("access token password grant", request);

        return request;
    }

    public Future<OAuth2AccessToken> getAccessTokenClientCredentialsGrantAsync() {
        return getAccessTokenClientCredentialsGrant((OAuthAsyncRequestCallback<OAuth2AccessToken>) null);
    }

    public Future<OAuth2AccessToken> getAccessTokenClientCredentialsGrantAsync(String scope) {
        return getAccessTokenClientCredentialsGrant(scope, null);
    }

    public OAuth2AccessToken getAccessTokenClientCredentialsGrant()
            throws IOException, InterruptedException, ExecutionException {
        final OAuthRequest request = createAccessTokenClientCredentialsGrantRequest(null);

        return sendAccessTokenRequestSync(request);
    }

    public OAuth2AccessToken getAccessTokenClientCredentialsGrant(String scope)
            throws IOException, InterruptedException, ExecutionException {
        final OAuthRequest request = createAccessTokenClientCredentialsGrantRequest(scope);

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
        final OAuthRequest request = createAccessTokenClientCredentialsGrantRequest(null);

        return sendAccessTokenRequestAsync(request, callback);
    }

    public Future<OAuth2AccessToken> getAccessTokenClientCredentialsGrant(String scope,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback) {
        final OAuthRequest request = createAccessTokenClientCredentialsGrantRequest(scope);

        return sendAccessTokenRequestAsync(request, callback);
    }

    protected OAuthRequest createAccessTokenClientCredentialsGrantRequest(String scope) {
        final OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());

        api.getClientAuthentication().addClientAuthentication(request, getApiKey(), getApiSecret());

        if (scope != null) {
            request.addParameter(OAuthConstants.SCOPE, scope);
        } else if (defaultScope != null) {
            request.addParameter(OAuthConstants.SCOPE, defaultScope);
        }
        request.addParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.CLIENT_CREDENTIALS);

        logRequestWithParams("access token client credentials grant", request);

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

    /**
     * Returns the URL where you should redirect your users to authenticate your application.
     *
     * @return the URL where you should redirect your users
     */
    public String getAuthorizationUrl() {
        return createAuthorizationUrlBuilder().build();
    }

    public String getAuthorizationUrl(String state) {
        return createAuthorizationUrlBuilder()
                .state(state)
                .build();
    }

    /**
     * Returns the URL where you should redirect your users to authenticate your application.
     *
     * @param additionalParams any additional GET params to add to the URL
     * @return the URL where you should redirect your users
     */
    public String getAuthorizationUrl(Map<String, String> additionalParams) {
        return createAuthorizationUrlBuilder()
                .additionalParams(additionalParams)
                .build();
    }

    public String getAuthorizationUrl(PKCE pkce) {
        return createAuthorizationUrlBuilder()
                .pkce(pkce)
                .build();
    }

    public AuthorizationUrlBuilder createAuthorizationUrlBuilder() {
        return new AuthorizationUrlBuilder(this);
    }

    public DefaultApi20 getApi() {
        return api;
    }

    protected OAuthRequest createRevokeTokenRequest(String tokenToRevoke, TokenTypeHint tokenTypeHint) {
        final OAuthRequest request = new OAuthRequest(Verb.POST, api.getRevokeTokenEndpoint());

        api.getClientAuthentication().addClientAuthentication(request, getApiKey(), getApiSecret());

        request.addParameter("token", tokenToRevoke);
        if (tokenTypeHint != null) {
            request.addParameter("token_type_hint", tokenTypeHint.getValue());
        }

        logRequestWithParams("revoke token", request);

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

        try (Response response = execute(request)) {
            checkForErrorRevokeToken(response);
        }
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
                response.close();
                return null;
            }
        });
    }

    private void checkForErrorRevokeToken(Response response) throws IOException {
        if (response.getCode() != 200) {
            OAuth2AccessTokenJsonExtractor.instance().generateError(response);
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
                try {
                    switch (keyValue[0]) {
                        case "code":
                            authorization.setCode(URLDecoder.decode(keyValue[1], "UTF-8"));
                            break;
                        case "state":
                            authorization.setState(URLDecoder.decode(keyValue[1], "UTF-8"));
                            break;
                        default: //just ignore any other param;
                    }
                } catch (UnsupportedEncodingException ueE) {
                    throw new IllegalStateException("jvm without UTF-8, really?", ueE);
                }
            }
        }
        return authorization;
    }

    public String getResponseType() {
        return responseType;
    }

    public String getDefaultScope() {
        return defaultScope;
    }

    protected OAuthRequest createDeviceAuthorizationCodesRequest(String scope) {
        final OAuthRequest request = new OAuthRequest(Verb.POST, api.getDeviceAuthorizationEndpoint());
        request.addParameter(OAuthConstants.CLIENT_ID, getApiKey());
        if (scope != null) {
            request.addParameter(OAuthConstants.SCOPE, scope);
        } else if (defaultScope != null) {
            request.addParameter(OAuthConstants.SCOPE, defaultScope);
        }

        logRequestWithParams("Device Authorization Codes", request);

        return request;
    }

    /**
     * Requests a set of verification codes from the authorization server with the default scope
     *
     * @see <a href="https://tools.ietf.org/html/rfc8628#section-3.1">RFC 8628</a>
     *
     * @return DeviceAuthorization
     * @throws InterruptedException InterruptedException
     * @throws ExecutionException ExecutionException
     * @throws IOException IOException
     */
    public DeviceAuthorization getDeviceAuthorizationCodes()
            throws InterruptedException, ExecutionException, IOException {
        return getDeviceAuthorizationCodes((String) null);
    }

    /**
     * Requests a set of verification codes from the authorization server
     *
     * @see <a href="https://tools.ietf.org/html/rfc8628#section-3.1">RFC 8628</a>
     *
     * @param scope scope
     * @return DeviceAuthorization
     * @throws InterruptedException InterruptedException
     * @throws ExecutionException ExecutionException
     * @throws IOException IOException
     */
    public DeviceAuthorization getDeviceAuthorizationCodes(String scope)
            throws InterruptedException, ExecutionException, IOException {
        final OAuthRequest request = createDeviceAuthorizationCodesRequest(scope);

        try (Response response = execute(request)) {
            if (isDebug()) {
                log("got DeviceAuthorizationCodes response");
                log("response status code: %s", response.getCode());
                log("response body: %s", response.getBody());
            }
            return api.getDeviceAuthorizationExtractor().extract(response);
        }
    }

    public Future<DeviceAuthorization> getDeviceAuthorizationCodes(
            OAuthAsyncRequestCallback<DeviceAuthorization> callback) {
        return getDeviceAuthorizationCodes(null, callback);
    }

    public Future<DeviceAuthorization> getDeviceAuthorizationCodes(String scope,
            OAuthAsyncRequestCallback<DeviceAuthorization> callback) {
        final OAuthRequest request = createDeviceAuthorizationCodesRequest(scope);

        return execute(request, callback, new OAuthRequest.ResponseConverter<DeviceAuthorization>() {
            @Override
            public DeviceAuthorization convert(Response response) throws IOException {
                final DeviceAuthorization deviceAuthorization = api.getDeviceAuthorizationExtractor().extract(response);
                response.close();
                return deviceAuthorization;
            }
        });
    }

    public Future<DeviceAuthorization> getDeviceAuthorizationCodesAsync() {
        return getDeviceAuthorizationCodesAsync(null);
    }

    public Future<DeviceAuthorization> getDeviceAuthorizationCodesAsync(String scope) {
        return getDeviceAuthorizationCodes(scope, null);
    }

    protected OAuthRequest createAccessTokenDeviceAuthorizationGrantRequest(DeviceAuthorization deviceAuthorization) {
        final OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        request.addParameter(OAuthConstants.GRANT_TYPE, "urn:ietf:params:oauth:grant-type:device_code");
        request.addParameter("device_code", deviceAuthorization.getDeviceCode());
        api.getClientAuthentication().addClientAuthentication(request, getApiKey(), getApiSecret());
        return request;
    }

    /**
     * Attempts to get a token from a server.
     *
     * Function {@link #pollAccessTokenDeviceAuthorizationGrant(com.github.scribejava.core.model.DeviceAuthorization)}
     * is usually used instead of this.
     *
     * @param deviceAuthorization deviceAuthorization
     * @return token
     *
     * @throws java.lang.InterruptedException InterruptedException
     * @throws java.util.concurrent.ExecutionException ExecutionException
     * @throws java.io.IOException IOException
     * @throws OAuth2AccessTokenErrorResponse If {@link OAuth2AccessTokenErrorResponse#getError()} is
     * {@link com.github.scribejava.core.oauth2.OAuth2Error#AUTHORIZATION_PENDING} or
     * {@link com.github.scribejava.core.oauth2.OAuth2Error#SLOW_DOWN}, another attempt should be made after a while.
     *
     * @see #getDeviceAuthorizationCodes()
     */
    public OAuth2AccessToken getAccessTokenDeviceAuthorizationGrant(DeviceAuthorization deviceAuthorization)
            throws InterruptedException, ExecutionException, IOException {
        final OAuthRequest request = createAccessTokenDeviceAuthorizationGrantRequest(deviceAuthorization);

        try (Response response = execute(request)) {
            if (isDebug()) {
                log("got AccessTokenDeviceAuthorizationGrant response");
                log("response status code: %s", response.getCode());
                log("response body: %s", response.getBody());
            }
            return api.getAccessTokenExtractor().extract(response);
        }
    }

    public Future<OAuth2AccessToken> getAccessTokenDeviceAuthorizationGrant(DeviceAuthorization deviceAuthorization,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback) {
        final OAuthRequest request = createAccessTokenDeviceAuthorizationGrantRequest(deviceAuthorization);

        return execute(request, callback, new OAuthRequest.ResponseConverter<OAuth2AccessToken>() {
            @Override
            public OAuth2AccessToken convert(Response response) throws IOException {
                final OAuth2AccessToken accessToken = api.getAccessTokenExtractor().extract(response);
                response.close();
                return accessToken;
            }
        });
    }

    public Future<OAuth2AccessToken> getAccessTokenDeviceAuthorizationGrantAsync(
            DeviceAuthorization deviceAuthorization) {
        return getAccessTokenDeviceAuthorizationGrant(deviceAuthorization, null);
    }

    /**
     * Periodically tries to get a token from a server (waiting for the user to give consent). Sync only version. No
     * Async variants yet, one should implement async scenarios themselves.
     *
     * @param deviceAuthorization deviceAuthorization
     * @return token
     * @throws java.lang.InterruptedException InterruptedException
     * @throws java.util.concurrent.ExecutionException ExecutionException
     * @throws java.io.IOException IOException
     * @throws OAuth2AccessTokenErrorResponse Indicates OAuth error.
     *
     * @see #getDeviceAuthorizationCodes()
     */
    public OAuth2AccessToken pollAccessTokenDeviceAuthorizationGrant(DeviceAuthorization deviceAuthorization)
            throws InterruptedException, ExecutionException, IOException {
        long intervalMillis = deviceAuthorization.getIntervalSeconds() * 1000;
        while (true) {
            try {
                return getAccessTokenDeviceAuthorizationGrant(deviceAuthorization);
            } catch (OAuth2AccessTokenErrorResponse e) {
                if (e.getError() != OAuth2Error.AUTHORIZATION_PENDING) {
                    if (e.getError() == OAuth2Error.SLOW_DOWN) {
                        intervalMillis += 5000;
                    } else {
                        throw e;
                    }
                }
            }
            Thread.sleep(intervalMillis);
        }
    }

    private void logRequestWithParams(String requestDescription, OAuthRequest request) {
        if (isDebug()) {
            log("created " + requestDescription + " request with body params [%s], query string params [%s]",
                    request.getBodyParams().asFormUrlEncodedString(),
                    request.getQueryStringParams().asFormUrlEncodedString());
        }
    }
}
