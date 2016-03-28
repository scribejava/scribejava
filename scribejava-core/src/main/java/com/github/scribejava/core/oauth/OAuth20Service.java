package com.github.scribejava.core.oauth;

import com.github.scribejava.core.services.Base64Encoder;
import com.ning.http.client.ProxyServer;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.Future;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuth2Authorization;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.OAuthRequestAsync;
import java.util.Map;

public class OAuth20Service extends OAuthService {

    private static final String VERSION = "2.0";
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

    //sync version, protected to facilitate mocking
    protected OAuth2AccessToken sendAccessTokenRequestSync(OAuthRequest request) {
        return api.getAccessTokenExtractor().extract(request.send().getBody());
    }

    //async version, protected to facilitate mocking
    protected Future<OAuth2AccessToken> sendAccessTokenRequestAsync(OAuthRequestAsync request,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback, ProxyServer proxyServer) {

        return request.sendAsync(callback, new OAuthRequestAsync.ResponseConverter<OAuth2AccessToken>() {
            @Override
            public OAuth2AccessToken convert(com.ning.http.client.Response response) throws IOException {
                return getApi().getAccessTokenExtractor()
                        .extract(OAuthRequestAsync.RESPONSE_CONVERTER.convert(response).getBody());
            }
        }, proxyServer);
    }

    public final OAuth2AccessToken getAccessToken(String code) {
        final OAuthRequest request = createAccessTokenRequest(code,
                new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint(), this));

        return sendAccessTokenRequestSync(request);
    }

    /**
     * Start the request to retrieve the access token. The optionally provided callback will be called with the Token
     * when it is available.
     *
     * @param code code
     * @param callback optional callback
     * @return Future
     */
    public final Future<OAuth2AccessToken> getAccessTokenAsync(String code,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback) {
        return getAccessTokenAsync(code, callback, null);
    }

    public final Future<OAuth2AccessToken> getAccessTokenAsync(String code,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback, ProxyServer proxyServer) {
        final OAuthRequestAsync request = createAccessTokenRequest(code,
                new OAuthRequestAsync(api.getAccessTokenVerb(), api.getAccessTokenEndpoint(), this));

        return sendAccessTokenRequestAsync(request, callback, proxyServer);
    }

    protected <T extends AbstractRequest> T createAccessTokenRequest(String code, T request) {
        final OAuthConfig config = getConfig();
        request.addParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        request.addParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
        request.addParameter(OAuthConstants.CODE, code);
        request.addParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
        if (config.hasScope()) {
            request.addParameter(OAuthConstants.SCOPE, config.getScope());
        }
        if (config.hasGrantType()) {
            request.addParameter(OAuthConstants.GRANT_TYPE, config.getGrantType());
        }
        return request;
    }

    public final OAuth2AccessToken refreshAccessToken(String refreshToken) {
        final OAuthRequest request = createRefreshTokenRequest(refreshToken,
                new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint(), this));

        return sendAccessTokenRequestSync(request);
    }

    public final Future<OAuth2AccessToken> refreshAccessTokenAsync(String refreshToken,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback) {
        return refreshAccessTokenAsync(refreshToken, callback, null);
    }

    public final Future<OAuth2AccessToken> refreshAccessTokenAsync(String refreshToken,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback, ProxyServer proxyServer) {
        final OAuthRequestAsync request = createRefreshTokenRequest(refreshToken,
                new OAuthRequestAsync(api.getAccessTokenVerb(), api.getRefreshTokenEndpoint(), this));

        return sendAccessTokenRequestAsync(request, callback, proxyServer);
    }

    protected <T extends AbstractRequest> T createRefreshTokenRequest(String refreshToken, T request) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new IllegalArgumentException("The refreshToken cannot be null or empty");
        }
        final OAuthConfig config = getConfig();
        request.addParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        request.addParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
        request.addParameter(OAuthConstants.REFRESH_TOKEN, refreshToken);
        request.addParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.REFRESH_TOKEN);
        return request;
    }

    /**
     * Request Access Token Password Grant sync version
     *
     * @param uname User name
     * @param password User password
     * @return OAuth2AccessToken
     */
    public final OAuth2AccessToken getAccessTokenPasswordGrant(String uname, String password) {
        final OAuthRequest request = createAccessTokenPasswordGrantRequest(uname, password,
                new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint(), this));

        return sendAccessTokenRequestSync(request);
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
        return getAccessTokenPasswordGrantAsync(uname, password, callback, null);
    }

    public final Future<OAuth2AccessToken> getAccessTokenPasswordGrantAsync(String uname, String password,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback, ProxyServer proxyServer) {
        final OAuthRequestAsync request = createAccessTokenPasswordGrantRequest(uname, password,
                new OAuthRequestAsync(api.getAccessTokenVerb(), api.getAccessTokenEndpoint(), this));

        return sendAccessTokenRequestAsync(request, callback, proxyServer);
    }

    protected <T extends AbstractRequest> T createAccessTokenPasswordGrantRequest(String username, String password,
            T request) {
        final OAuthConfig config = getConfig();
        request.addParameter(OAuthConstants.USERNAME, username);
        request.addParameter(OAuthConstants.PASSWORD, password);

        if (config.hasScope()) {
            request.addParameter(OAuthConstants.SCOPE, config.getScope());
        }

        request.addParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.PASSWORD);

        request.addHeader(OAuthConstants.HEADER, OAuthConstants.BASIC + " "
                + Base64Encoder.getInstance().encode(
                        String.format("%s:%s", config.getApiKey(), config.getApiSecret()).getBytes(
                        Charset.forName("UTF-8")
                )
                )
        );

        return request;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVersion() {
        return VERSION;
    }

    public void signRequest(OAuth2AccessToken accessToken, AbstractRequest request) {
        request.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN, accessToken.getAccessToken());
    }

    /**
     * Returns the URL where you should redirect your users to authenticate your application.
     *
     * @return the URL where you should redirect your users
     */
    public final String getAuthorizationUrl() {
        return getAuthorizationUrl(null);
    }

    /**
     * Returns the URL where you should redirect your users to authenticate your application.
     *
     * @param additionalParams any additional GET params to add to the URL
     * @return the URL where you should redirect your users
     */
    public String getAuthorizationUrl(Map<String, String> additionalParams) {
        return api.getAuthorizationUrl(getConfig(), additionalParams);
    }

    public DefaultApi20 getApi() {
        return api;
    }

    public OAuth2Authorization extractAuthorization(String redirectLocation) {
        final OAuth2Authorization authorization = new OAuth2Authorization();
        for (String param : redirectLocation.substring(redirectLocation.indexOf('?') + 1).split("&")) {
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
