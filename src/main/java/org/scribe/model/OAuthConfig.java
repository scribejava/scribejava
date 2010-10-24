package org.scribe.model;

/**
 * Parameter object that groups OAuth config values
 * 
 * @author Pablo Fernandez
 */
public class OAuthConfig
{
  private String apiKey;
  private String apiSecret;
  private String callback;
  private String requestTokenEndpoint;
  private String accessTokenEndpoint;
  private Verb requestTokenVerb;
  private Verb accessTokenVerb;

  public String getApiKey()
  {
    return apiKey;
  }

  public void setApiKey(String apiKey)
  {
    this.apiKey = apiKey;
  }

  public String getApiSecret()
  {
    return apiSecret;
  }

  public void setApiSecret(String apiSecret)
  {
    this.apiSecret = apiSecret;
  }

  public String getCallback()
  {
    return callback != null ? callback : OAuthConstants.OUT_OF_BAND;
  }

  public void setCallback(String callback)
  {
    this.callback = callback;
  }

  public String getRequestTokenEndpoint()
  {
    return requestTokenEndpoint;
  }

  public void setRequestTokenEndpoint(String requestTokenEndpoint)
  {
    this.requestTokenEndpoint = requestTokenEndpoint;
  }

  public String getAccessTokenEndpoint()
  {
    return accessTokenEndpoint;
  }

  public void setAccessTokenEndpoint(String accessTokenEndpoint)
  {
    this.accessTokenEndpoint = accessTokenEndpoint;
  }

  public Verb getRequestTokenVerb()
  {
    return requestTokenVerb != null ? requestTokenVerb : Verb.GET;
  }

  public void setRequestTokenVerb(Verb requestTokenVerb)
  {
    this.requestTokenVerb = requestTokenVerb;
  }

  public Verb getAccessTokenVerb()
  {
    return accessTokenVerb != null ? accessTokenVerb : Verb.GET;
  }

  public void setAccessTokenVerb(Verb accessTokenVerb)
  {
    this.accessTokenVerb = accessTokenVerb;
  }

}