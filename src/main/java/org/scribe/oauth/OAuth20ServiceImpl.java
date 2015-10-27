package org.scribe.oauth;

import java.nio.charset.Charset;

import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.services.Base64Encoder;

public class OAuth20ServiceImpl implements OAuthService
{
  private static final String VERSION = "2.0";
  
  private final DefaultApi20 api;
  private final OAuthConfig config;
  
  /**
   * Default constructor
   * 
   * @param api OAuth2.0 api information
   * @param config OAuth 2.0 configuration param object
   */
  public OAuth20ServiceImpl(DefaultApi20 api, OAuthConfig config)
  {
    this.api = api;
    this.config = config;
  }

  /**
   * {@inheritDoc}
   */
  public Token getAccessToken(Token requestToken, Verifier verifier)
  {
	Verb verb = api.getAccessTokenVerb();
	OAuthRequest request = new OAuthRequest(verb, api.getAccessTokenEndpoint());
	Response response;
	switch (api.getClientAuthenticationType()) {
	case Header :
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Basic ");
		stringBuilder.append(Base64Encoder.getInstance().encode((config.getApiKey() + ":" + config.getApiSecret()).getBytes(Charset.forName("UTF-8"))));
		request.addHeader("Authorization", stringBuilder.toString());
		break;
	
	case PostForm :
		if(verb!=Verb.POST && verb!=Verb.PUT) throw new IllegalArgumentException("Impossible to set parameter in a form with an other HTTP method than POST or PUT");
		request.addBodyParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
		request.addBodyParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
		break;
		
	case QueryString :
		request.addQuerystringParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
		request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
		break;
	}
	
	switch (api.getParameterType()) {
	case Header :
		request.addHeader(OAuthConstants.CLIENT_ID, config.getApiKey());
		request.addHeader(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
		request.addHeader(OAuthConstants.CODE, verifier.getValue());
		request.addHeader(OAuthConstants.REDIRECT_URI, config.getCallback());
		if (config.hasScope()) request.addHeader(OAuthConstants.SCOPE, config.getScope());
		break;
	
	case PostForm :
		if(verb!=Verb.POST && verb!=Verb.PUT) throw new IllegalArgumentException("Impossible to set parameter in a form with an other HTTP method than POST or PUT");
		request.addBodyParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.AUTHORIZATION_CODE);
		request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
		request.addBodyParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
		request.addBodyParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
		if (config.hasScope()) request.addBodyParameter(OAuthConstants.SCOPE, config.getScope());
		break;
		
	case QueryString :
		request.addQuerystringParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
		request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
		request.addQuerystringParameter(OAuthConstants.CODE, verifier.getValue());
		request.addQuerystringParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
		if (config.hasScope()) request.addQuerystringParameter(OAuthConstants.SCOPE, config.getScope());
		break;
	}
	response = api.handleRequest(request).send();
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
