package com.github.scribejava.core.model;

import java.io.Serializable;
import com.github.scribejava.core.utils.Preconditions;

/**
 * Represents an OAuth token (either request or access token) and its secret
 *
 * @author Pablo Fernandez
 */
public class Token implements Serializable {

    private static final long serialVersionUID = 715000866082812683L;

    private final String token;
    private final String secret;
    private final String rawResponse;

    /**
     * Default constructor
     *
     * @param token token value. Can't be null.
     * @param secret token secret. Can't be null.
     */
    public Token(final String token, final String secret) {
        this(token, secret, null);
    }

    public Token(final String token, final String secret, final String rawResponse) {
        Preconditions.checkNotNull(token, "Token can't be null");
        Preconditions.checkNotNull(secret, "Secret can't be null");

        this.token = token;
        this.secret = secret;
        this.rawResponse = rawResponse;
    }

    public String getToken() {
        return token;
    }

    public String getSecret() {
        return secret;
    }

    public String getRawResponse() {
        if (rawResponse == null) {
            throw new IllegalStateException(
                    "This token object was not constructed by ScribeJava and does not have a rawResponse");
        }
        return rawResponse;
    }

    public String getParameter(String parameter) {
        String value = null;
        for (String str : this.getRawResponse().split("&")) {
            if (str.startsWith(parameter + '=')) {
                String[] part = str.split("=");
                if (part.length > 1) {
                    value = part[1].trim();
                }
                break;
            }
        }
        return value;
    }

    @Override
    public String toString() {
        return String.format("Token[%s , %s]", token, secret);
    }

    /**
     * @return true if the token is empty (token = "", secret = "")
     */
    public boolean isEmpty() {
        return "".equals(this.token) && "".equals(this.secret);
    }

    /**
     * Factory method
     *
     * Useful for two legged OAuth.
     *
     * @return empty token (token = "", secret = "")
     */
    public static Token empty() {
        return new Token("", "");
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Token that = (Token) o;
        return token.equals(that.getToken()) && secret.equals(that.getSecret());
    }

    @Override
    public int hashCode() {
        return 31 * token.hashCode() + secret.hashCode();
    }
}
