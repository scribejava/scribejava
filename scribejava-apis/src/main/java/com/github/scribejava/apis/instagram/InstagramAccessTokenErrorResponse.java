package com.github.scribejava.apis.instagram;

import com.github.scribejava.core.model.OAuthResponseException;
import java.io.IOException;
import java.util.Objects;
import com.github.scribejava.core.model.Response;

/**
 * non standard Instagram replace for {@link com.github.scribejava.core.model.OAuth2AccessTokenErrorResponse}
 *
 * examples:<br>
 *
 * '{"error_type": "OAuthException", "code": 400, "error_message": "Missing required field client_id"}'
 */
public class InstagramAccessTokenErrorResponse extends OAuthResponseException {

    private static final long serialVersionUID = -1277129706699856895L;

    private final String errorType;
    private final int code;
    private final String errorMessage;
    private final Response response;

    public InstagramAccessTokenErrorResponse(String errorType, int code, String errorMessage, Response response)
            throws IOException {
        super(response);
        this.errorType = errorType;
        this.code = code;
        this.errorMessage = errorMessage;
        this.response = response;
    }

    public String getErrorType() {
        return errorType;
    }

    public int getCode() {
        return code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }

        final InstagramAccessTokenErrorResponse that = (InstagramAccessTokenErrorResponse) obj;
        if (!Objects.equals(errorMessage, that.getErrorMessage())) {
            return false;
        }
        return code == that.code && Objects.equals(errorType, that.errorType)
                && Objects.equals(response, that.response);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 83 * hash + Objects.hashCode(response);
        hash = 83 * hash + Objects.hashCode(errorMessage);
        hash = 83 * hash + Objects.hashCode(errorType);
        hash = 83 * hash + Objects.hashCode(code);
        return hash;
    }

    @Override
    public String toString() {
        return "InstagramAccessTokenErrorResponse{"
                + "errorType='" + errorType
                + "', code=" + code
                + "', errorMessage='" + errorMessage
                + "', response=" + response
                + '}';
    }
}
