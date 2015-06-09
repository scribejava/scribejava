package ru.hh.oauth.subscribe.core.extractors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.hh.oauth.subscribe.core.exceptions.OAuthException;
import ru.hh.oauth.subscribe.core.model.Token;
import ru.hh.oauth.subscribe.core.utils.OAuthEncoder;
import ru.hh.oauth.subscribe.core.utils.Preconditions;

/**
 * Default implementation of {@link AccessTokenExtractor}. Conforms to OAuth 2.0
 *
 */
public class TokenExtractor20Impl implements AccessTokenExtractor {

    private static final String TOKEN_REGEX = "access_token=([^&]+)";
    private static final String EMPTY_SECRET = "";

    /**
     * {@inheritDoc}
     */
    @Override
    public Token extract(final String response) {
        Preconditions.checkEmptyString(response,
                "Response body is incorrect. Can't extract a token from an empty string");

        final Matcher matcher = Pattern.compile(TOKEN_REGEX).matcher(response);
        if (matcher.find()) {
            final String token = OAuthEncoder.decode(matcher.group(1));
            return new Token(token, EMPTY_SECRET, response);
        } else {
            throw new OAuthException("Response body is incorrect. Can't extract a token from this: '" + response + "'",
                    null);
        }
    }
}
