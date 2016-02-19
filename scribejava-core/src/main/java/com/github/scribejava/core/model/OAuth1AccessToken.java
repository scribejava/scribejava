package com.github.scribejava.core.model;

import java.util.Objects;

/**
 * Represents an OAuth 1 Access Token http://oauth.net/core/1.0a/#rfc.section.6.3.2
 */
public class OAuth1AccessToken extends OAuth1Token {

    private static final long serialVersionUID = -8784937061938486135L;

    public OAuth1AccessToken(String token, String tokenSecret) {
        this(token, tokenSecret, null);
    }

    public OAuth1AccessToken(String token, String tokenSecret, String rawResponse) {
        super(token, tokenSecret, rawResponse);
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

    @Override
    public String toString() {
        return "OAuth1AccessToken{"
                + "oauth_token=" + getToken()
                + ", oauth_token_secret=" + getTokenSecret() + '}';
    }
}
