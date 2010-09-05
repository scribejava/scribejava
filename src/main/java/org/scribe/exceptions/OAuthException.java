package org.scribe.exceptions;

public class OAuthException extends RuntimeException
{

  public OAuthException(String message, Exception e)
  {
    super(message, e);
  }

  public OAuthException(String message)
  {
    super(message, null);
  }

  private static final long serialVersionUID = 1L;

}
