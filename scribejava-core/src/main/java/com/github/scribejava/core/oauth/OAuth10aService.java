package com.github.scribejava.core.oauth;

import java.io.IOException;
import java.util.concurrent.Future;
import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.builder.api.OAuth1SignatureType;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import java.util.concurrent.ExecutionException;

/**
 * OAuth 1.0a implementation of {@link OAuthService}
 */
public class OAuth10aService extends OAuthService {

    private static final String VERSION = "1.0";
    private final DefaultApi10a api;

    /**
     * Default constructor
     *
     * @param api OAuth1.0a api information
     * @param config OAuth 1.0a configuration param object
     */
    public OAuth10aService(DefaultApi10a api, OAuthConfig config) {
        super(config);
        this.api = api;
    }

    public final OAuth1RequestToken getRequestToken() throws IOException, InterruptedException, ExecutionException {
        final OAuthConfig config = getConfig();
        config.log("obtaining request token from " + api.getRequestTokenEndpoint());
        final OAuthRequest request = prepareRequestTokenRequest();

        config.log("sending request...");
        final Response response = execute(request);
        final String body = response.getBody();

        config.log("response status code: " + response.getCode());
        config.log("response body: " + body);
        return api.getRequestTokenExtractor().extract(response);
    }

    public final Future<OAuth1RequestToken> getRequestTokenAsync() {
        return getRequestTokenAsync(null);
    }

    public final Future<OAuth1RequestToken> getRequestTokenAsync(
            OAuthAsyncRequestCallback<OAuth1RequestToken> callback) {
        final OAuthConfig config = getConfig();
        config.log("async obtaining request token from " + api.getRequestTokenEndpoint());
        final OAuthRequest request = prepareRequestTokenRequest();
        return execute(request, callback, response -> getApi().getRequestTokenExtractor().extract(response));
    }

    protected OAuthRequest prepareRequestTokenRequest() {
        final OAuthRequest request = new OAuthRequest(api.getRequestTokenVerb(), api.getRequestTokenEndpoint());
        final OAuthConfig config = getConfig();
        config.log("setting oauth_callback to " + config.getCallback());
        request.addOAuthParameter(OAuthConstants.CALLBACK, config.getCallback());
        addOAuthParams(request, "");
        appendSignature(request);
        return request;
    }

    protected void addOAuthParams(OAuthRequest request, String tokenSecret) {
        final OAuthConfig config = getConfig();
        request.addOAuthParameter(OAuthConstants.TIMESTAMP, api.getTimestampService().getTimestampInSeconds());
        request.addOAuthParameter(OAuthConstants.NONCE, api.getTimestampService().getNonce());
        request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, config.getApiKey());
        request.addOAuthParameter(OAuthConstants.SIGN_METHOD, api.getSignatureService().getSignatureMethod());
        request.addOAuthParameter(OAuthConstants.VERSION, getVersion());
        final String scope = config.getScope();
        if (scope != null) {
            request.addOAuthParameter(OAuthConstants.SCOPE, scope);
        }
        request.addOAuthParameter(OAuthConstants.SIGNATURE, getSignature(request, tokenSecret));

        config.log("appended additional OAuth parameters: " + request.getOauthParameters());
    }

    public final OAuth1AccessToken getAccessToken(OAuth1RequestToken requestToken, String oauthVerifier)
            throws IOException, InterruptedException, ExecutionException {
        getConfig().log("obtaining access token from " + api.getAccessTokenEndpoint());
        final OAuthRequest request = prepareAccessTokenRequest(requestToken, oauthVerifier);
        final Response response = execute(request);
        return api.getAccessTokenExtractor().extract(response);
    }

    public final Future<OAuth1AccessToken> getAccessTokenAsync(OAuth1RequestToken requestToken, String oauthVerifier) {
        return getAccessTokenAsync(requestToken, oauthVerifier, null);
    }

    /**
     * Start the request to retrieve the access token. The optionally provided
     * callback will be called with the Token when it is available.
     *
     * @param requestToken request token (obtained previously or null)
     * @param oauthVerifier oauth_verifier
     * @param callback optional callback
     * @return Future
     */
    public final Future<OAuth1AccessToken> getAccessTokenAsync(OAuth1RequestToken requestToken, String oauthVerifier,
            OAuthAsyncRequestCallback<OAuth1AccessToken> callback) {
        final OAuthConfig config = getConfig();
        config.log("async obtaining access token from " + api.getAccessTokenEndpoint());
        final OAuthRequest request = prepareAccessTokenRequest(requestToken, oauthVerifier);
        return execute(request, callback, response -> getApi().getAccessTokenExtractor().extract(response));
    }

    protected OAuthRequest prepareAccessTokenRequest(OAuth1RequestToken requestToken, String oauthVerifier) {
        final OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        final OAuthConfig config = getConfig();
        request.addOAuthParameter(OAuthConstants.TOKEN, requestToken.getToken());
        request.addOAuthParameter(OAuthConstants.VERIFIER, oauthVerifier);
        config.log("setting token to: " + requestToken + " and verifier to: " + oauthVerifier);
        addOAuthParams(request, requestToken.getTokenSecret());
        appendSignature(request);
        return request;
    }

    public void signRequest(OAuth1AccessToken token, OAuthRequest request) {
        final OAuthConfig config = getConfig();
        config.log("signing request: " + request.getCompleteUrl());

        if (!token.isEmpty() || api.isEmptyOAuthTokenParamIsRequired()) {
            request.addOAuthParameter(OAuthConstants.TOKEN, token.getToken());
        }
        config.log("setting token to: " + token);
        addOAuthParams(request, token.getTokenSecret());
        appendSignature(request);
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    /**
     * Returns the URL where you should redirect your users to authenticate your
     * application.
     *
     * @param requestToken the request token you need to authorize
     * @return the URL where you should redirect your users
     */
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return api.getAuthorizationUrl(requestToken);
    }

    private String getSignature(OAuthRequest request, String tokenSecret) {
        final OAuthConfig config = getConfig();
        config.log("generating signature...");
        final String baseString = api.getBaseStringExtractor().extract(request);
        final String signature = api.getSignatureService().getSignature(baseString, config.getApiSecret(), tokenSecret);

        config.log("base string is: " + baseString);
        config.log("signature is: " + signature);
        return signature;
    }

    protected void appendSignature(OAuthRequest request) {
        final OAuthConfig config = getConfig();
        final OAuth1SignatureType signatureType = api.getSignatureType();
        switch (signatureType) {
            case Header:
                config.log("using Http Header signature");

                final String oauthHeader = api.getHeaderExtractor().extract(request);
                request.addHeader(OAuthConstants.HEADER, oauthHeader);
                break;
            case QueryString:
                config.log("using Querystring signature");

                request.getOauthParameters().forEach((key, value) -> request.addQuerystringParameter(key, value));
                break;
            default:
                throw new IllegalStateException("Unknown new Signature Type '" + signatureType + "'.");
        }
    }

    public DefaultApi10a getApi() {
        return api;
    }
}
