package org.scribe.builder.api;

import org.scribe.model.Token;

/**
 * Class to support integration with Withings APi
 * Created by tvoet on 2/13/2014.
 */
public class WithingsApi extends DefaultApi10a {
    private static final String AUTHORIZE_URL = "https://oauth.withings.com/account/authorize?oauth_token=%s";
    private static final String REQUEST_TOKEN_RESOURCE = "https://oauth.withings.com/account/request_token";
    private static final String ACCESS_TOKEN_RESOURCE = "https://oauth.withings.com/account/access_token";

    @Override
    public String getAccessTokenEndpoint()
    {
        return ACCESS_TOKEN_RESOURCE;
    }

    @Override
    public String getRequestTokenEndpoint()
    {
        return REQUEST_TOKEN_RESOURCE;
    }

    @Override
    public String getAuthorizationUrl(Token requestToken)
    {
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }
}
