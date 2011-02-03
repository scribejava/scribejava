package org.scribe.model;

import static org.junit.Assert.*;

import java.net.MalformedURLException;

import org.junit.*;

public class OAuthRequestTest
{

  private OAuthRequest request;

  @Before
  public void setup() throws MalformedURLException
  {
    request = new OAuthRequest(Verb.GET, "http://example.com");
  }

  @Test
  public void shouldAddOAuthParamters()
  {
    request.addOAuthParameter(OAuthConstants.TOKEN, "token");
    request.addOAuthParameter(OAuthConstants.NONCE, "nonce");
    request.addOAuthParameter(OAuthConstants.TIMESTAMP, "ts");
    request.addOAuthParameter(OAuthConstants.SCOPE, "feeds");

    assertEquals(4, request.getOauthParameters().size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionIfParameterIsNotOAuth()
  {
    request.addOAuthParameter("otherParam", "value");
  }
}
