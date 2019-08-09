package com.github.scribejava.core.model;

import com.github.scribejava.core.utils.Preconditions;

/**
 * Represents an abstract OAuth 1 Token (either request or access token)
 */
public abstract class OAuth1Token extends Token {

    private static final long serialVersionUID = 6285873427974823019L;

    private final String token;

    private final String tokenSecret;

    public OAuth1Token(String token, String tokenSecret, String rawResponse) {
        super(rawResponse);
        Preconditions.checkNotNull(token, "oauth_token can't be null");
        Preconditions.checkNotNull(tokenSecret, "oauth_token_secret can't be null");
        this.token = token;
        this.tokenSecret = tokenSecret;
    }

    public String getToken() {
        return token;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    /**
     * @return true if the token is empty (oauth_token = "", oauth_token_secret = "")
     */
    public boolean isEmpty() {
        return "".equals(token) && "".equals(tokenSecret);
    }
}
