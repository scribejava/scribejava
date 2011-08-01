package org.scribe.oauth;

import java.util.Map;

import org.apache.log4j.Logger;
import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verifier;


/**
 * OAuth 1.0a implementation of {@link OAuthService}
 * 
 * @author Pablo Fernandez
 */
public class OAuth10aServiceImpl implements OAuthService
{
	private static final String VERSION = "1.0";
	
	private final OAuthConfig config;
	private final DefaultApi10a api;

	static Logger				logger	= Logger.getLogger("YahooAuthorization");

	/**
	 * Default constructor
	 * 
	 * @param api OAuth1.0a api information
	 * @param config OAuth 1.0a configuration param object
	 */
	public OAuth10aServiceImpl(DefaultApi10a api, OAuthConfig config)
	{

		this.api = api;
		this.config = config;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Token getAccessToken(Token requestToken, Verifier verifier)
	{
		OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
		request.addOAuthParameter(OAuthConstants.TOKEN, requestToken.getToken());
		request.addOAuthParameter(OAuthConstants.VERIFIER, verifier.getValue());
		addOAuthParams(request, requestToken);
		addSignature(request);
		Response response = request.send();
		return api.getAccessTokenExtractor().extract(response.getBody());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAuthorizationUrl(Token requestToken)
	{
		return api.getAuthorizationUrl(requestToken);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Token getRequestToken()
	{

		System.out.println("Got token from OAuth10aServiceImpl.java");
		OAuthRequest request = new OAuthRequest(api.getRequestTokenVerb(), api.getRequestTokenEndpoint());
		request.addOAuthParameter(OAuthConstants.CALLBACK, config.getCallback());
		addOAuthParams(request, OAuthConstants.EMPTY_TOKEN);
		addSignature(request);
		Response response = request.send();
		return api.getRequestTokenExtractor().extract(response.getBody());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getVersion()
	{
		return VERSION;
	}

	@Override
	public Token refreshToken(Token expiredToken, String sessionHandle) {
		OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
		request.addOAuthParameter(OAuthConstants.TOKEN, expiredToken.getToken());
		request.addOAuthParameter("oauth_session_handle", sessionHandle);

		logger.debug("[REQUEST]: " + request);
		logger.debug("[ExpiredToken] : " + expiredToken);
		addOAuthParams(request, expiredToken);

		addSignature(request);
		Response response = request.send();
		return api.getRequestTokenExtractor().extract(response.getBody());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void signRequest(Token token, OAuthRequest request)
	{
		request.addOAuthParameter(OAuthConstants.TOKEN, token.getToken());
		addOAuthParams(request, token);
		addSignature(request);
	}

	private void addOAuthParams(OAuthRequest request, Token token)
	{
		request.addOAuthParameter(OAuthConstants.TIMESTAMP, api.getTimestampService().getTimestampInSeconds());
		request.addOAuthParameter(OAuthConstants.NONCE, api.getTimestampService().getNonce());
		request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, config.getApiKey());
		request.addOAuthParameter(OAuthConstants.SIGN_METHOD, api.getSignatureService().getSignatureMethod());
		request.addOAuthParameter(OAuthConstants.VERSION, getVersion());
		if(config.hasScope()) {
			request.addOAuthParameter(OAuthConstants.SCOPE, config.getScope());
		}
		request.addOAuthParameter(OAuthConstants.SIGNATURE, getSignature(request, token));
	}

	private void addSignature(OAuthRequest request)
	{
		switch (config.getSignatureType())
		{
			case Header:
				String oauthHeader = api.getHeaderExtractor().extract(request);
				request.addHeader(OAuthConstants.HEADER, oauthHeader);
				break;
			case QueryString:
				for (Map.Entry<String, String> entry : request.getOauthParameters().entrySet())
				{
					request.addQuerystringParameter(entry.getKey(), entry.getValue());
				}
				break;
		}
	}

	private String getSignature(OAuthRequest request, Token token)
	{

		String baseString = api.getBaseStringExtractor().extract(request);

		return api.getSignatureService().getSignature(baseString, config.getApiSecret(), token.getSecret());
	}

}
