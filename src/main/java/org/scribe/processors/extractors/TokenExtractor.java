package org.scribe.processors.extractors;

import org.scribe.model.Token;

/**
 */
public interface TokenExtractor {
    /**
     * Extracts the access token from the contents of an Http Response
     *
     * @param response the contents of the response
     * @return OAuth access token
     */
    public Token extract(String response);
}
