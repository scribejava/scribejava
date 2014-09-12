package org.scribe.oauth;

import com.ning.http.client.AsyncHttpClient;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Future;
import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.AbstractRequest;
import org.scribe.model.OAuthAsyncRequestCallback;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConfigAsync;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.OAuthRequestAsync;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.services.Base64Encoder;
import org.scribe.utils.MapUtils;

/**
 * OAuth 1.0a implementation of {@link OAuthService}
 *
 * @author Pablo Fernandez
 */
public class OAuth10aServiceImpl implements OAuthService {

    private static final String VERSION = "1.0";

    private final OAuthConfig config;
    private final DefaultApi10a api;
    private AsyncHttpClient asyncHttpClient = null;

    /**
     * Default constructor
     *
     * @param api OAuth1.0a api information
     * @param config OAuth 1.0a configuration param object
     */
    public OAuth10aServiceImpl(final DefaultApi10a api, final OAuthConfig config) {
        this.api = api;
        this.config = config;
        if (config instanceof OAuthConfigAsync) {
            asyncHttpClient = new AsyncHttpClient(((OAuthConfigAsync) config).getAsyncHttpClientConfig());
        }
    }

    public Token getRequestToken() {
        config.log("obtaining request token from " + api.getRequestTokenEndpoint());
        final OAuthRequest request = new OAuthRequest(api.getRequestTokenVerb(), api.getRequestTokenEndpoint(), this);

        config.log("setting oauth_callback to " + config.getCallback());
        request.addOAuthParameter(OAuthConstants.CALLBACK, config.getCallback());
        addOAuthParams(request, OAuthConstants.EMPTY_TOKEN);
        appendSignature(request);

        config.log("sending request...");
        final Response response = request.send();
        final String body = response.getBody();

        config.log("response status code: " + response.getCode());
        config.log("response body: " + body);
        return api.getRequestTokenExtractor().extract(body);
    }

    private void addOAuthParams(final AbstractRequest request, final Token token) {
        request.addOAuthParameter(OAuthConstants.TIMESTAMP, api.getTimestampService().getTimestampInSeconds());
        request.addOAuthParameter(OAuthConstants.NONCE, api.getTimestampService().getNonce());
        request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, config.getApiKey());
        request.addOAuthParameter(OAuthConstants.SIGN_METHOD, api.getSignatureService().getSignatureMethod());
        request.addOAuthParameter(OAuthConstants.VERSION, getVersion());
        if (config.hasScope()) {
            request.addOAuthParameter(OAuthConstants.SCOPE, config.getScope());
        }
        request.addOAuthParameter(OAuthConstants.SIGNATURE, getSignature(request, token));

        config.log("appended additional OAuth parameters: " + MapUtils.toString(request.getOauthParameters()));
    }

    public Token getAccessToken(final Token requestToken, final Verifier verifier) {
        config.log("obtaining access token from " + api.getAccessTokenEndpoint());
        final OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint(), this);
        prepareAccessTokenRequest(request, requestToken, verifier);
        final Response response = request.send();
        return api.getAccessTokenExtractor().extract(response.getBody());
    }

    @Override
    public Future<Token> getAccessTokenAsync(Token requestToken, Verifier verifier, OAuthAsyncRequestCallback<Token> callback) {
        config.log("async obtaining access token from " + api.getAccessTokenEndpoint());
        final OAuthRequestAsync request = new OAuthRequestAsync(api.getAccessTokenVerb(), api.getAccessTokenEndpoint(), this);
        prepareAccessTokenRequest(request, requestToken, verifier);
        return request.sendAsync(callback, new OAuthRequestAsync.ResponseConverter<Token>() {
            @Override
            public Token convert(com.ning.http.client.Response response) throws IOException {
                return api.getAccessTokenExtractor().extract(OAuthRequestAsync.RESPONSE_CONVERTER.convert(response).getBody());
            }
        });
    }

    private void prepareAccessTokenRequest(AbstractRequest request, Token requestToken, Verifier verifier) {
        request.addOAuthParameter(OAuthConstants.TOKEN, requestToken.getToken());
        request.addOAuthParameter(OAuthConstants.VERIFIER, verifier.getValue());
        config.log("setting token to: " + requestToken + " and verifier to: " + verifier);
        addOAuthParams(request, requestToken);
        appendSignature(request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void signRequest(final Token token, final AbstractRequest request) {
        config.log("signing request: " + request.getCompleteUrl());

        // Do not append the token if empty. This is for two legged OAuth calls.
        if (!token.isEmpty()) {
            request.addOAuthParameter(OAuthConstants.TOKEN, token.getToken());
        }
        config.log("setting token to: " + token);
        addOAuthParams(request, token);
        appendSignature(request);
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
    public String getAuthorizationUrl(final Token requestToken) {
        return api.getAuthorizationUrl(requestToken);
    }

    @Override
    public OAuthConfig getConfig() {
        return config;
    }

    @Override
    public AsyncHttpClient getAsyncHttpClient() {
        return asyncHttpClient;
    }

    @Override
    public void closeAsyncClient() {
        asyncHttpClient.close();
    }

    private String getSignature(final AbstractRequest request, final Token token) {
        config.log("generating signature...");
        config.log("using base64 encoder: " + Base64Encoder.type());
        final String baseString = api.getBaseStringExtractor().extract(request);
        final String signature = api.getSignatureService().getSignature(baseString, config.getApiSecret(), token.
                getSecret());

        config.log("base string is: " + baseString);
        config.log("signature is: " + signature);
        return signature;
    }

    private void appendSignature(final AbstractRequest request) {
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
}
