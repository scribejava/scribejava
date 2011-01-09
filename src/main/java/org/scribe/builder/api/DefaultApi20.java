package org.scribe.builder.api;

import org.scribe.model.*;
import org.scribe.oauth.*;

/**
 * Default implementation of the OAuth protocol, version 2.0 (draft 11)
 *
 * This class is meant to be extended by concrete implementations of the API,
 * providing the endpoints and endpoint-http-verbs.
 *
 * If your Api adheres to the 2.0 (draft 11) protocol correctly, you just need to extend
 * this class and define the getters for your endpoints.
 *
 * If your Api does something a bit different, you can override the different
 * extractors or services, in order to fine-tune the process. Please read the
 * javadocs of the interfaces to get an idea of what to do.
 *
 * @author Diego Silveira
 *
 */
public abstract class DefaultApi20 implements Api
{

  /**
   * Returns the URL where you should redirect your users to authenticate
   * your application.
   *
   * @param config OAuth 2.0 configuration param object
   * @return the URL where you should redirect your users
   */
  public abstract String getAuthorizationUrl(OAuthConfig config);

  /**
   * {@inheritDoc}
   */
  public OAuthService createService(OAuthConfig config, String scope)
  {
    return new OAuth20ServiceImpl(this, config);
  }

}
