package com.github.scribejava.core.model;

import com.github.scribejava.core.utils.Preconditions;
import java.util.Objects;

/**
 * Represents an OAuth 2 Access token.
 * <p>
 * http://tools.ietf.org/html/rfc6749#section-5.1
 *
 * @see <a href="https://tools.ietf.org/html/rfc6749#section-4.1.4">OAuth 2 Access Token Specification</a></p>
 */
public class OAuth2AccessToken extends Token {

    private static final long serialVersionUID = 8901381135476613449L;

    /**
     * access_token
     * <p>
     * REQUIRED. The access token issued by the authorization server.</p>
     */
    private String accessToken;

    /**
     * token_type
     * <p>
     * REQUIRED. The type of the token issued as described in http://tools.ietf.org/html/rfc6749#section-7.1 Value is
     * case insensitive.</p>
     */
    private String tokenType;

    /**
     * expires_in
     * <p>
     * RECOMMENDED. The lifetime in seconds of the access token. For example, the value "3600" denotes that the access
     * token will expire in one hour from the time the response was generated. If omitted, the authorization server
     * SHOULD provide the expiration time via other means or document the default value.</p>
     */
    private Integer expiresIn;

    /**
     * refresh_token
     * <p>
     * OPTIONAL. The refresh token, which can be used to obtain new access tokens using the same authorization grant as
     * described in http://tools.ietf.org/html/rfc6749#section-6</p>
     */
    private String refreshToken;

    /**
     * scope
     * <p>
     * OPTIONAL, if identical to the scope requested by the client; otherwise, REQUIRED. The scope of the access token
     * as described by http://tools.ietf.org/html/rfc6749#section-3.3</p>
     */
    private String scope;

    public OAuth2AccessToken(String accessToken) {
        this(accessToken, null);
    }

    public OAuth2AccessToken(String accessToken, String rawResponse) {
        this(accessToken, null, null, null, null, rawResponse);
    }

    public OAuth2AccessToken(String accessToken, String tokenType, Integer expiresIn, String refreshToken, String scope,
            String rawResponse) {
        super(rawResponse);
        Preconditions.checkNotNull(accessToken, "access_token can't be null");
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
        this.scope = scope;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getScope() {
        return scope;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(accessToken);
        hash = 41 * hash + Objects.hashCode(tokenType);
        hash = 41 * hash + Objects.hashCode(expiresIn);
        hash = 41 * hash + Objects.hashCode(refreshToken);
        hash = 41 * hash + Objects.hashCode(scope);
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
        final OAuth2AccessToken other = (OAuth2AccessToken) obj;
        if (!Objects.equals(accessToken, other.getAccessToken())) {
            return false;
        }
        if (!Objects.equals(tokenType, other.getTokenType())) {
            return false;
        }
        if (!Objects.equals(refreshToken, other.getRefreshToken())) {
            return false;
        }
        if (!Objects.equals(scope, other.getScope())) {
            return false;
        }
        return Objects.equals(expiresIn, other.getExpiresIn());
    }
}
