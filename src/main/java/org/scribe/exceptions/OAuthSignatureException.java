package org.scribe.exceptions;

public class OAuthSignatureException extends OAuthException
{
  private static final long serialVersionUID = 1L;
  
  public OAuthSignatureException(String stringToSign, Exception e)
  {
    super("Error while signing string: " + stringToSign, e);
  }

}
