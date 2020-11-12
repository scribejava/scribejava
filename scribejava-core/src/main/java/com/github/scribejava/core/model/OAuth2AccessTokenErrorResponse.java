package com.github.scribejava.core.model;

import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.oauth2.OAuth2Error;
import java.io.IOException;

import java.net.URI;

/**
 * Representing <a href="https://tools.ietf.org/html/rfc6749#section-5.2">"5.2. Error Response"</a>
 */
public class OAuth2AccessTokenErrorResponse extends OAuthException {

    private static final long serialVersionUID = 2309424849700276816L;

    private final OAuth2Error error;
    private final String errorDescription;
    private final URI errorUri;
    private final Response response;

    public OAuth2AccessTokenErrorResponse(OAuth2Error error, String errorDescription, URI errorUri,
            Response rawResponse) throws IOException {
        super(rawResponse.getBody());
        this.error = error;
        this.errorDescription = errorDescription;
        this.errorUri = errorUri;
        this.response = rawResponse;
    }

    /**
     *
     * @param error error
     * @param errorDescription errorDescription
     * @param errorUri errorUri
     * @param rawResponse rawResponse
     * @throws java.io.IOException IOException
     * @deprecated use {@link #OAuth2AccessTokenErrorResponse(com.github.scribejava.core.oauth2.OAuth2Error,
     * java.lang.String, java.net.URI, com.github.scribejava.core.model.Response)
     * }
     */
    @Deprecated
    public OAuth2AccessTokenErrorResponse(OAuth2Error error, String errorDescription, URI errorUri,
            String rawResponse) throws IOException {
        this(error, errorDescription, errorUri, new Response(-1, null, null, rawResponse));
    }

    public OAuth2Error getError() {
        return error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public URI getErrorUri() {
        return errorUri;
    }

    /**
     *
     * @return body of response
     * @throws IOException IOException
     * @deprecated use {@link #getResponse()} and then {@link Response#getBody()}
     */
    @Deprecated
    public String getRawResponse() throws IOException {
        return response.getBody();
    }

    public Response getResponse() {
        return response;
    }

}
