package org.scribe.model;

import java.util.*;

public class OAuthRequest extends Request
{

  private static final String OAUTH_PREFIX = "oauth_";
  private Map<String, String> oauthParameters;

  public OAuthRequest(Verb verb, String url)
  {
    super(verb, url);
    this.oauthParameters = new HashMap<String, String>();
  }

  public void addOAuthParameter(String key, String value)
  {
    oauthParameters.put(checkKey(key), value);
  }

  private String checkKey(String key)
  {
    if (!key.startsWith(OAUTH_PREFIX))
    {
      throw new IllegalArgumentException(String.format("OAuth parameters must start with '%s'", OAUTH_PREFIX));
    } else
    {
      return key;
    }
  }

  public Map<String, String> getOauthParameters()
  {
    return oauthParameters;
  }

  @Override
  public String toString()
  {
    return String.format("@OAuthRequest(%s, %s)", getVerb(), getUrl());
  }
}
