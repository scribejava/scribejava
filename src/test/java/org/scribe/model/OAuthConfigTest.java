package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.*;

public class OAuthConfigTest
{

  @Test
  public void shouldReturnDefaultValuesIfNotSet()
  {
    OAuthConfig config = new OAuthConfig();
    assertEquals(Verb.GET, config.getAccessTokenVerb());
    assertEquals(Verb.GET, config.getRequestTokenVerb());
    assertEquals(OAuthConstants.OUT_OF_BAND, config.getCallback());
  }

  @Test
  public void shouldOverrideDefaultsIfSet()
  {
    OAuthConfig config = new OAuthConfig();
    config.setCallback("http://callback");
    config.setAccessTokenVerb(Verb.POST);
    config.setRequestTokenVerb(Verb.POST);
    assertEquals("http://callback", config.getCallback());
    assertEquals(Verb.POST, config.getAccessTokenVerb());
    assertEquals(Verb.POST, config.getRequestTokenVerb());
  }

}
