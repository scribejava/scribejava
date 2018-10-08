package com.github.scribejava.core.builder.api;

import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.ParameterList;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignature;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignatureAuthorizationRequestHeaderField;
import com.github.scribejava.core.oauth2.clientauthentication.ClientAuthentication;
import com.github.scribejava.core.oauth2.clientauthentication.HttpBasicAuthenticationScheme;
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
     * Returns the URL where you should redirect your users to authenticate your application.
     *
     * @param responseType responseType
     * @param apiKey apiKey
     * @param additionalParams any additional GET params to add to the URL
     * @param callback callback
     * @param state state
     * @param scope scope
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

    @Override
    public OAuth20Service createService(String apiKey, String apiSecret, String callback, String scope,
            OutputStream debugStream, String state, String responseType, String userAgent,
            HttpClientConfig httpClientConfig, HttpClient httpClient) {
        return new OAuth20Service(this, apiKey, apiSecret, callback, scope, state, responseType, userAgent,
                httpClientConfig, httpClient);
    }

    public BearerSignature getBearerSignature() {
        return BearerSignatureAuthorizationRequestHeaderField.instance();
    }

    public ClientAuthentication getClientAuthentication() {
        return HttpBasicAuthenticationScheme.instance();
    }
}
