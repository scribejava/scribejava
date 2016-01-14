package com.github.scribejava.core.oauth;

import com.ning.http.client.ProxyServer;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Future;
import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.AccessToken;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.OAuthRequestAsync;
import com.github.scribejava.core.model.RequestToken;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.services.Base64Encoder;
import com.github.scribejava.core.utils.MapUtils;

/**
 * OAuth 1.0a implementation of {@link OAuthService}
 *
 * @author Pablo Fernandez
 */
public class OAuth10aServiceImpl extends OAuthService {

    private static final String VERSION = "1.0";
    private final DefaultApi10a api;

    /**
     * Default constructor
     *
     * @param api OAuth1.0a api information
     * @param config OAuth 1.0a configuration param object
     */
    public OAuth10aServiceImpl(final DefaultApi10a api, final OAuthConfig config) {
        super(config);
        this.api = api;
    }

    @Override
    public RequestToken getRequestToken() {
        final OAuthConfig config = getConfig();
        config.log("obtaining request token from " + api.getRequestTokenEndpoint());
        final OAuthRequest request = new OAuthRequest(api.getRequestTokenVerb(), api.getRequestTokenEndpoint(), this);

        config.log("setting oauth_callback to " + config.getCallback());
        request.addOAuthParameter(OAuthConstants.CALLBACK, config.getCallback());
        addOAuthParams(request, "");
        appendSignature(request);

        config.log("sending request...");
        final Response response = request.send();
        final String body = response.getBody();

        config.log("response status code: " + response.getCode());
        config.log("response body: " + body);
        return api.getRequestTokenExtractor().extract(body);
    }

    private void addOAuthParams(final AbstractRequest request, String secret) {
        final OAuthConfig config = getConfig();
        request.addOAuthParameter(OAuthConstants.TIMESTAMP, api.getTimestampService().getTimestampInSeconds());
        request.addOAuthParameter(OAuthConstants.NONCE, api.getTimestampService().getNonce());
        request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, config.getApiKey());
        request.addOAuthParameter(OAuthConstants.SIGN_METHOD, api.getSignatureService().getSignatureMethod());
        request.addOAuthParameter(OAuthConstants.VERSION, getVersion());
        if (config.hasScope()) {
            request.addOAuthParameter(OAuthConstants.SCOPE, config.getScope());
        }
        request.addOAuthParameter(OAuthConstants.SIGNATURE, getSignature(request, secret));

