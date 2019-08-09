package com.github.scribejava.core.extractors;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuth1Token;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;

/**
 * Abstract base implementation of {@link TokenExtractor} for OAuth 1.0a
 *
 * The process for extracting access and request tokens is similar so this class can do both things.
 *
 * @param <T> concrete type of OAuth1Token. access or request
 */
public abstract class AbstractOAuth1TokenExtractor<T extends OAuth1Token> implements TokenExtractor<T> {

    private static final Pattern OAUTH_TOKEN_REGEXP_PATTERN = Pattern.compile("oauth_token=([^&]+)");
    private static final Pattern OAUTH_TOKEN_SECRET_REGEXP_PATTERN = Pattern.compile("oauth_token_secret=([^&]*)");

    /**
     * {@inheritDoc}
     */
    @Override
    public T extract(Response response) throws IOException {
        final String body = response.getBody();
        Preconditions.checkEmptyString(body,
                "Response body is incorrect. Can't extract a token from an empty string");
        final String token = extract(body, OAUTH_TOKEN_REGEXP_PATTERN);
        final String secret = extract(body, OAUTH_TOKEN_SECRET_REGEXP_PATTERN);
        return createToken(token, secret, body);
    }

    private String extract(String response, Pattern p) {
        final Matcher matcher = p.matcher(response);
        if (matcher.find() && matcher.groupCount() >= 1) {
            return OAuthEncoder.decode(matcher.group(1));
        } else {
            throw new OAuthException("Response body is incorrect. Can't extract token and secret from this: '"
                    + response + "'", null);
        }
    }

    protected abstract T createToken(String token, String secret, String response);
}
