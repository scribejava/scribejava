package org.scribe.builder.api;

import org.scribe.extractors.*;
import org.scribe.model.*;
import org.scribe.oauth.OAuth20ServiceImpl;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.*;

public class EventBriteApi extends DefaultApi20
{
  private static final String AUTHORIZATION_URL = "https://www.eventbrite.com/oauth/authorize?client_id=%s&response_type=code&set_mobile=on&redirect_uri=%s";
  private static final String ACCESS_URL = "https://www.eventbrite.com/oauth/token?client_id=%s&redirect_uri=%s&client_secret=%s&grant_type=authorization_code";
  
  OAuthConfig config;
  
  @Override
  public String getAccessTokenEndpoint()
  {
	  if (config!=null)
	  {
		  return String.format(ACCESS_URL, config.getApiKey(),OAuthEncoder.encode(config.getCallback()),config.getApiSecret());   
	  }
	  return ACCESS_URL;
  }

  @Override
  public String getAuthorizationUrl(OAuthConfig config)
  {
    Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. EventBrite does not support OOB");
    this.config = config;
    return String.format(AUTHORIZATION_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
  }

  @Override
  public AccessTokenExtractor getAccessTokenExtractor()
  {
    return new JsonTokenExtractor();
  }
  
  @Override
  public Verb getAccessTokenVerb()
  {
    return Verb.POST;
  }
  
  @Override
  public OAuthService createService(OAuthConfig config)
  {
    return new EventBriteService(this, config);
  }
  
	public class EventBriteService extends OAuth20ServiceImpl
	{
		private final DefaultApi20 api;

		public EventBriteService(DefaultApi20 api, OAuthConfig config)
		{
			super(api, config);
			this.api = api;
		}

		public Token getAccessToken(Token requestToken, Verifier verifier)
		{
			OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(),api.getAccessTokenEndpoint());
			request.addBodyParameter(OAuthConstants.CLIENT_ID,config.getApiKey());
			request.addBodyParameter(OAuthConstants.CLIENT_SECRET,config.getApiSecret());
			request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
			request.addBodyParameter("grant_type", "authorization_code");
			request.addBodyParameter(OAuthConstants.REDIRECT_URI,config.getCallback());
			if (config.hasScope())request.addBodyParameter(OAuthConstants.SCOPE,config.getScope());
			Response response = request.send();
			return api.getAccessTokenExtractor().extract(response.getBody());
		}
		
		public void signRequest(Token accessToken, OAuthRequest request)
	    {
			request.addHeader("Authorization","Bearer "+accessToken.getToken());
	    }
	}
}
