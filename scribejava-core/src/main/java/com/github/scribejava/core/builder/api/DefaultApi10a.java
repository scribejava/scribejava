package com.github.scribejava.core.builder.api;

import com.github.scribejava.core.extractors.BaseStringExtractor;
import com.github.scribejava.core.extractors.BaseStringExtractorImpl;
import com.github.scribejava.core.extractors.HeaderExtractor;
import com.github.scribejava.core.extractors.HeaderExtractorImpl;
import com.github.scribejava.core.extractors.OAuth1AccessTokenExtractor;
import com.github.scribejava.core.extractors.OAuth1RequestTokenExtractor;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.github.scribejava.core.services.HMACSha1SignatureService;
import com.github.scribejava.core.services.SignatureService;
import com.github.scribejava.core.services.TimestampService;
import com.github.scribejava.core.services.TimestampServiceImpl;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.ParameterList;
import java.io.OutputStream;

/**
 * Default implementation of the OAuth protocol, version 1.0a
 *
 * This class is meant to be extended by concrete implementations of the API, providing the endpoints and
 * endpoint-http-verbs.
 *
 * If your Api adheres to the 1.0a protocol correctly, you just need to extend this class and define the getters for
 * your endpoints.
 *
 * If your Api does something a bit different, you can override the different extractors or services, in order to
 * fine-tune the process. Please read the javadocs of the interfaces to get an idea of what to do.
 *
 */
public abstract class DefaultApi10a implements BaseApi<OAuth10aService> {

    /**
     * Returns the access token extractor.
     *
     * @return access token extractor
     */
    public TokenExtractor<OAuth1AccessToken> getAccessTokenExtractor() {
        return OAuth1AccessTokenExtractor.instance();
    }

    /**
     * Returns the base string extractor.
     *
     * @return base string extractor
     */
    public BaseStringExtractor getBaseStringExtractor() {
        return new BaseStringExtractorImpl();
    }

    /**
     * Returns the header extractor.
     *
     * @return header extractor
     */
    public HeaderExtractor getHeaderExtractor() {
        return new HeaderExtractorImpl();
    }

    /**
     * Returns the request token extractor.
     *
     * @return request token extractor
     */
    public TokenExtractor<OAuth1RequestToken> getRequestTokenExtractor() {
        return OAuth1RequestTokenExtractor.instance();
    }

    /**
     * Returns the signature service.
     *
     * @return signature service
     */
    public SignatureService getSignatureService() {
        return new HMACSha1SignatureService();
    }

    /**
     * @return the signature type, choose between header, querystring, etc. Defaults to Header
     */
    public OAuth1SignatureType getSignatureType() {
        return OAuth1SignatureType.Header;
    }

    /**
     * Returns the timestamp service.
     *
     * @return timestamp service
     */
    public TimestampService getTimestampService() {
        return new TimestampServiceImpl();
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
     * Returns the verb for the request token endpoint (defaults to POST)
     *
     * @return request token endpoint verb
     */
    public Verb getRequestTokenVerb() {
        return Verb.POST;
    }

    /**
     * Returns the URL that receives the request token requests.
     *
     * @return request token URL
     */
    public abstract String getRequestTokenEndpoint();

    /**
     * Returns the URL that receives the access token requests.
     *
     * @return access token URL
     */
    public abstract String getAccessTokenEndpoint();

    protected abstract String getAuthorizationBaseUrl();

    /**
     * Returns the URL where you should redirect your users to authenticate your application.
     *
     * @param requestToken the request token you need to authorize
     * @return the URL where you should redirect your users
     */
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        final ParameterList parameters = new ParameterList();
        parameters.add(OAuthConstants.TOKEN, requestToken.getToken());
        return parameters.appendTo(getAuthorizationBaseUrl());
    }

    @Override
    public OAuth10aService createService(String apiKey, String apiSecret, String callback, String scope,
            OutputStream debugStream, String state, String responseType, String userAgent,
            HttpClientConfig httpClientConfig, HttpClient httpClient) {
        return new OAuth10aService(this, apiKey, apiSecret, callback, scope, debugStream, userAgent, httpClientConfig,
                httpClient);
    }

    /**
     * http://tools.ietf.org/html/rfc5849 says that "The client MAY omit the empty "oauth_token" protocol parameter from
     * the request", but not all oauth servers are good boys.
     *
     * @return whether to inlcude empty oauth_token param to the request
     */
    public boolean isEmptyOAuthTokenParamIsRequired() {
        return false;
    }
}
