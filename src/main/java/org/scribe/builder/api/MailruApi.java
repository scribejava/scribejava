package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.MailruTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.oauth.MailruOAuth20ServiceImpl;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.Preconditions;
import org.scribe.utils.URLUtils;

/**
 * User: elwood
 * Date: 22.03.2011
 * Time: 17:15:29
 */
public class MailruApi extends DefaultApi20 {

    private static final String AUTHORIZE_URL = "https://connect.mail.ru/oauth/authorize?client_id=%s&scope=widget&response_type=code&redirect_uri=%s";

    @Override
    public String getAccessTokenEndpoint() {
        return "https://connect.mail.ru/oauth/token";
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback.");
        return String.format(AUTHORIZE_URL, config.getApiKey(), URLUtils.formURLEncode(config.getCallback()));
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new MailruTokenExtractor();
    }

    @Override
    public OAuthService createService(OAuthConfig config, String scope) {
        return new MailruOAuth20ServiceImpl(this, config);
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }
}
