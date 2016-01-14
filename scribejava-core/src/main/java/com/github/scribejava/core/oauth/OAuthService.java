package com.github.scribejava.core.oauth;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.ProxyServer;
import java.util.concurrent.Future;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.AccessToken;
import com.github.scribejava.core.model.ForceTypeOfHttpRequest;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConfigAsync;
import com.github.scribejava.core.model.RequestToken;
import com.github.scribejava.core.model.ScribeJavaConfig;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verifier;

/**
 * The main ScribeJava object.
 *
 * A facade responsible for the retrieval of request and access tokens and for the signing of HTTP requests.
 *
 * @author Pablo Fernandez
 */
public abstract class OAuthService {

    private final OAuthConfig config;
    private AsyncHttpClient asyncHttpClient;

    public OAuthService(final OAuthConfig config) {
        this.config = config;
        final ForceTypeOfHttpRequest forceTypeOfHttpRequest = ScribeJavaConfig.getForceTypeOfHttpRequests();
        if (config instanceof OAuthConfigAsync) {
            if (ForceTypeOfHttpRequest.FORCE_SYNC_ONLY_HTTP_REQUESTS == forceTypeOfHttpRequest) {
                throw new OAuthException("Cannot use async operations, only sync");
            }
            if (ForceTypeOfHttpRequest.PREFER_SYNC_ONLY_HTTP_REQUESTS == forceTypeOfHttpRequest) {
                config.log("Cannot use async operations, only sync");
            }
            final OAuthConfigAsync asyncConfig = ((OAuthConfigAsync) config);
            final String asyncHttpProviderClassName = asyncConfig.getAsyncHttpProviderClassName();

            asyncHttpClient = asyncHttpProviderClassName == null ? new AsyncHttpClient(asyncConfig.getAsyncHttpClientConfig())
                    : new AsyncHttpClient(asyncHttpProviderClassName, asyncConfig.getAsyncHttpClientConfig());
        } else {
            if (ForceTypeOfHttpRequest.FORCE_ASYNC_ONLY_HTTP_REQUESTS == forceTypeOfHttpRequest) {
                throw new OAuthException("Cannot use sync operations, only async");
            }
            if (ForceTypeOfHttpRequest.PREFER_ASYNC_ONLY_HTTP_REQUESTS == forceTypeOfHttpRequest) {
                config.log("Cannot use sync operations, only async");
            }
        }
    }

    public AsyncHttpClient getAsyncHttpClient() {
        return asyncHttpClient;
    }

    public void closeAsyncClient() {
        asyncHttpClient.close();
    }

    public OAuthConfig getConfig() {
        return config;
    }

    /**
     * Retrieve the request token for an OAuth1 API.
     *
     * @return request token
     */
    public abstract RequestToken getRequestToken();
    
    /**
     * Fetches an access token, delegating to either the OAuth1 or OAuth2 versions
     * of this method. OAuth2 APIs do not require the requestToken parameter, but
     * this method signature is preserved for reverse compatibility.
     * 
     * @see #getOAuth1AccessToken(com.github.scribejava.core.model.RequestToken, com.github.scribejava.core.model.Verifier) 
     * @see #getOAuth2AccessToken(com.github.scribejava.core.model.Verifier) 
     * 
     * @param requestToken OAuth1 request token or null for OAuth 2 APIs
     * @param verifier verifier code
     * @return {@link com.github.scribejava.core.model.OAuth1AccessToken} or {@link com.github.scribejava.core.model.OAuth2AccessToken} access token
     */
    public abstract AccessToken getAccessToken(Token requestToken, Verifier verifier);
    
    /**
     * Retrieves an OAuth1 access token using the previously retrieved requestToken
     * and the verifier.
     * 
     * @param requestToken OAuth1 request token
     * @param verifier verifier code
     * @return {@link com.github.scribejava.core.model.OAuth1AccessToken}
     */
    public abstract AccessToken getOAuth1AccessToken(RequestToken requestToken, Verifier verifier);
    
