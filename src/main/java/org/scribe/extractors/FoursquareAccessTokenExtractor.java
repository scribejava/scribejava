package org.scribe.extractors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scribe.exceptions.OAuthException;
import org.scribe.extractors.TokenExtractor20Impl;
import org.scribe.model.Token;
import org.scribe.utils.Preconditions;
import org.scribe.utils.URLUtils;

public class FoursquareAccessTokenExtractor extends TokenExtractor20Impl {

    private static final String TOKEN_REGEX = ".[\\s]+\"access_token\":\"(\\S*?)(&(\\S*))?\"[\\s]+.";
    private static final String EMPTY_SECRET = "";

    public Token extract(String response) {
        Preconditions.checkEmptyString(response, "Response body is incorrect. Can't extract a token from an empty string");

        Matcher matcher = Pattern.compile(TOKEN_REGEX).matcher(response);
        if (matcher.matches()) {
            String token = URLUtils.formURLDecode(matcher.group(1));
            return new Token(token, EMPTY_SECRET);
        } else {
            throw new OAuthException("Response body is incorrect. Can't extract a token from this: '" + response + "'", null);
        }
    }
}
