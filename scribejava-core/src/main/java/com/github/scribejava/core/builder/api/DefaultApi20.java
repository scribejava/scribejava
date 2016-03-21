package com.github.scribejava.core.builder.api;

import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.utils.OAuthEncoder;
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
     * Returns the URL where you should redirect your users to authenticate your application.
     *
     * @param config OAuth 2.0 configuration param object
     * @return the URL where you should redirect your users
     */
    public abstract String getAuthorizationUrl(OAuthConfig config);

    /**
     * Returns the URL where you should redirect your users to authenticate your application.
     *
     * @param config OAuth 2.0 configuration param object
     * @param additionalParams any additional GET params to add to the URL
     * @return the URL where you should redirect your users
     */
    public String getAuthorizationUrl(OAuthConfig config, Map<String, String> additionalParams) {
        String authUrl = getAuthorizationUrl(config);

        if (additionalParams != null && !additionalParams.isEmpty()) {
            final StringBuilder authUrlWithParams = new StringBuilder(authUrl)
                    .append(authUrl.indexOf('?') == -1 ? '?' : '&');

            for (Map.Entry<String, String> param : additionalParams.entrySet()) {
                authUrlWithParams.append(OAuthEncoder.encode(param.getKey()))
                        .append('=')
                        .append(OAuthEncoder.encode(param.getValue()))
                        .append('&');
            }

            authUrl = authUrlWithParams.substring(0, authUrlWithParams.length() - 1);
        }

        return authUrl;
    }

    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return new OAuth20Service(this, config);
    }
}
