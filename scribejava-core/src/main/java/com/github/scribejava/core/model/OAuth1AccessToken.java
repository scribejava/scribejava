package com.github.scribejava.core.model;

import java.util.Objects;

/**
 * Represents an OAuth 1 Access Token http://tools.ietf.org/html/rfc5849#section-2.3
 */
public class OAuth1AccessToken extends OAuth1Token {

    private static final long serialVersionUID = -103999293167210966L;

    public OAuth1AccessToken(String token, String tokenSecret) {
        this(token, tokenSecret, null);
    }

    public OAuth1AccessToken(String token, String tokenSecret, String rawResponse) {
        super(token, tokenSecret, rawResponse);
    }

    /**
     * The token identifier.
     *
     * @return oauth_token
     */
    @Override
    public String getToken() {
        return super.getToken();
    }

    /**
     * The token shared-secret.
     *
     * @return oauth_token_secret
     */
    @Override
    public String getTokenSecret() {
        return super.getTokenSecret();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(getToken());
        hash = 73 * hash + Objects.hashCode(getTokenSecret());
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
        final OAuth1AccessToken other = (OAuth1AccessToken) obj;
        if (!Objects.equals(getToken(), other.getToken())) {
            return false;
        }
        return Objects.equals(getTokenSecret(), other.getTokenSecret());
    }
}
