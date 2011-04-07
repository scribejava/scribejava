package org.scribe.builder.api;

import org.scribe.model.OAuthConfig;
import org.scribe.model.Token;
import org.scribe.oauth.OAuth10aServiceImpl;
import org.scribe.oauth.OAuthService;

public class QqWeiboApi extends DefaultApi10a {

	private static final String REQUEST_TOKEN_URL = "https://open.t.qq.com/cgi-bin/request_token";
	private static final String ACCESS_TOKEN_URL = "https://open.t.qq.com/cgi-bin/access_token";
	private static final String AUTHORIZE_URL = "https://open.t.qq.com/cgi-bin/authorize?oauth_token=%s";

	@Override
	public String getRequestTokenEndpoint() {
		return REQUEST_TOKEN_URL;
	}

	@Override
	public String getAccessTokenEndpoint() {
		return ACCESS_TOKEN_URL;
	}

	@Override
	public String getAuthorizationUrl(Token requestToken) {
		return String.format(AUTHORIZE_URL, requestToken.getToken());
	}

	@Override
	public OAuthService createService(OAuthConfig config, String scope) {
		OAuthService service = doCreateService(config);
		service.addScope(scope);
		return service;
	}

	private OAuthService doCreateService(OAuthConfig config) {
		return new OAuth10aServiceImpl(this, config, true);
	}

}