package org.scribe.builder.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scribe.exceptions.OAuthException;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuth20HeaderServiceImpl;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

public class BiblioetecaApi20 extends DefaultApi20
{
  private static final String AUTHORIZE_URL = "http://api.biblioeteca.com/biblioeteca.web/oauth2/authorize?client_id=%s&response_type=code&redirect_uri=%s";
  private static final String ACCESS_TOKEN_URL="http://api.biblioeteca.com/biblioeteca.web/oauth2/token?grant_type=authorization_code";
  
  @Override
  public String getAccessTokenEndpoint()
  {
    return ACCESS_TOKEN_URL;
  }

@Override
public String getAuthorizationUrl(OAuthConfig config) {
	return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
}

@Override
public Verb getAccessTokenVerb() {
    return Verb.POST;
}

// {"expires_in":"3600","refresh_token":"301f4c7f388f25a5a98d0cfb5c910","access_token":"db3a5a071f5563f75ca3b7f6573b352"}

@Override
public AccessTokenExtractor getAccessTokenExtractor() {
    return new AccessTokenExtractor() {
        
        @Override
        public Token extract(String response) {
            Preconditions.checkEmptyString(response, "Response body is incorrect. Can't extract a token from an empty string");

            Matcher matcher = Pattern.compile("\"access_token\":\"([^&\"]+)\"").matcher(response);
            if (matcher.find())
            {
              String token = OAuthEncoder.decode(matcher.group(1));
              return new Token(token, "", response);
            } 
            else
            {
              throw new OAuthException("Response body is incorrect. Can't extract a token from this: '" + response + "'", null);
            }
        }
    };
}

@Override
public OAuthService createService(OAuthConfig config)
{
  return new OAuth20HeaderServiceImpl(this, config);
}

}