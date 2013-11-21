package org.scribe.builder.api;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
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

public class BoxNetApi extends DefaultApi20 {

	private static final String AUTHORIZE_URL = "https://api.box.com/oauth2/authorize?response_type=code&client_id=%s&redirect_uri=%s";

	@Override
	public String getAccessTokenEndpoint() {
		return "https://www.box.com/api/oauth2/token";
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config) {
		return String.format(AUTHORIZE_URL, config.getApiKey(),
				OAuthEncoder.encode(config.getCallback()));
	}

	@Override
	public AccessTokenExtractor getAccessTokenExtractor() {
		return new JsonTokenExtractor();
	}

	@Override
	public Verb getAccessTokenVerb() {
		return Verb.POST;
	}
	
	@Override
	public OAuthService createService(OAuthConfig config) {
		return new BoxNetOAuth20ServiceImpl(this, config);
	}
	
	public class BoxNetOAuth20ServiceImpl extends OAuth20ServiceImpl {
		  
		  private final DefaultApi20 api;
		  private final OAuthConfig config;
		  
		  public BoxNetOAuth20ServiceImpl(DefaultApi20 api, OAuthConfig config) {
			super(api,config);
			this.api = api;
		    this.config = config;
		  }
		  
		  public Token getAccessToken(Token requestToken, Verifier verifier)
		  {	  
		    OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
			
		    request.addBodyParameter("grant_type", "authorization_code");
		    request.addBodyParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
		    request.addBodyParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
		    request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
		    request.addBodyParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
		    
		    if(config.hasScope()) request.addQuerystringParameter(OAuthConstants.SCOPE, config.getScope());
		    Response response = request.send();
		    return api.getAccessTokenExtractor().extract(response.getBody());
		  }

		
	}

}
