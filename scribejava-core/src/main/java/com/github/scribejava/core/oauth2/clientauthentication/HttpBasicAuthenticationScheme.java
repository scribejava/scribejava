package com.github.scribejava.core.oauth2.clientauthentication;

import com.github.scribejava.core.java8.Base64;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import java.nio.charset.Charset;

/**
 * 2.3. Client Authentication<br>
 * 2.3.1. Client Password<br>
 * https://tools.ietf.org/html/rfc6749#section-2.3.1
 * <br>
 * НTTP Basic authentication scheme
 */
public class HttpBasicAuthenticationScheme implements ClientAuthentication {

    private final Base64.Encoder base64Encoder = Base64.getEncoder();

    protected HttpBasicAuthenticationScheme() {
    }

    private static class InstanceHolder {

        private static final HttpBasicAuthenticationScheme INSTANCE = new HttpBasicAuthenticationScheme();
    }

    public static HttpBasicAuthenticationScheme instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public void addClientAuthentication(OAuthRequest request, String apiKey, String apiSecret) {
        if (apiKey != null && apiSecret != null) {
            request.addHeader(OAuthConstants.HEADER, OAuthConstants.BASIC + ' '
                    + base64Encoder.encodeToString(
                            String.format("%s:%s", apiKey, apiSecret).getBytes(Charset.forName("UTF-8"))));
        }
    }

}
