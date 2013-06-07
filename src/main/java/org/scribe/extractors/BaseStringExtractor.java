package org.scribe.extractors;

import org.scribe.model.*;

/**
 * Simple command object that extracts a base string from a {@link OAuthBaseRequest}
 * 
 * @author Pablo Fernandez
 */
public interface BaseStringExtractor
{
  /**
   * Extracts an url-encoded base string from the {@link OAuthBaseRequest}.
   * 
   * See <a href="http://oauth.net/core/1.0/#anchor14">the oauth spec</a> for more info on this.
   * 
   * @param request the OAuthBaseRequest
   * @return the url-encoded base string
   */
  String extract(OAuthBaseRequest request);
}
