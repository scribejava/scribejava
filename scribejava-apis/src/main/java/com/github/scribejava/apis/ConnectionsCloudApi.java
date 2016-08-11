package com.github.scribejava.apis;

import java.security.InvalidParameterException;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.OAuth2AccessTokenExtractor;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Verb;

/**
 * ConnectionsCloudApi supports OAuth2 endpoint for IBM Connections Cloud.
 * 
 * as IBM CC has multiple datacenter and multiple endpoints an enum object is provided 
 * with the listing of allowable datacenters.
 * 
 * This API is based on BitBuckets Oauth2 API and simplifies usage of ConnectionsCloud
 * services.
 *  
 * @author daniele.vistalli@factor-y.com
 *
 */
public class ConnectionsCloudApi extends DefaultApi20 {

	public enum CC_Datacenter {
		NA,
		CE,
		AP
	}

	private String datacenterUrl = null;
	
    protected ConnectionsCloudApi(CC_Datacenter datacenter) {
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

    private static class InstanceHolderNA {
        private static final ConnectionsCloudApi INSTANCE = new ConnectionsCloudApi(CC_Datacenter.NA);
    }
    private static class InstanceHolderCE {
        private static final ConnectionsCloudApi INSTANCE = new ConnectionsCloudApi(CC_Datacenter.CE);
    }
    private static class InstanceHolderAP {
        private static final ConnectionsCloudApi INSTANCE = new ConnectionsCloudApi(CC_Datacenter.AP);
    }

    public static ConnectionsCloudApi instance(CC_Datacenter datacenter) {
    	switch (datacenter) {
		case NA:
			return InstanceHolderNA.INSTANCE;
		case CE:
			return InstanceHolderCE.INSTANCE;
		case AP:
			return InstanceHolderAP.INSTANCE;
		default:
			throw new InvalidParameterException("Datacenter name is not allowed: " + datacenter);
		}
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
