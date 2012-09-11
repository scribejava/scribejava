/*******************************************************************************
 * Copyright (c) 2012 Arieh 'Vainolo' Bibliowicz
 * You can use this code for educational purposes. For any other uses
 * please contact me: vainolo@gmail.com
 *******************************************************************************/
package org.scribe.builder.api;

import org.scribe.model.Token;
import org.scribe.model.Verb;

public class MendeleyApi extends DefaultApi10a {

  private static final String AUTHORIZATION_URL = "http://api.mendeley.com/oauth/authorize?oauth_token=%s";

  @Override
  public String getRequestTokenEndpoint() {
    return "http://api.mendeley.com/oauth/request_token/";
  }

  @Override
  public String getAccessTokenEndpoint() {
    return "http://api.mendeley.com/oauth/access_token/";
  }

  @Override
  public String getAuthorizationUrl(Token requestToken) {
    return String.format(AUTHORIZATION_URL, requestToken.getToken());
  }

  @Override
  public Verb getAccessTokenVerb() {
    return Verb.GET;
  }

  @Override
  public Verb getRequestTokenVerb() {
    return Verb.GET;
  }
}
