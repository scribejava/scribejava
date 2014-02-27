package org.scribe.oauth;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Request;
import org.scribe.model.RequestTuner;
import org.scribe.model.Response;
import static org.scribe.model.SignatureType.Header;
import static org.scribe.model.SignatureType.QueryString;
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

    /**
     * Default constructor
     *
     * @param api OAuth1.0a api information
     * @param config OAuth 1.0a configuration param object
     */
    public OAuth10aServiceImpl(final DefaultApi10a api, final OAuthConfig config) {
        this.api = api;
        this.config = config;
    }

    public Token getRequestToken(final int timeout, final TimeUnit unit) {
        return getRequestToken(new TimeoutTuner(timeout, unit));
    }

    @Override
    public Token getRequestToken() {
        return getRequestToken(2, TimeUnit.SECONDS);
    }

    public Token getRequestToken(final RequestTuner tuner) {
        config.log("obtaining request token from " + api.getRequestTokenEndpoint());
        final OAuthRequest request = new OAuthRequest(api.getRequestTokenVerb(), api.getRequestTokenEndpoint());

        config.log("setting oauth_callback to " + config.getCallback());
        request.addOAuthParameter(OAuthConstants.CALLBACK, config.getCallback());
        addOAuthParams(request, OAuthConstants.EMPTY_TOKEN);
        appendSignature(request);

        config.log("sending request...");
        final Response response = request.send(tuner);
        final String body = response.getBody();

        config.log("response status code: " + response.getCode());
        config.log("response body: " + body);
        return api.getRequestTokenExtractor().extract(body);
    }

    private void addOAuthParams(final OAuthRequest request, final Token token) {
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

    public Token getAccessToken(final Token requestToken, final Verifier verifier, final int timeout, final TimeUnit unit) {
        return getAccessToken(requestToken, verifier, new TimeoutTuner(timeout, unit));
    }

    @Override
    public Token getAccessToken(final Token requestToken, final Verifier verifier) {
        return getAccessToken(requestToken, verifier, 2, TimeUnit.SECONDS);
    }

    public Token getAccessToken(final Token requestToken, final Verifier verifier, final RequestTuner tuner) {
        config.log("obtaining access token from " + api.getAccessTokenEndpoint());
        final OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        request.addOAuthParameter(OAuthConstants.TOKEN, requestToken.getToken());
        request.addOAuthParameter(OAuthConstants.VERIFIER, verifier.getValue());

        config.log("setting token to: " + requestToken + " and verifier to: " + verifier);
        addOAuthParams(request, requestToken);
        appendSignature(request);
        final Response response = request.send(tuner);
        return api.getAccessTokenExtractor().extract(response.getBody());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void signRequest(final Token token, final OAuthRequest request) {
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

    private String getSignature(final OAuthRequest request, final Token token) {
        config.log("generating signature...");
        config.log("using base64 encoder: " + Base64Encoder.type());
        final String baseString = api.getBaseStringExtractor().extract(request);
        final String signature = api.getSignatureService().getSignature(baseString, config.getApiSecret(), token.
                getSecret());

        config.log("base string is: " + baseString);
        config.log("signature is: " + signature);
        return signature;
    }

    private void appendSignature(final OAuthRequest request) {
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

    private static class TimeoutTuner extends RequestTuner {

        private final int duration;
        private final TimeUnit unit;

        public TimeoutTuner(final int duration, final TimeUnit unit) {
            this.duration = duration;
            this.unit = unit;
        }

        @Override
        public void tune(final Request request) {
            request.setReadTimeout(duration, unit);
        }
    }
}
