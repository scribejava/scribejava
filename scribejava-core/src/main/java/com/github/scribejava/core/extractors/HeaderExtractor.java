package com.github.scribejava.core.extractors;

import com.github.scribejava.core.model.AbstractRequest;

/**
 * Simple command object that generates an OAuth Authorization header to include in the request.
 *
 * @author Pablo Fernandez
 */
public interface HeaderExtractor {

    /**
     * Generates an OAuth 'Authorization' Http header to include in requests as the signature.
     *
     * @param request the AbstractRequest to inspect and generate the header
     * @return the Http header value
     */
    String extract(AbstractRequest request);
}
