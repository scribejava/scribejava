package com.github.scribejava.apis.polar;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.httpclient.HttpClient;
import com.github.scribejava.core.httpclient.HttpClientConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.oauth.AccessTokenRequestParams;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.pkce.PKCE;

import java.io.OutputStream;

public class PolarOAuth20Service extends OAuth20Service {

	public PolarOAuth20Service(DefaultApi20 api, String apiKey, String apiSecret, String callback, String defaultScope, String responseType,
		OutputStream debugStream, String userAgent, HttpClientConfig httpClientConfig, HttpClient httpClient) {
		super(api, apiKey, apiSecret, callback, defaultScope, responseType, debugStream, userAgent, httpClientConfig, httpClient);
	}

	@Override
	protected OAuthRequest createAccessTokenRequest(AccessTokenRequestParams params) {
		final OAuthRequest request = new OAuthRequest(getApi().getAccessTokenVerb(), getApi().getAccessTokenEndpoint());

		getApi().getClientAuthentication().addClientAuthentication(request, getApiKey(), getApiSecret());

		request.addBodyParameter(OAuthConstants.CODE, params.getCode());
		final String callback = getCallback();
		if (callback != null) {
			request.addBodyParameter(OAuthConstants.REDIRECT_URI, callback);
		}
		request.addBodyParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.AUTHORIZATION_CODE);

		final String pkceCodeVerifier = params.getPkceCodeVerifier();
		if (pkceCodeVerifier != null) {
			request.addBodyParameter(PKCE.PKCE_CODE_VERIFIER_PARAM, pkceCodeVerifier);
		}
		if (isDebug()) {
			log("created access token request with body params [%s], query string params [%s]",
				request.getBodyParams().asFormUrlEncodedString(),
				request.getQueryStringParams().asFormUrlEncodedString());
		}
		return request;
	}
}
