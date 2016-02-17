package com.github.scribejava.core.model;

/**
 * Represents an OAuth 1 Access Token
 */
public class OAuth1AccessToken extends OAuth1Token {

    private static final long serialVersionUID = 1344500144115482404L;

    public OAuth1AccessToken(String token, String secret) {
        this(token, secret, null);
    }

    public OAuth1AccessToken(String token, String secret, String rawResponse) {
        super(token, secret, rawResponse);
    }
}
