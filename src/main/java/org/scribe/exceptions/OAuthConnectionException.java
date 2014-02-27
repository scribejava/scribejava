package org.scribe.exceptions;

/**
 * @author Pablo Fernandez
 */
public class OAuthConnectionException extends OAuthException {

    private static final String MSG = "There was a problem while creating a connection to the remote service.";

    public OAuthConnectionException(final Exception e) {
        super(MSG, e);
    }
}
