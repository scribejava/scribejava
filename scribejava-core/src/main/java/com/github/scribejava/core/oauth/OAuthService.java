package com.github.scribejava.core.oauth;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.ProxyServer;
import java.util.concurrent.Future;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.ForceTypeOfHttpRequest;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConfigAsync;
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
     * Retrieve the request token.
     *
     * @return request token
     */
    public abstract Token getRequestToken();

    public abstract Token getAccessToken(Token requestToken, Verifier verifier);

    /**
     * Signs am OAuth request
     *
     * @param accessToken access token (obtained previously)
     * @param request request to sign
     */
    public abstract void signRequest(Token accessToken, AbstractRequest request);

    /**
     * Start the request to retrieve the access token. The optionally provided callback will be called with the Token when it is available.
     *
     * @param requestToken request token (obtained previously or null)
     * @param verifier verifier code
     * @param callback optional callback
     * @return Future
     */
    public abstract Future<Token> getAccessTokenAsync(Token requestToken, Verifier verifier, OAuthAsyncRequestCallback<Token> callback);

    public abstract Future<Token> getAccessTokenAsync(Token requestToken, Verifier verifier, OAuthAsyncRequestCallback<Token> callback,
            ProxyServer proxyServer);

    /**
     * Returns the OAuth version of the service.
     *
     * @return oauth version as string
     */
    public abstract String getVersion();

    /**
     * Returns the URL where you should redirect your users to authenticate your application.
     *
     * @param requestToken the request token you need to authorize
     * @return the URL where you should redirect your users
     */
    public abstract String getAuthorizationUrl(Token requestToken);

}
