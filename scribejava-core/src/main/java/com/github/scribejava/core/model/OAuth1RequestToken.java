package com.github.scribejava.core.model;

import java.util.Objects;

/**
 * Represents an OAuth 1 Request Token http://oauth.net/core/1.0a/#rfc.section.6.1.2
 */
public class OAuth1RequestToken extends OAuth1Token {

    private static final long serialVersionUID = 359527630020350893L;

    /**
     * oauth_callback_confirmed:
     * <p>
     * MUST be present and set to true. The Consumer MAY use this to confirm that the Service Provider received the
     * callback value.</p>
     */
    private final boolean oauthCallbackConfirmed;

    public OAuth1RequestToken(String token, String tokenSecret) {
        this(token, tokenSecret, null);
    }

    public OAuth1RequestToken(String token, String tokenSecret, String rawResponse) {
        this(token, tokenSecret, true, rawResponse);
    }

    public OAuth1RequestToken(String token, String tokenSecret, boolean oauthCallbackConfirmed, String rawResponse) {
        super(token, tokenSecret, rawResponse);
        this.oauthCallbackConfirmed = oauthCallbackConfirmed;
    }

    public boolean isOauthCallbackConfirmed() {
        return oauthCallbackConfirmed;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(getToken());
        hash = 83 * hash + Objects.hashCode(getTokenSecret());
        hash = 83 * hash + (oauthCallbackConfirmed ? 1 : 0);
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
        final OAuth1RequestToken other = (OAuth1RequestToken) obj;
        if (oauthCallbackConfirmed != other.isOauthCallbackConfirmed()) {
            return false;
        }
        if (!Objects.equals(getToken(), other.getToken())) {
            return false;
        }
        return Objects.equals(getTokenSecret(), other.getTokenSecret());
    }

    @Override
    public String toString() {
        return "OAuth1RequestToken{"
                + "oauth_token=" + getToken()
                + ", oauth_token_secret=" + getTokenSecret()
                + ", oauthCallbackConfirmed=" + oauthCallbackConfirmed + '}';
    }
}
