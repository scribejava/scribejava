package org.scribe.extractors;

import org.scribe.model.Token;

public interface RefreshTokenExtractor
{
    /**
     * Extracts the refresh token from the contents of an Http Response
     * 
     * @param response
     *            the contents of the response
     * @return OAuth refresh token
     */
    public Token extract(String response);
}
