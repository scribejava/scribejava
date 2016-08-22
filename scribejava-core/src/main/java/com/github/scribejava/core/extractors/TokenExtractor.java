package com.github.scribejava.core.extractors;

import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;

import java.io.IOException;

/**
 * Simple command object that extracts a concrete {@link Token} from a String
 * @param <T> concrete type of Token
 */
public interface TokenExtractor<T extends Token> {

    /**
     * Extracts the concrete type of token from the contents of an Http Response
     *
     * @param response the contents of the response
     * @return OAuth access token
     */
    T extract(Response response) throws IOException, OAuthException;
}
