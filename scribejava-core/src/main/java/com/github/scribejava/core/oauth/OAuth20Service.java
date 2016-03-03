package com.github.scribejava.core.oauth;

import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.services.Base64Encoder;
import com.github.scribejava.core.utils.Uninterruptibles;
import com.ning.http.client.ProxyServer;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.OAuthRequestAsync;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verifier;
import org.apache.commons.codec.binary.Base64;

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

    public final OAuth2AccessToken getAccessToken(Verifier verifier) {
        return Uninterruptibles.getUninterruptibly( getAccessTokenAsync(verifier, null) );
    }

    public final OAuth2AccessToken getAccessTokenPasswordGrant(String uname, String password) {
        return Uninterruptibles.getUninterruptibly( getAccessTokenPasswordGrantAsync(uname, password, null) );
    }

    /**
     * Start the request to retrieve the access token. The optionally provided callback will be called with the Token
     * when it is available.
     *
     * @param verifier verifier code
     * @param callback optional callback
     * @return Future
     */
    public final Future<OAuth2AccessToken> getAccessTokenAsync(Verifier verifier,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback) {
        return getAccessTokenAsync(verifier, callback, null);
    }

    public final Future<OAuth2AccessToken> getAccessTokenAsync(Verifier verifier,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback, ProxyServer proxyServer) {
        final OAuthRequestAsync request = createAccessTokenRequest(verifier,
                new OAuthRequestAsync(api.getAccessTokenVerb(), api.getAccessTokenEndpoint(), this));

        return getAccessTokenAsync(request, callback, proxyServer);
    }

    public final Future<OAuth2AccessToken> getAccessTokenPasswordGrantAsync(String uname, String password,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback) {
        final OAuthRequestAsync request = createAccessTokenPasswordGrantRequest(uname, password,
          new OAuthRequestAsync(api.getAccessTokenVerb(), api.getAccessTokenEndpoint(), this));

        return getAccessTokenPasswordGrantAsync(uname, password, callback, null);
    }

    public final Future<OAuth2AccessToken> getAccessTokenPasswordGrantAsync(String uname, String password,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback, ProxyServer proxyServer) {
        final OAuthRequestAsync request = createAccessTokenPasswordGrantRequest(uname, password,
          new OAuthRequestAsync(api.getAccessTokenVerb(), api.getAccessTokenEndpoint(), this));

        return getAccessTokenAsync(request, callback, proxyServer);
    }

    protected Future<OAuth2AccessToken> getAccessTokenAsync(OAuthRequestAsync request,
        OAuthAsyncRequestCallback<OAuth2AccessToken> callback, ProxyServer proxyServer) {

        return request.sendAsync(callback, new OAuthRequestAsync.ResponseConverter<OAuth2AccessToken>() {
            @Override
            public OAuth2AccessToken convert(com.ning.http.client.Response response) throws IOException {
                return getApi().getAccessTokenExtractor()
                  .extract(OAuthRequestAsync.RESPONSE_CONVERTER.convert(response).getBody());
            }
        }, proxyServer);
    }

    protected <T extends AbstractRequest> T createAccessTokenPasswordGrantRequest(String username, String password, T request) {
        final OAuthConfig config = getConfig();
        request.addParameter(OAuthConstants.USERNAME, username);
        request.addParameter(OAuthConstants.PASSWORD, password);

        if (config.hasScope()) {
            request.addParameter(OAuthConstants.SCOPE, config.getScope());
        }

        request.addParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.PASSWORD);

        request.addHeader(OAuthConstants.HEADER, OAuthConstants.BASIC + " " +
            Base64Encoder.getInstance().encode(
                String.format("%s:%s", config.getApiKey(), config.getApiSecret()).getBytes(
                  Charset.forName("UTF-8")
                )
            )
        );

        return request;
    }

    protected <T extends AbstractRequest> T createAccessTokenRequest(Verifier verifier, T request) {
        final OAuthConfig config = getConfig();
        request.addParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        request.addParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
        request.addParameter(OAuthConstants.CODE, verifier.getValue());
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
        return Uninterruptibles.getUninterruptibly( refreshAccessTokenAsync(refreshToken, null) );
    }

    public final Future<OAuth2AccessToken> refreshAccessTokenAsync(String refreshToken,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback) {
        return refreshAccessTokenAsync(refreshToken, callback, null);
    }

    public final Future<OAuth2AccessToken> refreshAccessTokenAsync(String refreshToken,
            OAuthAsyncRequestCallback<OAuth2AccessToken> callback, ProxyServer proxyServer) {
        final OAuthRequestAsync request = createRefreshTokenRequest(refreshToken,
                new OAuthRequestAsync(api.getAccessTokenVerb(), api.getAccessTokenEndpoint(), this));
        return request.sendAsync(callback, new OAuthRequestAsync.ResponseConverter<OAuth2AccessToken>() {
            @Override
            public OAuth2AccessToken convert(com.ning.http.client.Response response) throws IOException {
                return getApi().getAccessTokenExtractor()
                        .extract(OAuthRequestAsync.RESPONSE_CONVERTER.convert(response).getBody());
            }
        }, proxyServer);
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
}
