package com.github.scribejava.core.extractors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuth1Token;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.utils.Preconditions;

import java.io.IOException;
import java.util.Map;


public abstract class AbstractOAuth1JSONTokenExtractor<T extends OAuth1Token> implements TokenExtractor<T> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public T extract(Response response) throws IOException {
        final String rawBody = response.getBody();
        Preconditions.checkEmptyString(rawBody,
                "Response body is incorrect. Can't extract a token from an empty string");

        @SuppressWarnings("unchecked")
        final Map<String, String> body = OBJECT_MAPPER.readValue(rawBody, Map.class);

        final String token = body.get(OAuthConstants.TOKEN);
        final String secret = body.get(OAuthConstants.TOKEN_SECRET);

        if (token == null || secret == null) {
            throw new OAuthException("Response body is incorrect. Can't extract token and secret from this: '"
                    + rawBody + '\'', null);
        }

        return createToken(token, secret, rawBody);
    }

    protected abstract T createToken(String token, String secret, String response);
}
