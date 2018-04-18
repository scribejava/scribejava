package com.github.scribejava.core.builder.api;

import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.ParameterList;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import java.io.OutputStream;
import java.util.Map;

/**
 * Default implementation of the OAuth protocol, version 2.0
 *
 * This class is meant to be extended by concrete implementations of the API, providing the endpoints and
 * endpoint-http-verbs.
 *
 * If your API adheres to the 2.0 protocol correctly, you just need to extend this class and define the getters for your
 * endpoints.
 *
 * If your API does something a bit different, you can override the different extractors or services, in order to
 * fine-tune the process. Please read the javadocs of the interfaces to get an idea of what to do.
 *
 */
public abstract class DefaultApi20 implements BaseApi<OAuth20Service> {

    /**
     * Returns the access token extractor.
     *
     * @return access token extractor
     */
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return OAuth2AccessTokenJsonExtractor.instance();
    }

    /**
     * Returns the verb for the access token endpoint (defaults to POST)
     *
     * @return access token endpoint verb
     */
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    /**
     * Returns the URL that receives the access token requests.
     *
     * @return access token URL
     */
    public abstract String getAccessTokenEndpoint();

    public String getRefreshTokenEndpoint() {
        return getAccessTokenEndpoint();
    }

    /**
     * As stated in RFC 7009 OAuth 2.0 Token Revocation
     *
     * @return endpoint, which allows clients to notify the authorization server that a previously obtained refresh or
     * access token is no longer needed.
     * @see <a href="https://tools.ietf.org/html/rfc7009">RFC 7009</a>
     */
    public String getRevokeTokenEndpoint() {
        throw new UnsupportedOperationException(
                "This API doesn't support revoking tokens or we have no info about this");
    }

    protected abstract String getAuthorizationBaseUrl();

    /**
     * @deprecated use {@link #getAuthorizationUrl(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.util.Map)}
     */
    @Deprecated
    public String getAuthorizationUrl(OAuthConfig config, Map<String, String> additionalParams) {
        return getAuthorizationUrl(config.getResponseType(), config.getApiKey(), config.getCallback(),
                config.getScope(), config.getState(), additionalParams);
    }

    /**
     * Returns the URL where you should redirect your users to authenticate your application.
     *
     * @param config OAuth 2.0 configuration param object
     * @param additionalParams any additional GET params to add to the URL
     * @return the URL where you should redirect your users
     */
    public String getAuthorizationUrl(String responseType, String apiKey, String callback, String scope, String state,
            Map<String, String> additionalParams) {
        final ParameterList parameters = new ParameterList(additionalParams);
        parameters.add(OAuthConstants.RESPONSE_TYPE, responseType);
        parameters.add(OAuthConstants.CLIENT_ID, apiKey);

        if (callback != null) {
            parameters.add(OAuthConstants.REDIRECT_URI, callback);
        }

        if (scope != null) {
            parameters.add(OAuthConstants.SCOPE, scope);
        }

        if (state != null) {
            parameters.add(OAuthConstants.STATE, state);
        }

        return parameters.appendTo(getAuthorizationBaseUrl());
    }

    /**
     * @deprecated use {@link #createService(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.io.OutputStream, java.lang.String, java.lang.String, java.lang.String,
     * com.github.scribejava.core.httpclient.HttpClientConfig, com.github.scribejava.core.httpclient.HttpClient)}
     */
    @Deprecated
    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return createService(config.getApiKey(), config.getApiSecret(), config.getCallback(), config.getScope(),
                config.getDebugStream(), config.getState(), config.getResponseType(), config.getUserAgent(),
                config.getHttpClientConfig(), config.getHttpClient());
    }

    @Override
    public OAuth20Service createService(String apiKey, String apiSecret, String callback, String scope,
            OutputStream debugStream, String state, String responseType, String userAgent,
            HttpClientConfig httpClientConfig, HttpClient httpClient) {
        return new OAuth20Service(this, apiKey, apiSecret, callback, scope, debugStream, state, responseType, userAgent,
                httpClientConfig, httpClient);
    }

    public OAuth2SignatureType getSignatureType() {
        return OAuth2SignatureType.BEARER_AUTHORIZATION_REQUEST_HEADER_FIELD;
    }

    public ClientAuthenticationType getClientAuthenticationType() {
        return ClientAuthenticationType.HTTP_BASIC_AUTHENTICATION_SCHEME;
    }
}
