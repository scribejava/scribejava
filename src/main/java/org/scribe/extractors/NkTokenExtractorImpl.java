package org.scribe.extractors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scribe.exceptions.OAuthException;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.model.Token;
import org.scribe.utils.Preconditions;
import org.scribe.utils.URLUtils;

public class NkTokenExtractorImpl implements AccessTokenExtractor {

    private static final String TOKEN_REGEX = "access_token\":\"([^\"]+)";
    private static final Pattern TOKEN_PATTERN = Pattern.compile(TOKEN_REGEX);
    private static final String EMPTY_SECRET = "";

    /**
     * {@inheritDoc}
     */
    public Token extract(String response) {
        Preconditions.checkEmptyString(response,
                "Response body is incorrect. Can't extract a token from an empty string");

        Matcher matcher = TOKEN_PATTERN.matcher(response);
        if (matcher.find()) {
            String token = URLUtils.formURLDecode(matcher.group(1));
            return new Token(token, EMPTY_SECRET, response);
        } else {
            throw new OAuthException("Response body is incorrect. Can't extract a token from this: '" + response + "'",
                    null);
        }
    }
}
