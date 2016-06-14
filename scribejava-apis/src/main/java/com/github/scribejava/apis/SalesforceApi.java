package com.github.scribejava.apis;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import com.github.scribejava.apis.salesforce.SalesforceJsonTokenExtractor;

/**
 * This class is an implementation of the Salesforce OAuth2 API. 
 */

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Verb;

public class SalesforceApi extends DefaultApi20 {
	
	// Constants
    private static final String BASE_URL = "https://login.salesforce.com/services/oauth2";
    private static final String ACCESS_URL = BASE_URL + "/token?grant_type=authorization_code";
    private static final String AUTHORIZE_URL = BASE_URL + "/authorize";

    protected SalesforceApi() {
    	SSLContext sc;
		try {
			String protocol = SSLContext.getDefault().getProtocol();
			if (!protocol.equals("TLSv1.2")) { // This is important!
				sc = SSLContext.getInstance("TLSv1.2");
				sc.init(null, null, null);
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());				
			}
		} catch (Exception e) {
			throw new  IllegalStateException("Unexpected JVM without TLS 1.2 support", e);
		} 
    }

    private static class InstanceHolder {
        private static final SalesforceApi INSTANCE = new SalesforceApi();
    }

    public static SalesforceApi instance() {
        return InstanceHolder.INSTANCE;
    }
    
    @Override
    public Verb getAccessTokenVerb(){
          return Verb.POST;
    }

	@Override
	public String getAccessTokenEndpoint() {
		return ACCESS_URL;
	}

	@Override
	protected String getAuthorizationBaseUrl() {
		return AUTHORIZE_URL;
	}
	
    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return SalesforceJsonTokenExtractor.instance();
    }

}
