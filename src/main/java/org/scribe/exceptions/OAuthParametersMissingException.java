package org.scribe.exceptions;

import org.scribe.model.*;

public class OAuthParametersMissingException extends OAuthException
{

  private static final long serialVersionUID = 1745308760111976671L;
  private static final String MSG = "Could not find oauth parameters in request: %s. "
      + "OAuth parameters must be specified with the addOAuthParameter() method";

  public OAuthParametersMissingException(OAuthRequest request)
  {
    super(String.format(MSG, request));
  }
}
