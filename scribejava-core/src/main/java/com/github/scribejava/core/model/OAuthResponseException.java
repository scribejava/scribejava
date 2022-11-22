package com.github.scribejava.core.model;

import com.github.scribejava.core.exceptions.OAuthException;
import java.io.IOException;
import java.util.Objects;

public class OAuthResponseException extends OAuthException {

    private static final long serialVersionUID = 1309424849700276816L;

    private final transient Response response;

    public OAuthResponseException(Response rawResponse) throws IOException {
        super(rawResponse.getBody());
        this.response = rawResponse;
    }

    public Response getResponse() {
        return response;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(response);
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
        final OAuthResponseException other = (OAuthResponseException) obj;
        return Objects.equals(this.response, other.response);
    }

}
