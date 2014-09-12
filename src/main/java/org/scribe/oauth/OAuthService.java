package org.scribe.oauth;

import com.ning.http.client.AsyncHttpClient;
import java.util.concurrent.Future;
import org.scribe.model.AbstractRequest;
import org.scribe.model.OAuthAsyncRequestCallback;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Token;
import org.scribe.model.Verifier;

/**
 * The main SubScribe object.
 *
 * A facade responsible for the retrieval of request and access tokens and for the signing of HTTP requests.
 *
 * @author Pablo Fernandez
 */
public interface OAuthService {

    /**
     * Retrieve the request token.
     *
     * @return request token
     */
    public Token getRequestToken();

    public Token getAccessToken(Token requestToken, Verifier verifier);

    /**
     * Signs am OAuth request
     *
     * @param accessToken access token (obtained previously)
     * @param request request to sign
     */
    public void signRequest(Token accessToken, AbstractRequest request);

    /**
     * Start the request to retrieve the access token. The optionally provided callback will be called with the Token when it is available.
     *
     * @param requestToken request token (obtained previously or null)
     * @param verifier verifier code
     * @param callback optional callback
     * @return Future
     */
    public Future<Token> getAccessTokenAsync(Token requestToken, Verifier verifier, OAuthAsyncRequestCallback<Token> callback);

    /**
     * Returns the OAuth version of the service.
     *
     * @return oauth version as string
     */
    public String getVersion();

    /**
     * Returns the URL where you should redirect your users to authenticate your application.
     *
     * @param requestToken the request token you need to authorize
     * @return the URL where you should redirect your users
     */
    public String getAuthorizationUrl(Token requestToken);

    public OAuthConfig getConfig();

    public AsyncHttpClient getAsyncHttpClient();

    public void closeAsyncClient();

}
