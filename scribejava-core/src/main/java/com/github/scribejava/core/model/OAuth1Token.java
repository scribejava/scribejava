package com.github.scribejava.core.model;

import com.github.scribejava.core.utils.Preconditions;
import java.util.Objects;

/**
 * Represents an abstract OAuth 1 Access Token (either request or access token)
 */
public abstract class OAuth1Token extends Token {

    private static final long serialVersionUID = -2591380053073767654L;

    private final String secret;

    public OAuth1Token(String token, String secret, String rawResponse) {
        super(token, rawResponse);
        Preconditions.checkNotNull(secret, "Secret can't be null");
        this.secret = secret;
    }

    public String getSecret() {
        return secret;
    }

    @Override
    public String toString() {
        return String.format("Token[%s , %s]", getToken(), secret);
    }

    /**
     * @return true if the token is empty (token = "", secret = "")
     */
    public boolean isEmpty() {
        return "".equals(this.getToken()) && "".equals(this.secret);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(secret);
        hash = 71 * hash + Objects.hashCode(getToken());
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
        final OAuth1Token other = (OAuth1Token) obj;
        if (!Objects.equals(secret, other.getSecret())) {
            return false;
        }
        return Objects.equals(getToken(), other.getToken());
    }
}
