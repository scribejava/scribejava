package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.OdnoklassnikiTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.scribe.oauth.OdnoklassnikiOAuth20ServiceImpl;
import org.scribe.utils.Preconditions;
import org.scribe.utils.URLUtils;

/**
 * User: elwood
 * Date: 24.03.2011
 * Time: 16:25:40
 *
 * Implementation for odnoklassniki.ru
 * http://dev.odnoklassniki.ru/wiki/display/ok/The+OAuth+2.0+Protocol
 */
public class OdnoklassnikiApi extends DefaultApi20 {

    // Also you can add the scope parameter if need (see documentation for available values of scope).
    private final static String AUTHORIZE_URL = "http://www.odnoklassniki.ru/oauth/authorize?client_id=%s&response_type=code&redirect_uri=%s";

    @Override
    public String getAccessTokenEndpoint() {
        return "http://api.odnoklassniki.ru/oauth/token.do";
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. Facebook does not support OOB");
        return String.format(AUTHORIZE_URL, config.getApiKey(), URLUtils.formURLEncode(config.getCallback()));
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new OdnoklassnikiTokenExtractor();
    }

    @Override
    public OAuthService createService(OAuthConfig config, String scope) {
        return new OdnoklassnikiOAuth20ServiceImpl(this, config);
    }
}
