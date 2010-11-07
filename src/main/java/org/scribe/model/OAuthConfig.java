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
  
  public OAuthConfig(String key, String secret)
  {
    this(key,secret,null);
  }
  
  public OAuthConfig(String key, String secret, String callback)
  {
    this.apiKey = key;
    this.apiSecret = secret;
    this.callback = callback;
  }

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
}