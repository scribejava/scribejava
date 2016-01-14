package com.github.scribejava.core.model;

import com.github.scribejava.core.utils.Preconditions;
import java.util.Objects;

/**
 * Represents an OAuth 2 access token
 * 
 * @see <a href="https://tools.ietf.org/html/rfc6749#section-4.1.4">OAuth 2 Access Token Specification</a>
 * 
 * @author Daniel Tyreus
 */
public class OAuth2AccessToken implements AccessToken {
    
    private final String accessToken;
    private final String tokenType;
    private final String refreshToken;
    private final Long expiresIn;
    private final String rawResponse;

    public OAuth2AccessToken(String accessToken, String tokenType, String refreshToken, Long expiresIn, String rawResponse) {
        Preconditions.checkNotNull(accessToken, "Token can't be null");
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.rawResponse = rawResponse;
    }
    
    public OAuth2AccessToken(String accessToken, String rawResponse) {
        Preconditions.checkNotNull(accessToken, "Token can't be null");
        this.accessToken = accessToken;
        this.tokenType = null;
        this.refreshToken = null;
        this.expiresIn = null;
        this.rawResponse = rawResponse;
    }
    
    public OAuth2AccessToken(String accessToken) {
        Preconditions.checkNotNull(accessToken, "Token can't be null");
        this.accessToken = accessToken;
        this.tokenType = null;
        this.refreshToken = null;
        this.expiresIn = null;
        this.rawResponse = null;
    }

    @Override
    public String getToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public String getRawResponse() {
        return rawResponse;
    }

    @Override
    public boolean isEmpty() {
        if (accessToken == null) {
            return true;
        }
        return accessToken.isEmpty();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.accessToken);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OAuth2AccessToken other = (OAuth2AccessToken) obj;
        if (!Objects.equals(this.accessToken, other.accessToken)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (rawResponse != null){
            return rawResponse;
        }
        
        return "OAuth2AccessToken{" + "accessToken=" + accessToken + ", tokenType=" + tokenType + ", refreshToken=" + refreshToken + ", expiresIn=" + expiresIn + '}';
    }

}
