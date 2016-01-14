package com.github.scribejava.core.oauth;

import com.ning.http.client.ProxyServer;
import java.io.IOException;
import java.util.concurrent.Future;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.AccessToken;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.OAuthRequestAsync;
import com.github.scribejava.core.model.RequestToken;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verifier;

public class OAuth20ServiceImpl extends OAuthService {

    private static final String VERSION = "2.0";
    private final DefaultApi20 api;

    /**
     * Default constructor
     *
     * @param api OAuth2.0 api information
     * @param config OAuth 2.0 configuration param object
     */
    public OAuth20ServiceImpl(final DefaultApi20 api, final OAuthConfig config) {
        super(config);
        this.api = api;
    }

    @Override
    public AccessToken getAccessToken(final Token requestToken, final Verifier verifier) {
        return getOAuth2AccessToken(verifier);
    }

    @Override
    public AccessToken getOAuth2AccessToken(final Verifier verifier) {
        final Response response = createAccessTokenRequest(verifier, new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint(), this)).
                send();
        return api.getAccessTokenExtractor().extract(response.getBody());
    }

    @Override
    public AccessToken getOAuth1AccessToken(final RequestToken requestToken, final Verifier verifier) {
        throw new UnsupportedOperationException("getOAuth1AccessToken not supported for OAuth 2 APIs");
    }

    @Override
    public Future<AccessToken> getOAuth1AccessTokenAsync(final RequestToken requestToken, final Verifier verifier, final OAuthAsyncRequestCallback<AccessToken> callback) {
        throw new UnsupportedOperationException("getOAuth1AccessTokenAsync not supported for OAuth 2 APIs");
    }

    @Override
    public Future<AccessToken> getAccessTokenAsync(final Token requestToken, final Verifier verifier, final OAuthAsyncRequestCallback<AccessToken> callback,
            final ProxyServer proxyServer) {
        return getOAuth2AccessTokenAsync(verifier, callback, proxyServer);
    }

    @Override
    public Future<AccessToken> getAccessTokenAsync(Token requestToken, Verifier verifier, OAuthAsyncRequestCallback<AccessToken> callback) {
        return getAccessTokenAsync(requestToken, verifier, callback, null);
    }

    @Override
    public Future<AccessToken> getOAuth1AccessTokenAsync(RequestToken requestToken, Verifier verifier, OAuthAsyncRequestCallback<AccessToken> callback, ProxyServer proxyServer) {
        throw new UnsupportedOperationException("getOAuth1AccessTokenAsync not supported for OAuth 2 APIs");
    }

    @Override
    public Future<AccessToken> getOAuth2AccessTokenAsync(Verifier verifier, OAuthAsyncRequestCallback<AccessToken> callback) {
        return getOAuth2AccessTokenAsync(verifier, callback, null);
    }

    @Override
    public Future<AccessToken> getOAuth2AccessTokenAsync(Verifier verifier, OAuthAsyncRequestCallback<AccessToken> callback, ProxyServer proxyServer) {
        final OAuthRequestAsync request = createAccessTokenRequest(verifier, new OAuthRequestAsync(api.getAccessTokenVerb(), api.
                getAccessTokenEndpoint(),
                this));
        return request.sendAsync(callback, new OAuthRequestAsync.ResponseConverter<AccessToken>() {
            @Override
            public AccessToken convert(final com.ning.http.client.Response response) throws IOException {
                return getApi().getAccessTokenExtractor().extract(OAuthRequestAsync.RESPONSE_CONVERTER.convert(response).getBody());
            }
        }, proxyServer);
    }

    protected <T extends AbstractRequest> T createAccessTokenRequest(final Verifier verifier, final T request) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public RequestToken getRequestToken() {
        throw new UnsupportedOperationException("Unsupported operation, please use 'getAuthorizationUrl' and redirect your users there");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVersion() {
        return VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void signRequest(final AccessToken accessToken, final AbstractRequest request) {
        request.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN, accessToken.getToken());
    }
    
        /**
     * {@inheritDoc}
     */
    @Override
    public void signRequest(final Token accessToken, final AbstractRequest request) {
        if (accessToken instanceof AccessToken) {
            signRequest((AccessToken)accessToken,request);
        } else {
            throw new IllegalArgumentException("accessToken must be an instance of AccessToken");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuthorizationUrl(final RequestToken requestToken) {
        return api.getAuthorizationUrl(getConfig());
    }

    @Override
    public String getAuthorizationUrl(final Token requestToken) {
        if (requestToken instanceof RequestToken) {
            return api.getAuthorizationUrl(getConfig());
        } else {
            throw new IllegalArgumentException("requestToken must be an instance of RequestToken");
        }
    }

    public DefaultApi20 getApi() {
        return api;
    }
}
