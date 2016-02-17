package com.github.scribejava.core.model;

/**
 * Represents an OAuth 1 Abstract Token
 */
public class OAuth1RequestToken extends OAuth1Token {

    private static final long serialVersionUID = 3910105236810537839L;

    public OAuth1RequestToken(String token, String secret) {
        this(token, secret, null);
    }

    public OAuth1RequestToken(String token, String secret, String rawResponse) {
        super(token, secret, rawResponse);
    }
}
