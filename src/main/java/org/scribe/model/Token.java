package org.scribe.model;

import java.io.Serializable;

/**
 * Represents an OAuth token (either request or access token) and its secret
 * 
 * @author Pablo Fernandez
 */
public class Token implements Serializable
{
  private static final long serialVersionUID = 715000866082812683L;
	
  private final String token;
  private final String secret;
  private final String rawResponse;

  /**
   * Default constructor
   * 
   * @param token token value
   * @param secret token secret
   */
  public Token(String token, String secret)
  {
    this(token, secret, null);
  }

  public Token(String token, String secret, String rawResponse)
  {
    this.token = token;
    this.secret = secret;
    this.rawResponse = rawResponse;
  }

  public String getToken()
  {
    return token;
  }

  public String getSecret()
  {
    return secret;
  }

  public String getRawResponse()
  {
    if (rawResponse == null)
    {
      throw new IllegalStateException("This token object was not constructed by scribe and does not have a rawResponse");
    }
    return rawResponse;
  }

  @Override
  public String toString()
  {
    return String.format("Token[%s , %s]", token, secret);
  }

  /**
   * Returns true if the token is empty (token = "", secret = "")
   */
  public boolean isEmpty()
  {
    return "".equals(this.token) && "".equals(this.secret);
  }

  /**
   * Factory method that returns an empty token (token = "", secret = "").
   *
   * Useful for two legged OAuth.
   */
  public static Token empty()
  {
    return new Token("","");
  }
}
