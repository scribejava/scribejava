package com.github.scribejava.core.builder.api;

import com.github.scribejava.core.extractors.AccessTokenExtractor;
import com.github.scribejava.core.extractors.BaseStringExtractor;
import com.github.scribejava.core.extractors.BaseStringExtractorImpl;
import com.github.scribejava.core.extractors.HeaderExtractor;
import com.github.scribejava.core.extractors.HeaderExtractorImpl;
import com.github.scribejava.core.extractors.RequestTokenExtractor;
import com.github.scribejava.core.extractors.TokenExtractorImpl;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aServiceImpl;
import com.github.scribejava.core.oauth.OAuthService;
import com.github.scribejava.core.services.HMACSha1SignatureService;
import com.github.scribejava.core.services.SignatureService;
import com.github.scribejava.core.services.TimestampService;
import com.github.scribejava.core.services.TimestampServiceImpl;

/**
 * Default implementation of the OAuth protocol, version 1.0a
 *
 * This class is meant to be extended by concrete implementations of the API, providing the endpoints and endpoint-http-verbs.
 *
 * If your Api adheres to the 1.0a protocol correctly, you just need to extend this class and define the getters for your endpoints.
 *
 * If your Api does something a bit different, you can override the different extractors or services, in order to fine-tune the process. Please read
 * the javadocs of the interfaces to get an idea of what to do.
 *
 * @author Pablo Fernandez
 *
 */
public abstract class DefaultApi10a implements Api {

    /**
     * Returns the access token extractor.
     *
     * @return access token extractor
     */
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new TokenExtractorImpl();
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
    public RequestTokenExtractor getRequestTokenExtractor() {
        return new TokenExtractorImpl();
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

    /**
     * Returns the URL where you should redirect your users to authenticate your application.
     *
     * @param requestToken the request token you need to authorize
     * @return the URL where you should redirect your users
     */
    public abstract String getAuthorizationUrl(Token requestToken);

    @Override
    public OAuthService createService(final OAuthConfig config) {
        return new OAuth10aServiceImpl(this, config);
    }
}