    /**
     * Retrieves an OAuth2 access token from the verifier.
     * 
     * @param verifier verifier code
     * @return {@link com.github.scribejava.core.model.OAuth2AccessToken}
     */
    public abstract AccessToken getOAuth2AccessToken(Verifier verifier);

    /**
     * Signs an OAuth request. Token should be an AccessToken, but this method
     * signature is preserved for reverse compatibility.
     *
     * @param accessToken access token (obtained previously)
     * @param request request to sign
     */
    public abstract void signRequest(Token accessToken, AbstractRequest request);
    
    /**
     * Signs an OAuth request. 
     *
     * @param accessToken access token (obtained previously)
     * @param request request to sign
     */
    public abstract void signRequest(AccessToken accessToken, AbstractRequest request);

    /**
     * Start a request to retrieve the access token. The optionally provided callback will be called with the Token when it is available. 
     * This method delegates to either the OAuth1 or OAuth2 versions
     * of this method. OAuth2 APIs do not require the requestToken parameter, but
     * this method signature is preserved for reverse compatibility.
     * 
     * @see #getOAuth1AccessTokenAsync(com.github.scribejava.core.model.RequestToken, com.github.scribejava.core.model.Verifier, com.github.scribejava.core.model.OAuthAsyncRequestCallback) 
     * @see #getOAuth2AccessTokenAsync(com.github.scribejava.core.model.Verifier, com.github.scribejava.core.model.OAuthAsyncRequestCallback) 
     *
     * @param requestToken request token (obtained previously or null)
     * @param verifier verifier code
     * @param callback optional callback
     * @return Future
     */
    public abstract Future<AccessToken> getAccessTokenAsync(Token requestToken, Verifier verifier, OAuthAsyncRequestCallback<AccessToken> callback);
    public abstract Future<AccessToken> getAccessTokenAsync(Token requestToken, Verifier verifier, OAuthAsyncRequestCallback<AccessToken> callback,
            ProxyServer proxyServer);
    

    /**
     * Start a request to retrieve an OAuth1 access token.
     * 
     * @param requestToken OAuth1 request token
     * @param verifier verifier code
     * @param callback optional callback
     * @return 
     */
    public abstract Future<AccessToken> getOAuth1AccessTokenAsync(RequestToken requestToken, Verifier verifier, OAuthAsyncRequestCallback<AccessToken> callback);

    public abstract Future<AccessToken> getOAuth1AccessTokenAsync(RequestToken requestToken, Verifier verifier, OAuthAsyncRequestCallback<AccessToken> callback,
            ProxyServer proxyServer);
    
    /**
     * Start a request to retrieve an OAuth2 access token.
     * 
     * @param verifier verifier code
     * @param callback optional callback
     * @return 
     */
    public abstract Future<AccessToken> getOAuth2AccessTokenAsync(Verifier verifier, OAuthAsyncRequestCallback<AccessToken> callback);

    public abstract Future<AccessToken> getOAuth2AccessTokenAsync(Verifier verifier, OAuthAsyncRequestCallback<AccessToken> callback,
            ProxyServer proxyServer);

    /**
     * Returns the OAuth version of the service.
     *
     * @return oauth version as string
     */
    public abstract String getVersion();

    /**
     * Returns the URL where you should redirect your users to authenticate your application. Delegates to either
     * the OAuth1 or OAuth2 versions of this method. OAuth2 APIs do not require a requestToken to generate
     * an authorization url.
     * 
     * @see #getOAuth1AuthorizationUrl(com.github.scribejava.core.model.RequestToken) 
     * @see #getOAuth2AuthorizationUrl()
     *
     * @param requestToken the request token you need to authorize
     * @return the URL where you should redirect your users
     */
    public abstract String getAuthorizationUrl(Token requestToken);
    
    /**
     * Returns the URL where you should redirect your users to authenticate your application for
     * an OAuth1 API.
     * 
     * @param requestToken the request token you need to authorize
     * @return URL string
     */
    public abstract String getOAuth1AuthorizationUrl(RequestToken requestToken);
    
    /**
     * Returns the URL where you should redirect your users to authenticate your application for
     * an OAuth2 API.
     * 
     * @return URL string
     */
    public abstract String getOAuth2AuthorizationUrl();

}
