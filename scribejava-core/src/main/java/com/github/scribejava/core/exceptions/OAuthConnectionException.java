package com.github.scribejava.core.exceptions;

/**
 * @author Pablo Fernandez
 */
public class OAuthConnectionException extends OAuthException {

    private static final String MSG = "There was a problem while creating a connection to the remote service: ";

    public OAuthConnectionException(final String url, final Exception e) {
        super(MSG + url, e);
    }
}
