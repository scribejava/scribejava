package org.scribe.test.helpers;

import java.net.MalformedURLException;

import org.scribe.model.*;

public class ObjectMother
{

  public static OAuthRequest createSampleOAuthRequest() throws MalformedURLException
  {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");
    request.addOAuthParameter(OAuthConstants.TIMESTAMP, "123456");
    request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, "AS#$^*@&");
    request.addOAuthParameter(OAuthConstants.CALLBACK, "http://example/callback");
    request.addOAuthParameter(OAuthConstants.SIGNATURE, "OAuth-Signature");
    return request;
  }
  
  public static OAuthRequest createSampleOAuthRequest2() throws MalformedURLException
  {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://EXAMPLE.com");
    request.addOAuthParameter(OAuthConstants.TIMESTAMP, "123456");
    request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, "AS#$^*@&");
    request.addOAuthParameter(OAuthConstants.CALLBACK, "http://example/callback");
    request.addOAuthParameter(OAuthConstants.SIGNATURE, "OAuth-Signature");
    return request;
  }
}
