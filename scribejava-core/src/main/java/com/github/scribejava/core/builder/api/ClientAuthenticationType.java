package com.github.scribejava.core.builder.api;

import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import java.nio.charset.Charset;
import java.util.Base64;

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
        public void addClientAuthentication(OAuthRequest request, OAuthConfig config) {
            final String apiKey = config.getApiKey();
            final String apiSecret = config.getApiSecret();
            if (apiKey != null && apiSecret != null) {
                request.addHeader(OAuthConstants.HEADER, OAuthConstants.BASIC + ' '
                        + base64Encoder.encodeToString(
                                String.format("%s:%s", apiKey, apiSecret).getBytes(Charset.forName("UTF-8"))));
            }
        }
    },
    REQUEST_BODY {
        @Override
        public void addClientAuthentication(OAuthRequest request, OAuthConfig config) {
            request.addParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
            final String apiSecret = config.getApiSecret();
            if (apiSecret != null) {
                request.addParameter(OAuthConstants.CLIENT_SECRET, apiSecret);
            }
        }
    };

    public abstract void addClientAuthentication(OAuthRequest request, OAuthConfig config);
}
