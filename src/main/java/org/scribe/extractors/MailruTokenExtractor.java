package org.scribe.extractors;

import org.scribe.exceptions.OAuthException;
import org.scribe.model.Token;
import org.scribe.utils.Preconditions;

/**
 * User: elwood
 * Date: 23.03.2011
 * Time: 11:59:39
 */
public class MailruTokenExtractor implements AccessTokenExtractor {

    private final static String EMPTY_SECRET = "";

    /**
     * {@inheritDoc}
     */
    public Token extract(String response) {
        Preconditions.checkEmptyString(response, "Response body is incorrect. Can't extract a token and uid from an empty string");
        //
        String accessTokenMarker = "\"access_token\":\"";
        int indexOfMarker = response.indexOf(accessTokenMarker);
        if (-1 == indexOfMarker) {
            throw new OAuthException("Response body is incorrect. Can't extract a token from this: '" + response + "'", null);
        }
        try {
            String substr = response.substring(indexOfMarker + accessTokenMarker.length());
            String accessTokenValue = substr.substring(0, substr.indexOf("\""));
            //
            return new Token(accessTokenValue, EMPTY_SECRET);
        } catch (IndexOutOfBoundsException e) {
            throw new OAuthException("Response body is incorrect. Can't extract a token and uid from this: '" + response + "'", e);
        }
    }
}
