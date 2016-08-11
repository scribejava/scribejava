package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.OAuth2AccessTokenExtractor;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Verb;

public class ConnectionsCloudApi extends DefaultApi20 {

	public enum CC_DATACENTER {
		NA,
		CE,
		AP
	}

	private String datacenterUrl = null;
	
    protected ConnectionsCloudApi(CC_DATACENTER datacenter) {
    	switch (datacenter) {
		case CE:
			this.datacenterUrl = "https://apps.ce.collabserv.com";
			break;
		case NA:
			this.datacenterUrl = "https://apps.na.collabserv.com";
			break;
		case AP:
			this.datacenterUrl = "https://apps.ap.collabserv.com";
			break;
		default:
			throw new IllegalArgumentException("Datacenter : " + datacenter + " is not supported");
		}
    	 
    }

    private static class InstanceHolder {
        private static final ConnectionsCloudApi INSTANCE = new ConnectionsCloudApi(CC_DATACENTER.CE);
    }

    public static ConnectionsCloudApi instance(CC_DATACENTER datacenter) {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return datacenterUrl + "/manage/oauth2/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return datacenterUrl + "/manage/oauth2/authorize";
    }

    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return OAuth2AccessTokenExtractor.instance();
    }
}
