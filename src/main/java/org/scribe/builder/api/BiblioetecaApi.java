package org.scribe.builder.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.scribe.model.Token;

public class BiblioetecaApi extends DefaultApi10
{
  private static final String AUTHORIZE_URL = "http://www.biblioeteca.com/biblioeteca.web/authorize?oauth_token=%s";
  
  @Override
  public String getAccessTokenEndpoint()
  {
    return "http://api.biblioeteca.com/biblioeteca.web/access_token";
  }

  @Override
  public String getRequestTokenEndpoint()
  {
    return "http://api.biblioeteca.com/biblioeteca.web/request_token";
  }
  
  @Override
  public String getAuthorizationUrl(Token requestToken, String callback)
  {
    String url = String.format(AUTHORIZE_URL, requestToken.getToken());
    try {
        return url+"&oauth_callback="+URLEncoder.encode(callback, "UTF-8");
    } catch (UnsupportedEncodingException e) {
        return url;
    }
  }
}
