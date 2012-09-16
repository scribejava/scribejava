package org.scribe.exceptions;

/**
 * @author: Pablo Fernandez
 */
public class OAuthConnectionException extends OAuthException
{

  private static final long serialVersionUID = 8845426292529379604L;

  private static final String MSG = "There was a problem while creating a connection to the remote service.";

  public OAuthConnectionException(Exception e)
  {
    super(MSG, e);
  }
}
