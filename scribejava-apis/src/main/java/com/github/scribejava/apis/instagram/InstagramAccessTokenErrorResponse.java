package com.github.scribejava.apis.instagram;

import java.io.IOException;
import java.util.Objects;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.Response;

/**
 * non standard Instagram replace for
 * {@link com.github.scribejava.core.model.OAuth2AccessTokenErrorResponse}
 *
 * examples:<br>
 *
 * '{"error_type": "OAuthException", "code": 400, "error_message": "Missing required field client_id"}'
 */
public class InstagramAccessTokenErrorResponse extends OAuthException {

    private static final long serialVersionUID = -1277129766099856895L;

    private final String errorType;
    private final int codeInt;
    private final Response response;

    public InstagramAccessTokenErrorResponse(String errorType, int code,
       String errorMessage, Response response) {
        super(errorMessage);
        this.errorType = errorType;
        this.codeInt = code;
        this.response = response;
    }

    public String getErrorType() {
        return errorType;
    }

    public int getCodeInt() {
        return codeInt;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (o == null || getClass() != o.getClass()) {
          return false;
        }
        InstagramAccessTokenErrorResponse that = (InstagramAccessTokenErrorResponse) o;
        return codeInt == that.codeInt && Objects.equals(errorType, that.errorType)
               && Objects.equals(response, that.response);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(response);
        hash = 83 * hash + Objects.hashCode(getMessage());
        hash = 83 * hash + Objects.hashCode(errorType);
        hash = 83 * hash + Objects.hashCode(codeInt);
        return hash;
    }

    @Override
    public String toString() {
        return "InstagramAccessTokenErrorResponse{" +
               "errorType='" + errorType + '\'' +
               ", codeInt=" + codeInt +
               ", response=" + response +
               '}';
    }
}
