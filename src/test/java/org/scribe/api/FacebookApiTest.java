package org.scribe.api;

import static org.junit.Assert.*;

import org.junit.*;
import org.scribe.builder.api.DefaultApi20;
import org.scribe.builder.api.FacebookApi;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class FacebookApiTest
{
  private static final String KEY = "key";
  private static final String SECRET = "secret";
  private static final String CALLBACK = "http://callback";
  private static final SignatureType TYPE = SignatureType.Header;
  private static final String SCOPE = "scope";
  private static final String STATE = "state";

  private DefaultApi20 api;
  private OAuthConfig config;

  @Before
  public void setup() throws Exception
  {
    api = new FacebookApi();
  }

  @Test
  public void authorizationUrl()
  {
    config = new OAuthConfig( KEY, SECRET, CALLBACK, TYPE, null, null );
    assertEquals("https://www.facebook.com/dialog/oauth?client_id=key&redirect_uri=http%3A%2F%2Fcallback",
                 api.getAuthorizationUrl( config ));
  }

  @Test
  public void authorizationUrlWithScope()
  {
    config = new OAuthConfig( KEY, SECRET, CALLBACK, TYPE, SCOPE, null );
    assertEquals("https://www.facebook.com/dialog/oauth?client_id=key&redirect_uri=http%3A%2F%2Fcallback&scope=scope",
                 api.getAuthorizationUrl( config ));
  }

  @Test
  public void authorizationUrlWithState()
  {
    config = new OAuthConfig( KEY, SECRET, CALLBACK, TYPE, null, STATE);
    assertEquals("https://www.facebook.com/dialog/oauth?client_id=key&redirect_uri=http%3A%2F%2Fcallback&state=state",
                 api.getAuthorizationUrl( config ));
  }

  @Test
  public void authorizationUrlWithScopeAndState()
  {
    config = new OAuthConfig( KEY, SECRET, CALLBACK, TYPE, SCOPE, STATE);
    assertEquals("https://www.facebook.com/dialog/oauth?client_id=key&redirect_uri=http%3A%2F%2Fcallback&scope=scope&state=state",
                 api.getAuthorizationUrl( config ));
  }

}