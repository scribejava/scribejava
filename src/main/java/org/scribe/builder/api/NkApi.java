package org.scribe.builder.api;

import static org.scribe.utils.URLUtils.formURLEncode;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.BaseStringExtractor;
import org.scribe.extractors.BaseStringExtractorImpl;
import org.scribe.extractors.HeaderExtractor;
import org.scribe.extractors.HeaderExtractorImpl;
import org.scribe.extractors.NkTokenExtractorImpl;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.oauth.NKOAuthServiceImpl;
import org.scribe.oauth.OAuthService;
import org.scribe.services.HMACSha1SignatureService;
import org.scribe.services.SignatureService;
import org.scribe.services.TimestampService;
import org.scribe.services.TimestampServiceImpl;

public class NkApi implements Api {

    private static final String AUTHORIZE_URL = "https://ssl.3pp.nk.pl/oauth2/login?client_id=%s&response_type=code&redirect_uri=%s";
    private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";
    private static final String accessTokenEndpoint = "http://nk.pl/oauth2/token?grant_type=authorization_code";

    public String getAccessTokenEndpoint() {
        return accessTokenEndpoint;
    }

    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    public String getAuthorizationUrl(OAuthConfig config) {
        if (config.hasScope()) {
            return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(), formURLEncode(config.getCallback()),
                    formURLEncode(config.getScope()));
        } else {
            return String.format(AUTHORIZE_URL, config.getApiKey(), formURLEncode(config.getCallback()));
        }
    }

    public OAuthService createService(OAuthConfig config) {
        return new NKOAuthServiceImpl(this, config);
    }

    public AccessTokenExtractor getAccessTokenExtractor() {
        return new NkTokenExtractorImpl();
    }

    public TimestampService getTimestampService() {
        return new TimestampServiceImpl();
    }

    public SignatureService getSignatureService() {
        return new HMACSha1SignatureService();
    }

    public HeaderExtractor getHeaderExtractor() {
        return new HeaderExtractorImpl();
    }

    public BaseStringExtractor getBaseStringExtractor() {
        return new BaseStringExtractorImpl();
    }
}
