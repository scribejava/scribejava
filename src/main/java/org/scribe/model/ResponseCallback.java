package org.scribe.model;

import org.scribe.exceptions.OAuthException;

public interface ResponseCallback
{
  public void onResponse(Response response);

  public void onError(OAuthException authException);

}
