package org.scribe.oauth;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.*;

public class OAuthOdnoklassnikiServiceImpl implements OAuthService 
{

    private static final String VERSION = "2.0_Odnoklassniki";

    private DefaultApi20 api;
    private OAuthConfig config;

    public OAuthOdnoklassnikiServiceImpl(DefaultApi20 api, OAuthConfig config) 
	{
        this.api = api;
        this.config = config;
    }

    public Token getAccessToken(Token requestToken, Verifier verifier) 
	{
        OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        request.addQuerystringParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
        request.addQuerystringParameter(OAuthConstants.CODE, verifier.getValue());
        request.addQuerystringParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
        request.addQuerystringParameter("grant_type", "authorization_code");  	// It's from odnoklassniki.ru specification http://dev.odnoklassniki.ru/wiki/pages/viewpage.action?pageId=12878032
        if(config.hasScope()) request.addQuerystringParameter(OAuthConstants.SCOPE, config.getScope());
        Response response = request.send();
        return api.getAccessTokenExtractor().extract(response.getBody());

    }

  /**
   * {@inheritDoc}
   */
    public Token getRequestToken()
    {
        throw new UnsupportedOperationException("Unsupported operation, please use 'getAuthorizationUrl' and redirect your users there");
    }

  /**
   * {@inheritDoc}
   */
    public String getVersion()
    {
        return VERSION;
    }

  /**
   * {@inheritDoc}
   */
    public void signRequest(Token accessToken, OAuthRequest request)
    {
        request.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN, accessToken.getToken());
    }

  /**
   * {@inheritDoc}
   */
    public String getAuthorizationUrl(Token requestToken)
    {
        return api.getAuthorizationUrl(config);
    }
}
