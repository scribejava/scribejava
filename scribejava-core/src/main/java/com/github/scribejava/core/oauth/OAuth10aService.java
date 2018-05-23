package com.github.scribejava.core.oauth;

import java.io.IOException;
import java.util.concurrent.Future;
import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.builder.api.OAuth1SignatureType;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * OAuth 1.0a implementation of {@link OAuthService}
 */
public class OAuth10aService extends OAuthService {

    private static final String VERSION = "1.0";
    private final OutputStream debugStream;
    private final DefaultApi10a api;

    public OAuth10aService(DefaultApi10a api, String apiKey, String apiSecret, String callback, String scope,
            OutputStream debugStream, String userAgent, HttpClientConfig httpClientConfig, HttpClient httpClient) {
        super(apiKey, apiSecret, callback, scope, userAgent, httpClientConfig, httpClient);
        this.debugStream = debugStream;
        this.api = api;
    }

    public OAuth1RequestToken getRequestToken() throws IOException, InterruptedException, ExecutionException {
        log("obtaining request token from %s", api.getRequestTokenEndpoint());
        final OAuthRequest request = prepareRequestTokenRequest();

        log("sending request...");
        final Response response = execute(request);
        final String body = response.getBody();

        log("response status code: %s", response.getCode());
        log("response body: %s", body);
        return api.getRequestTokenExtractor().extract(response);
    }

    public Future<OAuth1RequestToken> getRequestTokenAsync() {
        return getRequestTokenAsync(null);
    }

    public Future<OAuth1RequestToken> getRequestTokenAsync(OAuthAsyncRequestCallback<OAuth1RequestToken> callback) {
        log("async obtaining request token from %s", api.getRequestTokenEndpoint());
        final OAuthRequest request = prepareRequestTokenRequest();
        return execute(request, callback, new OAuthRequest.ResponseConverter<OAuth1RequestToken>() {
            @Override
            public OAuth1RequestToken convert(Response response) throws IOException {
                return getApi().getRequestTokenExtractor().extract(response);
            }
        });
    }

    protected OAuthRequest prepareRequestTokenRequest() {
        final OAuthRequest request = new OAuthRequest(api.getRequestTokenVerb(), api.getRequestTokenEndpoint());
        String callback = getCallback();
        if (callback == null) {
            callback = OAuthConstants.OOB;
        }
        log("setting oauth_callback to %s", callback);
        request.addOAuthParameter(OAuthConstants.CALLBACK, callback);
        addOAuthParams(request, "");
        appendSignature(request);
        return request;
    }

    protected void addOAuthParams(OAuthRequest request, String tokenSecret) {
        request.addOAuthParameter(OAuthConstants.TIMESTAMP, api.getTimestampService().getTimestampInSeconds());
        request.addOAuthParameter(OAuthConstants.NONCE, api.getTimestampService().getNonce());
        request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, getApiKey());
        request.addOAuthParameter(OAuthConstants.SIGN_METHOD, api.getSignatureService().getSignatureMethod());
        request.addOAuthParameter(OAuthConstants.VERSION, getVersion());
        final String scope = getScope();
        if (scope != null) {
            request.addOAuthParameter(OAuthConstants.SCOPE, scope);
        }
        request.addOAuthParameter(OAuthConstants.SIGNATURE, getSignature(request, tokenSecret));

        log("appended additional OAuth parameters: %s", request.getOauthParameters());
    }

    public OAuth1AccessToken getAccessToken(OAuth1RequestToken requestToken, String oauthVerifier)
            throws IOException, InterruptedException, ExecutionException {
        log("obtaining access token from %s", api.getAccessTokenEndpoint());
        final OAuthRequest request = prepareAccessTokenRequest(requestToken, oauthVerifier);
        final Response response = execute(request);
        return api.getAccessTokenExtractor().extract(response);
    }

    public Future<OAuth1AccessToken> getAccessTokenAsync(OAuth1RequestToken requestToken, String oauthVerifier) {
        return getAccessTokenAsync(requestToken, oauthVerifier, null);
    }

    /**
     * Start the request to retrieve the access token. The optionally provided callback will be called with the Token
     * when it is available.
     *
     * @param requestToken request token (obtained previously or null)
     * @param oauthVerifier oauth_verifier
     * @param callback optional callback
     * @return Future
     */
    public Future<OAuth1AccessToken> getAccessTokenAsync(OAuth1RequestToken requestToken, String oauthVerifier,
            OAuthAsyncRequestCallback<OAuth1AccessToken> callback) {
        log("async obtaining access token from %s", api.getAccessTokenEndpoint());
        final OAuthRequest request = prepareAccessTokenRequest(requestToken, oauthVerifier);
        return execute(request, callback, new OAuthRequest.ResponseConverter<OAuth1AccessToken>() {
            @Override
            public OAuth1AccessToken convert(Response response) throws IOException {
                return getApi().getAccessTokenExtractor().extract(response);
            }
        });
    }

    protected OAuthRequest prepareAccessTokenRequest(OAuth1RequestToken requestToken, String oauthVerifier) {
        final OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        request.addOAuthParameter(OAuthConstants.TOKEN, requestToken.getToken());
        request.addOAuthParameter(OAuthConstants.VERIFIER, oauthVerifier);
        log("setting token to: %s and verifier to: %s", requestToken, oauthVerifier);
        addOAuthParams(request, requestToken.getTokenSecret());
        appendSignature(request);
        return request;
    }

    public void signRequest(OAuth1AccessToken token, OAuthRequest request) {
        log("signing request: %s", request.getCompleteUrl());

        if (!token.isEmpty() || api.isEmptyOAuthTokenParamIsRequired()) {
            request.addOAuthParameter(OAuthConstants.TOKEN, token.getToken());
        }
        log("setting token to: %s", token);
        addOAuthParams(request, token.getTokenSecret());
        appendSignature(request);
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    /**
     * Returns the URL where you should redirect your users to authenticate your application.
     *
     * @param requestToken the request token you need to authorize
     * @return the URL where you should redirect your users
     */
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return api.getAuthorizationUrl(requestToken);
    }

    private String getSignature(OAuthRequest request, String tokenSecret) {
        log("generating signature...");
        final String baseString = api.getBaseStringExtractor().extract(request);
        final String signature = api.getSignatureService().getSignature(baseString, getApiSecret(), tokenSecret);

        log("base string is: %s", baseString);
        log("signature is: %s", signature);
        return signature;
    }

    protected void appendSignature(OAuthRequest request) {
        final OAuth1SignatureType signatureType = api.getSignatureType();
        switch (signatureType) {
            case Header:
                log("using Http Header signature");

                final String oauthHeader = api.getHeaderExtractor().extract(request);
                request.addHeader(OAuthConstants.HEADER, oauthHeader);
                break;
            case QueryString:
                log("using Querystring signature");

                for (Map.Entry<String, String> oauthParameter : request.getOauthParameters().entrySet()) {
                    request.addQuerystringParameter(oauthParameter.getKey(), oauthParameter.getValue());
                }
                break;
            default:
                throw new IllegalStateException("Unknown new Signature Type '" + signatureType + "'.");
        }
    }

    public DefaultApi10a getApi() {
        return api;
    }

    public void log(String messagePattern, Object... params) {
        if (debugStream != null) {
            final String message = String.format(messagePattern, params) + '\n';
            try {
                debugStream.write(message.getBytes("UTF8"));
            } catch (IOException | RuntimeException e) {
                throw new RuntimeException("there were problems while writting to the debug stream", e);
            }
        }
    }
}
