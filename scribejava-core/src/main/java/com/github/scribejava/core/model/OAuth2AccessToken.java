package com.github.scribejava.core.model;

/**
 * Represents an OAuth 2 Access Token
 */
public class OAuth2AccessToken extends Token {

    private static final long serialVersionUID = -7450991088697660741L;

    public OAuth2AccessToken(String token) {
        super(token, null);
    }

    public OAuth2AccessToken(String token, String rawResponse) {
        super(token, rawResponse);
    }

}
