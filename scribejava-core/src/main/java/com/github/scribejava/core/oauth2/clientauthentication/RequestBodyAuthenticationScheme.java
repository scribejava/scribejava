package com.github.scribejava.core.oauth2.clientauthentication;

import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;

/**
 * 2.3. Client Authentication<br>
 * 2.3.1. Client Password<br>
 * https://tools.ietf.org/html/rfc6749#section-2.3.1
 * <br>
 * request-body authentication scheme
 */
public class RequestBodyAuthenticationScheme implements ClientAuthentication {

    protected RequestBodyAuthenticationScheme() {
    }

    private static class InstanceHolder {

        private static final RequestBodyAuthenticationScheme INSTANCE = new RequestBodyAuthenticationScheme();
    }

    public static RequestBodyAuthenticationScheme instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public void addClientAuthentication(OAuthRequest request, String apiKey, String apiSecret) {
        request.addParameter(OAuthConstants.CLIENT_ID, apiKey);
        if (apiSecret != null) {
            request.addParameter(OAuthConstants.CLIENT_SECRET, apiSecret);
        }
    }
}
