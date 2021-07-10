package com.github.scribejava.apis.facebook;

import com.github.scribejava.core.model.OAuthResponseException;
import com.github.scribejava.core.model.Response;
import java.io.IOException;
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
public class FacebookAccessTokenErrorResponse extends OAuthResponseException {

    private static final long serialVersionUID = -1277129766099856895L;

    private final String errorMessage;
    private final String type;
    private final int codeInt;
    private final String fbtraceId;

    public FacebookAccessTokenErrorResponse(String errorMessage, String type, int code, String fbtraceId,
            Response response)
            throws IOException {
        super(response);
        this.errorMessage = errorMessage;
        this.type = type;
        this.codeInt = code;
        this.fbtraceId = fbtraceId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getType() {
        return type;
    }

    public int getCodeInt() {
        return codeInt;
    }

    public String getFbtraceId() {
        return fbtraceId;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 83 * hash + Objects.hashCode(errorMessage);
        hash = 83 * hash + Objects.hashCode(type);
        hash = 83 * hash + Objects.hashCode(codeInt);
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
        if (!super.equals(obj)) {
            return false;
        }

        final FacebookAccessTokenErrorResponse other = (FacebookAccessTokenErrorResponse) obj;

        if (!Objects.equals(errorMessage, other.getErrorMessage())) {
            return false;
        }
        if (!Objects.equals(type, other.getType())) {
            return false;
        }
        if (codeInt != other.getCodeInt()) {
            return false;
        }
        return Objects.equals(fbtraceId, other.getFbtraceId());
    }

    @Override
    public String toString() {
        return "FacebookAccessTokenErrorResponse{'type'='" + type + "', 'codeInt'='" + codeInt
                + "', 'fbtraceId'='" + fbtraceId + "', 'response'='" + getResponse()
                + "', 'errorMessage'='" + errorMessage + "'}";
    }
}
