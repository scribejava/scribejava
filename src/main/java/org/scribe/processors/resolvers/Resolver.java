package org.scribe.processors.resolvers;

import org.scribe.http.OAuthRequest;

/**
 */
public interface Resolver {
    /**
     * Generates an OAuth 'Authorization' Http header to include in requests as the signature.
     *
     * @param request the OAuthRequest to inspect and generate the header
     * @return the Http header value
     */
    String extract(OAuthRequest request);
}
