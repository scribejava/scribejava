package com.github.scribejava.apis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.extractors.AccessTokenExtractor;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;

public class ConstantContactApi2 extends DefaultApi20 {

    private static final String AUTHORIZE_URL
            = "https://oauth2.constantcontact.com/oauth2/oauth/siteowner/authorize?client_id=%s&response_type=code&redirect_uri=%s";
    private static final AccessTokenExtractor ACCESS_TOKEN_EXTRACTOR = new AccessTokenExtractor() {

        @Override
        public Token extract(String response) {
            Preconditions.checkEmptyString(response, "Response body is incorrect. Can't extract a token from an empty string");

            final String regex = "\"access_token\"\\s*:\\s*\"([^&\"]+)\"";
            final Matcher matcher = Pattern.compile(regex).matcher(response);
            if (matcher.find()) {
                final String token = OAuthEncoder.decode(matcher.group(1));
                return new Token(token, "", response);
            } else {
                throw new OAuthException("Response body is incorrect. Can't extract a token from this: '" + response + "'", null);
            }
        }
    };

    private ConstantContactApi2() {
    }

    private static class InstanceHolder {
        private static final ConstantContactApi2 INSTANCE = new ConstantContactApi2();
    }

    public static ConstantContactApi2 instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://oauth2.constantcontact.com/oauth2/oauth/token?grant_type=" + OAuthConstants.AUTHORIZATION_CODE;
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return ACCESS_TOKEN_EXTRACTOR;
    }
}
