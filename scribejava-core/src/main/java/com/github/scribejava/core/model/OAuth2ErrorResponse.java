package com.github.scribejava.core.model;

import com.github.scribejava.core.exceptions.OAuthException;

import java.net.URI;

/**
 * Representing <a href="https://tools.ietf.org/html/rfc6749#section-5.2">"5.2. Error Response"</a>
 */
public class OAuth2ErrorResponse extends OAuthException {

    public enum OAuthError {
        invalid_request, invalid_client, invalid_grant, unauthorized_client, unsupported_grant_type, invalid_scope
    }

    private final OAuthError error;
    private final String errorDescription;
    private final URI errorUri;
    private final String rawResponse;

    public OAuth2ErrorResponse(OAuthError error, String errorDescription, URI errorUri, String rawResponse) {
        super(generateMessage(error, errorDescription, errorUri, rawResponse));
        if (error == null) {
            throw new IllegalArgumentException("error must not be null");
        }
        this.error = error;
        this.errorDescription = errorDescription;
        this.errorUri = errorUri;
        this.rawResponse = rawResponse;
    }

    private static String generateMessage(OAuthError error, String errorDescription, URI errorUri, String rawResponse) {
        return rawResponse;
    }

    public OAuthError getError() {
        return error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public URI getErrorUri() {
        return errorUri;
    }

    public String getRawResponse() {
        return rawResponse;
    }
}
