package com.github.scribejava.core.oauth;

import com.ning.http.client.ProxyServer;
import java.io.IOException;
import java.util.concurrent.Future;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.OAuthRequestAsync;
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
    public Token getAccessToken(final Token requestToken, final Verifier verifier) {
        final Response response = createAccessTokenRequest(verifier, new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint(), this)).
                send();
        return api.getAccessTokenExtractor().extract(response.getBody());
    }

    @Override
    public Future<Token> getAccessTokenAsync(final Token requestToken, final Verifier verifier, final OAuthAsyncRequestCallback<Token> callback) {
        return getAccessTokenAsync(requestToken, verifier, callback, null);
    }

    @Override
    public Future<Token> getAccessTokenAsync(final Token requestToken, final Verifier verifier, final OAuthAsyncRequestCallback<Token> callback,
            final ProxyServer proxyServer) {
        final OAuthRequestAsync request = createAccessTokenRequest(verifier, new OAuthRequestAsync(api.getAccessTokenVerb(), api.
                getAccessTokenEndpoint(),
                this));
        return request.sendAsync(callback, new OAuthRequestAsync.ResponseConverter<Token>() {
            @Override
            public Token convert(final com.ning.http.client.Response response) throws IOException {
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
    public Token getRequestToken() {
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
    public void signRequest(final Token accessToken, final AbstractRequest request) {
        request.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN, accessToken.getToken());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuthorizationUrl(final Token requestToken) {
        return api.getAuthorizationUrl(getConfig());
    }

    public DefaultApi20 getApi() {
        return api;
    }
}
