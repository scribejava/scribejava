package com.github.scribejava.core.model;

import java.util.Objects;

/**
 * Represents an OAuth 1 Request Token http://tools.ietf.org/html/rfc5849#section-2.1
 */
public class OAuth1RequestToken extends OAuth1Token {

    private static final long serialVersionUID = 6185104114662587991L;

    /**
     * oauth_callback_confirmed:
     * <p>
     * MUST be present and set to "true". The parameter is used to differentiate from previous versions of the protocol.
     * </p>
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

    /**
     * The temporary credentials identifier.
     *
     * @return oauth_token
     */
    @Override
    public String getToken() {
        return super.getToken();
    }

    /**
     * The temporary credentials shared-secret.
     *
     * @return oauth_token_secret
     */
    @Override
    public String getTokenSecret() {
        return super.getTokenSecret();
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
}