        config.log("appended additional OAuth parameters: " + MapUtils.toString(request.getOauthParameters()));
    }

    @Override
    public AccessToken getAccessToken(final Token requestToken, final Verifier verifier) {
        if (requestToken instanceof RequestToken) {
            return getOAuth1AccessToken((RequestToken) requestToken, verifier);
        } else {
            throw new IllegalArgumentException("requestToken must be an instance of RequestToken");
        }
    }

    @Override
    public Future<AccessToken> getAccessTokenAsync(final Token requestToken, final Verifier verifier, final OAuthAsyncRequestCallback<AccessToken> callback) {
        if (requestToken instanceof RequestToken) {
            return getOAuth1AccessTokenAsync((RequestToken) requestToken, verifier, callback, null);
        } else {
            throw new IllegalArgumentException("requestToken must be an instance of RequestToken");
        }
    }

    @Override
    public Future<AccessToken> getAccessTokenAsync(final Token requestToken, final Verifier verifier, final OAuthAsyncRequestCallback<AccessToken> callback,
            final ProxyServer proxyServer) {
        if (requestToken instanceof RequestToken) {
            return getOAuth1AccessTokenAsync((RequestToken) requestToken, verifier, callback, proxyServer);
        } else {
            throw new IllegalArgumentException("requestToken must be an instance of RequestToken");
        }
    }

    @Override
    public OAuth1AccessToken getOAuth1AccessToken(RequestToken requestToken, Verifier verifier) {
        final OAuthConfig config = getConfig();
        config.log("obtaining access token from " + api.getAccessTokenEndpoint());
        final OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint(), this);
        prepareAccessTokenRequest(request, requestToken, verifier);
        final Response response = request.send();
        return (OAuth1AccessToken)api.getAccessTokenExtractor().extract(response.getBody());
    }
    
    /**
     * OAuth 2 APIs are not implemented and will throw an UnsupportedOperationException.
     * 
     * @return throws exception
     */
    @Override
    public OAuth2AccessToken getOAuth2AccessToken(Verifier verifier) {
        throw new UnsupportedOperationException("getOAuth2AccessToken is not supported for OAuth 1 APIs");
    }

    @Override
    public Future<AccessToken> getOAuth1AccessTokenAsync(RequestToken requestToken, Verifier verifier, OAuthAsyncRequestCallback<AccessToken> callback) {
        return getOAuth1AccessTokenAsync(requestToken, verifier, callback, null);
    }

    @Override
    public Future<AccessToken> getOAuth1AccessTokenAsync(RequestToken requestToken, Verifier verifier, OAuthAsyncRequestCallback<AccessToken> callback, ProxyServer proxyServer) {
        final OAuthConfig config = getConfig();
        config.log("async obtaining access token from " + api.getAccessTokenEndpoint());
        final OAuthRequestAsync request = new OAuthRequestAsync(api.getAccessTokenVerb(), api.getAccessTokenEndpoint(), this);
        prepareAccessTokenRequest(request, requestToken, verifier);
        return request.sendAsync(callback, new OAuthRequestAsync.ResponseConverter<AccessToken>() {
            @Override
            public AccessToken convert(final com.ning.http.client.Response response) throws IOException {
                return getApi().getAccessTokenExtractor().extract(OAuthRequestAsync.RESPONSE_CONVERTER.convert(response).getBody());
            }
        }, proxyServer);
    }
    
    /**
     * OAuth 2 APIs are not implemented and will throw an UnsupportedOperationException.
     * 
     * @return throws exception
     */
    @Override
    public Future<AccessToken> getOAuth2AccessTokenAsync(Verifier verifier, OAuthAsyncRequestCallback<AccessToken> callback) {
        throw new UnsupportedOperationException("getOAuth2AccessTokenAsync is not supported for OAuth 1 APIs");
    }

    /**
     * OAuth 2 APIs are not implemented and will throw an UnsupportedOperationException.
     * 
     * @return throws exception
     */
    @Override
    public Future<AccessToken> getOAuth2AccessTokenAsync(Verifier verifier, OAuthAsyncRequestCallback<AccessToken> callback, ProxyServer proxyServer) {
        throw new UnsupportedOperationException("getOAuth2AccessTokenAsync is not supported for OAuth 1 APIs");
    }

    private void prepareAccessTokenRequest(final AbstractRequest request, final RequestToken requestToken, final Verifier verifier) {
        final OAuthConfig config = getConfig();
        request.addOAuthParameter(OAuthConstants.TOKEN, requestToken.getToken());
        request.addOAuthParameter(OAuthConstants.VERIFIER, verifier.getValue());
        config.log("setting token to: " + requestToken + " and verifier to: " + verifier);
        addOAuthParams(request, requestToken.getSecret());
        appendSignature(request);
    }

    @Override
    public void signRequest(final Token token, final AbstractRequest request) {
        if (token instanceof OAuth1AccessToken) {
            signRequest((AccessToken) token, request);
        } else {
            throw new IllegalArgumentException("The access token must be an OAuth1AccessToken.");
        }

    }
    
    @Override
    public void signRequest(final AccessToken token, final AbstractRequest request) {
        final OAuthConfig config = getConfig();
        config.log("signing request: " + request.getCompleteUrl());

        // Do not append the token if empty. This is for two legged OAuth calls.
        if (!token.isEmpty()) {
            request.addOAuthParameter(OAuthConstants.TOKEN, token.getToken());
        }
        config.log("setting token to: " + token);
        addOAuthParams(request, ((OAuth1AccessToken) token).getSecret());
        appendSignature(request);

    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public String getAuthorizationUrl(final Token requestToken) {
        if (requestToken instanceof RequestToken) {
            return getOAuth1AuthorizationUrl((RequestToken)requestToken);
        } else  {
            throw new IllegalArgumentException("requestToken must be an instance of RequestToken");
        }
    }

    @Override
    public String getOAuth1AuthorizationUrl(final RequestToken requestToken) {
        return api.getAuthorizationUrl(requestToken);
    }

    /**
     * OAuth 2 APIs are not implemented and will throw an UnsupportedOperationException.
     * 
     * @return throws exception
     */
    @Override
    public String getOAuth2AuthorizationUrl() {
        throw new UnsupportedOperationException("OAuth 2 authorization urls are not supported for OAuth 1 APIs");
    }

    private String getSignature(final AbstractRequest request, String secret) {
        final OAuthConfig config = getConfig();
        config.log("generating signature...");
        config.log("using base64 encoder: " + Base64Encoder.type());
        final String baseString = api.getBaseStringExtractor().extract(request);
        final String signature = api.getSignatureService().getSignature(baseString, config.getApiSecret(), secret);

        config.log("base string is: " + baseString);
        config.log("signature is: " + signature);
        return signature;
    }

    private void appendSignature(final AbstractRequest request) {
        final OAuthConfig config = getConfig();
        switch (config.getSignatureType()) {
            case Header:
                config.log("using Http Header signature");

                final String oauthHeader = api.getHeaderExtractor().extract(request);
                request.addHeader(OAuthConstants.HEADER, oauthHeader);
                break;
            case QueryString:
                config.log("using Querystring signature");

                for (final Map.Entry<String, String> entry : request.getOauthParameters().entrySet()) {
                    request.addQuerystringParameter(entry.getKey(), entry.getValue());
                }
                break;
        }
    }

    public DefaultApi10a getApi() {
        return api;
    }
}
