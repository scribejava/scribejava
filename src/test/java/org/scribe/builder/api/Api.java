package org.scribe.builder.api;

import org.scribe.model.*;
import org.scribe.oauth.*;

/**
 * Contains all the configuration needed to instantiate a valid {@link OAuthService}
 * 
 * @author Pablo Fernandez
 *
 */
public interface Api
{  
  /**
   * Creates an {@link OAuthService}
   * 
   * @param apiKey your application api key
   * @param apiSecret your application api secret
   * @param callback the callback url (or 'oob' for out of band OAuth)
   * @param scope the OAuth scope
   * 
   * @return fully configured {@link OAuthService}
   */
  OAuthService createService(OAuthConfig config);
}
