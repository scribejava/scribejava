package com.github.scribejava.core.builder.api;

import com.github.scribejava.core.java8.Base64;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import java.nio.charset.Charset;

/**
 * Represents<br>
 * 2.3. Client Authentication<br>
 * https://tools.ietf.org/html/rfc6749#section-2.3 <br>
 * in it's part 2.3.1. Client Password<br>
 * https://tools.ietf.org/html/rfc6749#section-2.3.1
 */
public enum ClientAuthenticationType {
    HTTP_BASIC_AUTHENTICATION_SCHEME {
        private final Base64.Encoder base64Encoder = Base64.getEncoder();

        @Override
        public void addClientAuthentication(OAuthRequest request, String apiKey, String apiSecret) {
            if (apiKey != null && apiSecret != null) {
                request.addHeader(OAuthConstants.HEADER, OAuthConstants.BASIC + ' '
                        + base64Encoder.encodeToString(
                                String.format("%s:%s", apiKey, apiSecret).getBytes(Charset.forName("UTF-8"))));
            }
        }
    },
    REQUEST_BODY {
        @Override
        public void addClientAuthentication(OAuthRequest request, String apiKey, String apiSecret) {
            request.addParameter(OAuthConstants.CLIENT_ID, apiKey);
            if (apiSecret != null) {
                request.addParameter(OAuthConstants.CLIENT_SECRET, apiSecret);
            }
        }
    };

    public abstract void addClientAuthentication(OAuthRequest request, String apiKey, String apiSecret);
}
