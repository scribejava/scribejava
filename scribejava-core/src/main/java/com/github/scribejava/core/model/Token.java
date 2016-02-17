package com.github.scribejava.core.model;

import java.io.Serializable;
import com.github.scribejava.core.utils.Preconditions;
import java.util.Objects;

/**
 * Represents an abstract OAuth (1 and 2) token (either request or access token)
 */
public abstract class Token implements Serializable {

    private static final long serialVersionUID = 777818051043452947L;

    private final String token;
    private final String rawResponse;

    public Token(String token, String rawResponse) {
        Preconditions.checkNotNull(token, "Token can't be null");
        this.token = token;
        this.rawResponse = rawResponse;
    }

    public String getToken() {
        return token;
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
                final String[] part = str.split("=");
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
        return String.format("Token[%s]", token);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(token);
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
        final Token other = (Token) obj;
        return Objects.equals(token, other.getToken());
    }
}
