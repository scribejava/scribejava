package org.scribe.exceptions;

/**
 * Specialized exception that represents a problem in the signature
 * 
 * @author Pablo Fernandez
 */
public class OAuthSignatureException extends OAuthException
{
  private static final long serialVersionUID = 1L;
  
  /**
   * Default constructor
   * 
   * @param stringToSign plain string that gets signed (HMAC-SHA, etc)
   * @param e original exception
   */
  public OAuthSignatureException(String stringToSign, Exception e)
  {
    super("Error while signing string: " + stringToSign, e);
  }

}
