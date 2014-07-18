package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

public class ProducteevApi extends DefaultApi20 {

    private static final String AUTHORIZE_URL = "https://www.producteev.com/oauth/v2/auth?client_id=%s&response_type=code&redirect_uri=%s";

    @Override
    public String getAccessTokenEndpoint()
    {
      return "https://www.producteev.com/oauth/v2/token?grant_type=authorization_code";
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config)
    {
      Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. Producteev does not support OOB");

      return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
    }

    /**
     * Returns the access token extractor.
     *
     * @return access token extractor
     */
    public AccessTokenExtractor getAccessTokenExtractor()
    {
      return new JsonTokenExtractor();
    }
}
