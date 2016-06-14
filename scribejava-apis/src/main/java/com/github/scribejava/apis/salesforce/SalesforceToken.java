package com.github.scribejava.apis.salesforce;

import com.github.scribejava.core.model.OAuth2AccessToken;
import java.util.Objects;

public class SalesforceToken extends OAuth2AccessToken {

    private static final long serialVersionUID = 7845679917727899612L;

    /**
	 * This token model includes the instance_url to address the needed Salesforce organization instance.
     */
    private final String instanceUrl;

    public SalesforceToken(String accessToken, String instanceUrl, String rawResponse) {
        this(accessToken, null, null, null, null, instanceUrl, rawResponse);
    }

    public SalesforceToken(String accessToken, String tokenType, Integer expiresIn, String refreshToken, String scope,
            String instanceUrl, String rawResponse) {
        super(accessToken, tokenType, expiresIn, refreshToken, scope, rawResponse);
        this.instanceUrl = instanceUrl;
    }

    public String getInstanceUrl() {
        return instanceUrl;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(getAccessToken());
        hash = 37 * hash + Objects.hashCode(getTokenType());
        hash = 37 * hash + Objects.hashCode(getExpiresIn());
        hash = 37 * hash + Objects.hashCode(getRefreshToken());
        hash = 37 * hash + Objects.hashCode(getScope());
        hash = 37 * hash + Objects.hashCode(getInstanceUrl());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SalesforceToken other = (SalesforceToken) obj;
        if (!Objects.equals(getAccessToken(), other.getAccessToken())) {
            return false;
        }
        if (!Objects.equals(getTokenType(), other.getTokenType())) {
            return false;
        }
        if (!Objects.equals(getRefreshToken(), other.getRefreshToken())) {
            return false;
        }
        if (!Objects.equals(getScope(), other.getScope())) {
            return false;
        }
        if (!Objects.equals(getInstanceUrl(), other.getInstanceUrl())) {
            return false;
        }
        return Objects.equals(getExpiresIn(), other.getExpiresIn());
    }

    @Override
    public String toString() {
        return "SalesforceToken{"
                + "access_token=" + getAccessToken()
                + ", token_type=" + getTokenType()
                + ", expires_in=" + getExpiresIn()
                + ", refresh_token=" + getRefreshToken()
                + ", scope=" + getScope()
                + ", instance_url=" + getInstanceUrl() + '}';
    }
}
