package com.github.scribejava.core.model;

import com.github.scribejava.core.oauth2.OAuth2Error;
import java.io.IOException;

import java.net.URI;

/**
 * Representing <a href="https://tools.ietf.org/html/rfc6749#section-5.2">"5.2. Error Response"</a>
 */
public class OAuth2AccessTokenErrorResponse extends OAuthResponseException {

    private static final long serialVersionUID = 2309424849700276816L;

    private final OAuth2Error error;
    private final String errorDescription;
    private final URI errorUri;

    public OAuth2AccessTokenErrorResponse(OAuth2Error error, String errorDescription, URI errorUri,
            Response rawResponse) throws IOException {
        super(rawResponse);
        this.error = error;
        this.errorDescription = errorDescription;
        this.errorUri = errorUri;
    }

    public OAuth2Error getError() {
        return error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public URI getErrorUri() {
        return errorUri;
    }

}
