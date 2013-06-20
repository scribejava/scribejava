package org.scribe.builder.api;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scribe.exceptions.OAuthException;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuth20ServiceImpl;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

/**
 * Google OAuth2.0
 * Released under the same license as scribe (MIT License)
 * @author houman001
 * This code borrows from and modifies changes made by @yincrash
 * @author yincrash
 *
 */
public class Google2Api extends DefaultApi20 {

    private static final String AUTHORIZE_URL = "https://accounts.google.com/o/oauth2/auth?response_type=code&client_id=%s&redirect_uri=%s";
    private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";
    private static final String SUFFIX_OFFLINE = "&access_type=offline";

    @Override
    public String getAccessTokenEndpoint() {
        return "https://accounts.google.com/o/oauth2/token";
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new AccessTokenExtractor() {

            public Token extract(String response) {
                Preconditions.checkEmptyString(response, "Response body is incorrect. Can't extract a token from an empty string");

                Matcher matcher = Pattern.compile("\"access_token\" : \"([^&\"]+)\"").matcher(response);
                if (matcher.find())
                {
                    String token = OAuthEncoder.decode(matcher.group(1));
                    String refreshToken = "";
                    Matcher refreshMatcher = Pattern.compile("\"refresh_token\" : \"([^&\"]+)\"").matcher(response);
                    if (refreshMatcher.find())
                    	refreshToken = OAuthEncoder.decode(refreshMatcher.group(1));
                    Token result = new Token(token, refreshToken, response);
                    Matcher expiryMatcher = Pattern.compile("\"expires_in\" : ([^,&\"]+)").matcher(response);
                    if (expiryMatcher.find())
                    {
                        int lifeTime = Integer.parseInt(OAuthEncoder.decode(expiryMatcher.group(1)));
                        Date expiry = new Date(System.currentTimeMillis() + lifeTime * 1000);
                        result.setExpiry(expiry);
                    }
                    return result;
                }
                else
                {
                    throw new OAuthException("Response body is incorrect. Can't extract a token from this: '" + response + "'", null);
                }
            }
        };
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        // Append scope if present
        if (config.hasScope()) {
            String format = config.isOffline() ? SCOPED_AUTHORIZE_URL + SUFFIX_OFFLINE : SCOPED_AUTHORIZE_URL;
            return String.format(format, config.getApiKey(),
                    OAuthEncoder.encode(config.getCallback()),
                    OAuthEncoder.encode(config.getScope()));
        } else {
            String format = config.isOffline() ? AUTHORIZE_URL + SUFFIX_OFFLINE : AUTHORIZE_URL;
            return String.format(format, config.getApiKey(),
                    OAuthEncoder.encode(config.getCallback()));
        }
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public OAuthService createService(OAuthConfig config) {
        return new GoogleOAuth2Service(this, config);
    }

    private static class GoogleOAuth2Service extends OAuth20ServiceImpl {

        private DefaultApi20 api;
        private OAuthConfig config;

        public GoogleOAuth2Service(DefaultApi20 api, OAuthConfig config) {
            super(api, config);
            this.api = api;
            this.config = config;
        }

        @Override
        public Token getAccessToken(Token requestToken, Verifier verifier) {
            OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
            switch (api.getAccessTokenVerb()) {
                case POST:
                    request.addBodyParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
                    // API Secret is optional
                    if (config.getApiSecret() != null && config.getApiSecret().length() > 0)
                        request.addBodyParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
                    if (requestToken == null) {
                        request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
                        request.addBodyParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
                        request.addBodyParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.GRANT_TYPE_AUTHORIZATION_CODE);
                    } else {
                        request.addBodyParameter(OAuthConstants.REFRESH_TOKEN, requestToken.getSecret());
                        request.addBodyParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.GRANT_TYPE_REFRESH_TOKEN);
                    }
                    break;
                case GET:
                default:
                    request.addQuerystringParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
                    // API Secret is optional
                    if (config.getApiSecret() != null && config.getApiSecret().length() > 0)
                        request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
                    request.addQuerystringParameter(OAuthConstants.CODE, verifier.getValue());
                    request.addQuerystringParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
                    if(config.hasScope()) request.addQuerystringParameter(OAuthConstants.SCOPE, config.getScope());
            }
            Response response = request.send();
            return api.getAccessTokenExtractor().extract(response.getBody());
        }
    }

}
