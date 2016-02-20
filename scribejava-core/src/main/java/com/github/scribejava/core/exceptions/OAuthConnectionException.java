package com.github.scribejava.core.exceptions;

public class OAuthConnectionException extends OAuthException {

    private static final long serialVersionUID = 6901269342236961310L;
    private static final String MSG = "There was a problem while creating a connection to the remote service: ";

    public OAuthConnectionException(String url, Exception e) {
        super(MSG + url, e);
    }
}
