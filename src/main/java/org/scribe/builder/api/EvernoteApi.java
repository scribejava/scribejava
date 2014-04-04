package org.scribe.builder.api;

import org.scribe.model.Token;

/**
 * OAuth API for Evernote
 *
 * @author Norbert Potocki
 */
public class EvernoteApi extends DefaultApi10a
{
  protected String serviceUrl() {
    return "https://www.evernote.com";
  }

  @Override
  public String getRequestTokenEndpoint()
  {
    return serviceUrl() + "/oauth";
  }

  @Override
  public String getAccessTokenEndpoint()
  {
    return serviceUrl() + "/oauth";
  }

  @Override
  public String getAuthorizationUrl(Token requestToken)
  {
    return String.format(serviceUrl() + "/OAuth.action?oauth_token=%s", requestToken.getToken());
  }

  /**
   * Sandbox endpoint
   */
  public static class Sandbox extends EvernoteApi
  {
    @Override
    protected String serviceUrl() {
      return "https://sandbox.evernote.com";
    }
  }

  /**
   * Yinxiang Biji endpoint
   */
  public static class Yinxiang extends EvernoteApi
  {
    @Override
    protected String serviceUrl() {
      return "https://app.yinxiang.com";
    }
  }
}
