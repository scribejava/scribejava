package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.*;
import org.scribe.model.Request.*;

public class OAuthRequestTest
{

  private OAuthRequest request;

  @Before
  public void setup()
  {
    request = new OAuthRequest(Verb.GET, "http://example.com");
  }

  @Test
  public void shouldAddOAuthParamters()
  {
    request.addOAuthParameter(OAuthConstants.TOKEN, "token");
    request.addOAuthParameter(OAuthConstants.NONCE, "nonce");
    request.addOAuthParameter(OAuthConstants.TIMESTAMP, "ts");

    assertEquals(3, request.getOauthParameters().size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionIfParameterIsNotOAuth()
  {
    request.addOAuthParameter("otherParam", "value");
  }
}
