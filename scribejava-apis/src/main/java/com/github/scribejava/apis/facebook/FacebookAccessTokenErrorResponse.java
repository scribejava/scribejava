package com.github.scribejava.apis.facebook;

import com.github.scribejava.core.exceptions.OAuthException;
import java.util.Objects;

/**
 * non standard Facebook replace for {@link com.github.scribejava.core.model.OAuth2AccessTokenErrorResponse}
 *
 * examples:<br>
 *
 * '{"error":{"message":"This authorization code has been
 * used.","type":"OAuthException","code":100,"fbtrace_id":"DtxvtGRaxbB"}}'<br>
 *
 * '{"error":{"message":"Error validating application. Invalid application
 * ID.","type":"OAuthException","code":101,"fbtrace_id":"CvDR+X4WWIx"}}'
 */
public class FacebookAccessTokenErrorResponse extends OAuthException {

    private static final long serialVersionUID = -1277129766099856895L;

    private final String type;
    private final String code;
    private final String fbtraceId;
    private final String rawResponse;

    public FacebookAccessTokenErrorResponse(String message, String type, String code, String fbtraceId,
            String rawResponse) {
        super(message);
        this.type = type;
        this.code = code;
        this.fbtraceId = fbtraceId;
        this.rawResponse = rawResponse;
    }

    public String getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public String getFbtraceId() {
        return fbtraceId;
    }

    public String getRawResponse() {
        return rawResponse;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(rawResponse);
        hash = 83 * hash + Objects.hashCode(getMessage());
        hash = 83 * hash + Objects.hashCode(type);
        hash = 83 * hash + Objects.hashCode(code);
        hash = 83 * hash + Objects.hashCode(fbtraceId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FacebookAccessTokenErrorResponse other = (FacebookAccessTokenErrorResponse) obj;
        if (!Objects.equals(rawResponse, other.getRawResponse())) {
            return false;
        }
        if (!Objects.equals(getMessage(), other.getMessage())) {
            return false;
        }
        if (!Objects.equals(type, other.getType())) {
            return false;
        }
        if (!Objects.equals(code, other.getCode())) {
            return false;
        }
        return Objects.equals(fbtraceId, other.getFbtraceId());
    }

    @Override
    public String toString() {
        return "FacebookAccessTokenErrorResponse{'type'='" + type + "', 'code'='" + code
                + "', 'fbtraceId'='" + fbtraceId + "', 'rawResponse'='" + rawResponse
                + "', 'message'='" + getMessage() + "'}";
    }
}
