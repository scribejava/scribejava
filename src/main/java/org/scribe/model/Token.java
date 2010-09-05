package org.scribe.model;

public class Token
{

  private final String token;
  private final String secret;

  public Token(String token, String secret)
  {
    this.token = token;
    this.secret = secret;
  }

  public String getToken()
  {
    return token;
  }

  public String getSecret()
  {
    return secret;
  }

  @Override
  public String toString()
  {
    return String.format("Token[%s , %s]", token, secret);
  }
}
