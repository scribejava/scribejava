package org.scribe.builder.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.scribe.model.*;

public class DropBoxApi extends DefaultApi10
{
  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://api.dropbox.com/1/oauth/access_token";
  }

  @Override
  public String getAuthorizationUrl(Token requestToken, String callback)
  {
    String url = "https://www.dropbox.com/1/oauth/authorize?oauth_token="+requestToken.getToken();
    try {
        return url+"&oauth_callback="+URLEncoder.encode(callback, "UTF-8");
    } catch (UnsupportedEncodingException e) {
        return url;
    }
  }

  @Override
  public String getRequestTokenEndpoint()
  {
    return "https://api.dropbox.com/1/oauth/request_token";
  }

}