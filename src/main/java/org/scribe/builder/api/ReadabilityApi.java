package org.scribe.builder.api;

import org.scribe.model.Token;

public class ReadabilityApi extends DefaultApi10a
{
    private static final String AUTHORIZE_URL = "https://www.readability.com/api/rest/v1/oauth/authorize/";
    private static final String REQUEST_TOKEN_URL = "https://www.readability.com/api/rest/v1/oauth/request_token/";
    private static final String ACCESS_TOKEN_URL = "https://www.readability.com/api/rest/v1/oauth/access_token/";

    @Override
    public String getRequestTokenEndpoint()
    {
        return REQUEST_TOKEN_URL;
    }

    @Override
    public String getAccessTokenEndpoint()
    {
        return ACCESS_TOKEN_URL;
    }

    @Override
    public String getAuthorizationUrl(Token requestToken)
    {
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }
}