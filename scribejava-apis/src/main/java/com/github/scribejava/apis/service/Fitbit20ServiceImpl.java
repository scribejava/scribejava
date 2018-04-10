package com.github.scribejava.apis.service;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.java8.Base64;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.oauth.OAuth20Service;

import java.nio.charset.Charset;

/**
 * Created by hd on 10/09/16.
 * 
 * Note - this is an updated version of this library for Scribe v5.3.0. Original code here:
 * 		- https://github.com/alexthered/fitbitAPI20-scribe-java
 */
public class Fitbit20ServiceImpl extends OAuth20Service {

    public Fitbit20ServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
    }

    /**
     * ref: https://dev.fitbit.com/docs/oauth2/#access-token-request
     * @param code
     * @param request
     * @param <T>
     * @return
     */
    @Override
    protected OAuthRequest createAccessTokenRequest(String code)
    {
        final DefaultApi20 api = getApi();
        final OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        final OAuthConfig config = getConfig();
        request.addParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        request.addParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
        request.addParameter(OAuthConstants.CODE, code);
        request.addParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
        String scope = config.getScope();
        if (scope != null) {
            request.addParameter(OAuthConstants.SCOPE, scope);
        }
        
		//this is non-OAuth2 standard, but Fitbit requires it
		request.addHeader("Authorization", "Basic " + getKeyBytesForFitbitAuth());

        request.addParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.AUTHORIZATION_CODE);
        return request;
    }

    /**
     * ref: https://dev.fitbit.com/docs/oauth2/#refreshing-tokens
     * @param refreshToken
     * @param request
     * @param <T>
     * @return
     */
    @Override
    protected OAuthRequest createRefreshTokenRequest(String refreshToken)
    {
        if (refreshToken != null && !refreshToken.isEmpty()) {
        		final DefaultApi20 api = getApi();
        		final OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        		final OAuthConfig config = this.getConfig();
        		request.addParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        		request.addParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
        		request.addParameter(OAuthConstants.REFRESH_TOKEN, refreshToken);
        		request.addParameter(OAuthConstants.GRANT_TYPE, OAuthConstants.REFRESH_TOKEN);

        		//this is non-OAuth2 standard, but Fitbit requires it
        		request.addHeader("Authorization", "Basic " + getKeyBytesForFitbitAuth());

            return request;
        } else {
            throw new IllegalArgumentException("The refreshToken cannot be null or empty");
        }
    }

    /**
     */
    	private String getKeyBytesForFitbitAuth()
    	{
    		final OAuthConfig config = getConfig();
    		String keyAndSecret = String.format("%s:%s", new Object[] {config.getApiKey(), config.getApiSecret()});
        byte[] keyBytes = Base64.getEncoder().encode(keyAndSecret.getBytes(Charset.forName("UTF-8")));
        
        return new String(keyBytes);
    	}
}
