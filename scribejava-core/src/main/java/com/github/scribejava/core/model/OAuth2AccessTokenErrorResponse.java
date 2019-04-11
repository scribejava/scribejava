package com.github.scribejava.core.model;

import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.oauth2.OAuth2Error;

import java.net.URI;

/**
 * Representing <a href="https://tools.ietf.org/html/rfc6749#section-5.2">"5.2. Error Response"</a>
 */
public class OAuth2AccessTokenErrorResponse extends OAuthException {

    private static final long serialVersionUID = 2309424849700276816L;

    /**
     * @deprecated use {@link com.github.scribejava.core.oauth2.OAuth2Error}
     */
    @Deprecated
    public enum ErrorCode {
        INVALID_REQUEST("invalid_request"),
        INVALID_CLIENT("invalid_client"),
        INVALID_GRANT("invalid_grant"),
        UNAUTHORIZED_CLIENT("unauthorized_client"),
        UNSUPPORTED_GRANT_TYPE("unsupported_grant_type"),
        INVALID_SCOPE("invalid_scope"),
        /**
         * @see <a href="https://tools.ietf.org/html/rfc7009#section-2.2.1">RFC 7009, 2.2.1. Error Response</a>
         */
        UNSUPPORTED_TOKEN_TYPE("unsupported_token_type");

        private final String errorCodeString;

        ErrorCode(String errorCodeString) {
            this.errorCodeString = errorCodeString;
        }

        public static ErrorCode parseFrom(String errorCodeString) {
            for (ErrorCode errorCode : ErrorCode.values()) {
                if (errorCode.errorCodeString.equals(errorCodeString)) {
                    return errorCode;
                }
            }
            throw new IllegalArgumentException("there is no knowlege about '" + errorCodeString + "' ErrorCode");
        }
    }

    private final ErrorCode errorCode;
    private final OAuth2Error error;
    private final String errorDescription;
    private final URI errorUri;
    private final String rawResponse;

    /**
     * @param errorCode errorCode
     * @param errorDescription errorDescription
     * @param errorUri errorUri
     * @param rawResponse rawResponse
     * @deprecated use {@link #OAuth2AccessTokenErrorResponse(com.github.scribejava.core.oauth2.OAuth2Error,
     * java.lang.String, java.net.URI, java.lang.String)}
     */
    @Deprecated
    public OAuth2AccessTokenErrorResponse(ErrorCode errorCode, String errorDescription, URI errorUri,
            String rawResponse) {
        super(rawResponse);
        this.errorCode = errorCode;
        this.error = OAuth2Error.parseFrom(errorCode.errorCodeString);
        this.errorDescription = errorDescription;
        this.errorUri = errorUri;
        this.rawResponse = rawResponse;
    }

    public OAuth2AccessTokenErrorResponse(OAuth2Error error, String errorDescription, URI errorUri,
            String rawResponse) {
        super(rawResponse);
        ErrorCode oldErrorCode;
        try {
            oldErrorCode = ErrorCode.parseFrom(error.getErrorString());
        } catch (IllegalArgumentException iaE) {
            oldErrorCode = null;
        }
        this.errorCode = oldErrorCode;
        this.error = error;
        this.errorDescription = errorDescription;
        this.errorUri = errorUri;
        this.rawResponse = rawResponse;
    }

    public OAuth2Error getError() {
        return error;
    }

    /**
     * @return error code
     * @deprecated use {@link #getError() }
     */
    @Deprecated
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public URI getErrorUri() {
        return errorUri;
    }

    public String getRawResponse() {
        return rawResponse;
    }
}
