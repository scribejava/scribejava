package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.*;

public class OAuthConfigTest
{

  @Test
  public void shouldReturnDefaultValuesIfNotSet()
  {
    OAuthConfig config = new OAuthConfig("key", "secret");
    assertEquals(Callback.outOfBand(), config.getCallback());
    assertEquals(SignatureType.Header, config.getSignatureType());
    assertFalse(config.hasScope());
  }

  @Test
  public void shouldOverrideDefaultsIfSet()
  {
    OAuthConfig config = new OAuthConfig("key", "secret", Callback.from( "http://callback" ), SignatureType.Header, "scope");
    assertEquals( Callback.from( "http://callback" ), config.getCallback());
    assertEquals("key", config.getApiKey());
    assertEquals("secret", config.getApiSecret());
  }

}
