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
  private SignatureType signatureType;
  private String scope;
  
  public OAuthConfig(String key, String secret)
  {
    this(key,secret,null);
  }

  //Kept for backwards compatibility
  public OAuthConfig(String key, String secret, String callback)
  {
    this(key,secret,callback,null);
  }

  public OAuthConfig(String key, String secret, String callback, SignatureType type, String scope)
  {
    this.apiKey = key;
    this.apiSecret = secret;
    this.callback = callback;
    this.signatureType = type;
    this.scope = scope;
  }
  
  public OAuthConfig(String key, String secret, String callback, SignatureType type)
  {
     this(key,secret,callback,type,null);
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

  public SignatureType getSignatureType()
  {
    return signatureType != null ? signatureType : SignatureType.Header;
  }

  public void setSignatureType(SignatureType type)
  {
    this.signatureType = type;
  }

/**
 * @param scope the scope to set
 */
public void setScope(String scope)
{
   this.scope = scope;
}

/**
 * @return the scope
 */
public String getScope()
{
   return scope;
}
}