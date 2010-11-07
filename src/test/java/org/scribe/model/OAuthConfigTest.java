package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.*;

public class OAuthConfigTest
{

  @Test
  public void shouldReturnDefaultValuesIfNotSet()
  {
    OAuthConfig config = new OAuthConfig();
    assertEquals(OAuthConstants.OUT_OF_BAND, config.getCallback());
  }

  @Test
  public void shouldOverrideDefaultsIfSet()
  {
    OAuthConfig config = new OAuthConfig();
    config.setCallback("http://callback");
    config.setApiKey("key");
    config.setApiSecret("secret");
    assertEquals("http://callback", config.getCallback());
    assertEquals("key", config.getApiKey());
    assertEquals("secret", config.getApiSecret());
  }

}
