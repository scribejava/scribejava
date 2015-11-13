package com.github.scribejava.core.extractors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.utils.Preconditions;

public class JsonTokenExtractor implements AccessTokenExtractor {

    private static final Pattern ACCESS_TOKEN_PATTERN = Pattern.compile("\"access_token\"\\s*:\\s*\"(\\S*?)\"");

    @Override
    public Token extract(final String response) {
        return new Token(extractAccessToken(response), "", response);
    }

    protected String extractAccessToken(final String response) {
        Preconditions.checkEmptyString(response, "Cannot extract a token from a null or empty String");
        final Matcher matcher = ACCESS_TOKEN_PATTERN.matcher(response);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new OAuthException("Cannot extract an access token. Response was: " + response);
        }
    }
}
