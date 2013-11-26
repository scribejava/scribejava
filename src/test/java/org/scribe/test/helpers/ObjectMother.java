package org.scribe.test.helpers;

import org.scribe.model.*;

public class ObjectMother
{

  public static OAuthRequest createSampleOAuthRequest()
  {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
    request.addOAuthParameter(OAuthConstants.TIMESTAMP, "123456");
    request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, "AS#$^*@&");
    request.addOAuthParameter(OAuthConstants.CALLBACK, "http://example/callback");
    request.addOAuthParameter(OAuthConstants.SIGNATURE, "OAuth-Signature");
    return request;
  }
  
  public static OAuthRequest createSampleOAuthRequestPort80()
  {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com:80");
    request.addOAuthParameter(OAuthConstants.TIMESTAMP, "123456");
    request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, "AS#$^*@&");
    request.addOAuthParameter(OAuthConstants.CALLBACK, "http://example/callback");
    request.addOAuthParameter(OAuthConstants.SIGNATURE, "OAuth-Signature");
    return request;
  }
  
  public static OAuthRequest createSampleOAuthRequestPort80_2()
  {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com:80/test");
    request.addOAuthParameter(OAuthConstants.TIMESTAMP, "123456");
    request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, "AS#$^*@&");
    request.addOAuthParameter(OAuthConstants.CALLBACK, "http://example/callback");
    request.addOAuthParameter(OAuthConstants.SIGNATURE, "OAuth-Signature");
    return request;
  }
  
  public static OAuthRequest createSampleOAuthRequestPort8080()
  {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com:8080");
    request.addOAuthParameter(OAuthConstants.TIMESTAMP, "123456");
    request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, "AS#$^*@&");
    request.addOAuthParameter(OAuthConstants.CALLBACK, "http://example/callback");
    request.addOAuthParameter(OAuthConstants.SIGNATURE, "OAuth-Signature");
    return request;
  }
  
  public static OAuthRequest createSampleOAuthRequestPort443()
  {
    OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com:443");
    request.addOAuthParameter(OAuthConstants.TIMESTAMP, "123456");
    request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, "AS#$^*@&");
    request.addOAuthParameter(OAuthConstants.CALLBACK, "http://example/callback");
    request.addOAuthParameter(OAuthConstants.SIGNATURE, "OAuth-Signature");
    return request;
  }
  
  public static OAuthRequest createSampleOAuthRequestPort443_2()
  {
    OAuthRequest request = new OAuthRequest(Verb.GET, "https://example.com:443/test");
    request.addOAuthParameter(OAuthConstants.TIMESTAMP, "123456");
    request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, "AS#$^*@&");
    request.addOAuthParameter(OAuthConstants.CALLBACK, "http://example/callback");
    request.addOAuthParameter(OAuthConstants.SIGNATURE, "OAuth-Signature");
    return request;
  }
}
