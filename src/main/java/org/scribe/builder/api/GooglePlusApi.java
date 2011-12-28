package org.scribe.builder.api;

import org.scribe.builder.api.DefaultApi20a;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.utils.OAuthEncoder;

/**
 * User: ezgraphs
 * Date: 12/27/11
 * Time: 8:54 AM
 */
public class GooglePlusApi extends DefaultApi20a {

    private static final String BASE_URL = "https://accounts.google.com/o/oauth2";
    private static final String AUTHORIZE_URL = BASE_URL + "/auth?response_type=code&client_id=%s&scope=%s&redirect_uri=%s";


    @Override
    public String getAccessTokenEndpoint() {

        return BASE_URL + "/token";
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getScope()), OAuthEncoder.encode(config.getCallback()));
    }
}
