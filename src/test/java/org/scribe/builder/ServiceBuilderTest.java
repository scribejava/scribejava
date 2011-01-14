package org.scribe.builder;

import static org.junit.Assert.*;

import org.junit.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

public class ServiceBuilderTest
{
  private ServiceBuilder builder;

  @Before
  public void setup()
  {
    builder = new ServiceBuilder();
  }

  @Test
  public void shouldReturnOOBasDefaultCallback()
  {
    builder.provider(ApiMock.class).apiKey("key").apiSecret("secret").build();
    assertEquals(ApiMock.config.getApiKey(), "key");
    assertEquals(ApiMock.config.getApiSecret(), "secret");
    assertEquals(ApiMock.config.getCallback(), OAuthConstants.OUT_OF_BAND);
  }

  @Test
  public void shouldAcceptValidCallbackUrl()
  {
    builder.provider(ApiMock.class).apiKey("key").apiSecret("secret").callback("http://example.com").build();
    assertEquals(ApiMock.config.getApiKey(), "key");
    assertEquals(ApiMock.config.getApiSecret(), "secret");
    assertEquals(ApiMock.config.getCallback(), "http://example.com");
  }

  @Test(expected=IllegalArgumentException.class)
  public void shouldNotAcceptAnInvalidUrlAsCallback()
  {
    builder.provider(ApiMock.class).apiKey("key").apiSecret("secret").callback("example.com").build(); 
  }

  @Test
  public void shouldAcceptAnScope()
  {
    builder.provider(ApiMock.class).apiKey("key").apiSecret("secret").scope("rss-api").build();
    assertEquals(ApiMock.config.getApiKey(), "key");
    assertEquals(ApiMock.config.getApiSecret(), "secret");
    assertEquals(ApiMock.scope, "rss-api");
  }

  public static class ApiMock implements Api
  {
    public static OAuthConfig config;
    public static String scope;
    
    @Override
    public OAuthService createService(OAuthConfig config, String scope)
    {
      ApiMock.config = config;
      ApiMock.scope = scope;
      return null;
    }
  }
}
