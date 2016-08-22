package com.github.scribejava.core.exceptions;

import com.github.scribejava.core.model.OAuth2ErrorResponse;

public class OAuthRequestException extends OAuthException {

    private final OAuth2ErrorResponse error;

    public OAuthRequestException(String message, OAuth2ErrorResponse error) {
        super(message);
        this.error = error;
    }

    public OAuth2ErrorResponse getError() {
        return error;
    }
}
