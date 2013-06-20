package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.*;
import org.scribe.utils.OAuthEncoder;

public class YammerApi20 extends DefaultApi20
{
    private static final String AUTHORIZE_URL = "https://www.yammer.com/dialog/oauth?client_id=%s&redirect_uri=%s";

    @Override
    public AccessTokenExtractor getAccessTokenExtractor()
    {
      return new JsonTokenExtractor("\"token\":\\s*\"(\\S*?)\"");
    }

    @Override
    public String getAccessTokenEndpoint()
    {
      return "https://www.yammer.com/oauth2/access_token.json";
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config)
    {
      return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
    }
}
