package org.scribe.builder.api;

import org.scribe.model.*;
import org.scribe.exceptions.*;

public class EchoApi extends DefaultApi10a
{
  @Override
  public String getAccessTokenEndpoint()
  {
    throw new OAuthException("Echo API only supports 2-legged OAuth calls");
  }

  @Override
  public String getAuthorizationUrl(Token requestToken)
  {
    throw new OAuthException("Echo API only supports 2-legged OAuth calls");
  }

  @Override
  public String getRequestTokenEndpoint()
  {
    throw new OAuthException("Echo API only supports 2-legged OAuth calls");
  }
}

